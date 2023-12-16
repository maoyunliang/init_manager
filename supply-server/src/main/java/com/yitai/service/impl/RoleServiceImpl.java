package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.admin.dto.role.RoleAssDTO;
import com.yitai.admin.dto.role.RoleDTO;
import com.yitai.admin.dto.role.RolePageQueryDTO;
import com.yitai.admin.entity.*;
import com.yitai.admin.vo.DepartmentVO;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.RoleVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.RoleMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.result.PageResult;
import com.yitai.service.RoleService;
import com.yitai.utils.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: RoleServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:40
 * @Version: 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    MangerProperties mangerProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult pageQuery(RolePageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        Page<Role> page = roleMapper.pageQuery(pageQueryDTO);
        long total = page.getTotal();
        List<Role> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void save(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleType("01");
        int record = roleMapper.save(role, roleDTO.getTenantId());
        Long roleId = role.getId();
        //判断是否关联菜单
        if(!CollectionUtil.isEmpty(roleDTO.getMenuIds())){
            List<MenuRole> menuRoles = new ArrayList<>();
            for (Long menuId : roleDTO.getMenuIds()) {
//            MenuRole menuRole = new MenuRole();
                menuRoles.add(MenuRole.builder().menuId(menuId).roleId(roleId).build());
            }
            roleMapper.assMenu(menuRoles, roleDTO.getTenantId());
        }
    }

    @Override
    public void delete(RoleDTO deleteRoleDTO) {
        Long roleId = deleteRoleDTO.getId();
        Long tenantId = deleteRoleDTO.getTenantId();
        // 查询是否存在该角色
        if(ObjectUtil.isNull(roleMapper.getRoleById(roleId,tenantId))){
            throw new ServiceException("请输入正确的角色");
        }
        // 是否可以被删除
        isSysRole(roleId, tenantId);
        // 查询角色是否关联用户
        List<UserVO> userList = roleMapper.selectUserByRoleId(roleId, tenantId);
        if(!CollectionUtil.isEmpty(userList)){
            throw new ServiceException("当前角色关联相关用户");
        }
        //查询角色是否关联菜单
        List<MenuVO> menuList = roleMapper.selectByRoleId(roleId, tenantId);
        if(!CollectionUtil.isEmpty(menuList)){
            throw new ServiceException("当前角色关联相关菜单");
        }
        roleMapper.deleteById(roleId,tenantId);
    }

    @Override
    public void update(RoleDTO roleDTO) {
        Long roleId = roleDTO.getId();
        Long tenantId = roleDTO.getTenantId();
        if(ObjectUtil.isNull(roleMapper.getRoleById(roleId,tenantId))){
            throw new ServiceException("角色id不存在");
        }
        //判断是否是系统管理员（平台级别用户无法操作系统管理员）
        isSysRole(roleId, tenantId);
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        roleMapper.update(role, roleDTO.getTenantId());
    }
    /**
     * 给角色分配菜单
     */
    @Override
    public void assMenu(RoleAssDTO roleMenuDTO) {
        Long roleId = roleMenuDTO.getRoleId();
        Long tenantId = roleMenuDTO.getTenantId();
        //判断是否是系统管理员（平台级别用户无法操作系统管理员）
        isSysRole(roleId, tenantId);
        // 删除原有的菜单
        roleMapper.emptyMenu(roleId, tenantId);
        if(!CollectionUtil.isEmpty(roleMenuDTO.getMenuIds())){
            List<MenuRole> menuRoles = roleMenuDTO.getMenuIds().stream()
                    .map(menuId -> MenuRole.builder()
                    .menuId(menuId).roleId(roleId).build())
                    .toList();
            roleMapper.assMenu(menuRoles, tenantId);
        }
        // 菜单重新分配后，查询角色关联用户、删除redis用户相关缓存
        deleteCache(RedisConstant.USER_PERMISSION, roleId, tenantId);
    }

    @Override
    public void assDept(RoleAssDTO roleDeptDTO) {
        Long tenantId = roleDeptDTO.getTenantId();
        Long roleId = roleDeptDTO.getRoleId();
        //判断是否是系统管理员（平台级别用户无法操作系统管理员）
        isSysRole(roleId, tenantId);
        //清除之前关联的部门
        roleMapper.emptyDept(roleId, tenantId);
        if(!CollectionUtil.isEmpty(roleDeptDTO.getDeptIds())){
            List<RoleDepartment> roleDepartments = roleDeptDTO.getDeptIds()
                    .stream()
                    .map(e -> RoleDepartment.builder().deptId(e).roleId(roleId).build())
                    .collect(Collectors.toList());
            roleMapper.assDept(roleDepartments, tenantId);
        }

        // 数据权限重新分配后，查询角色关联用户、删除redis用户相关缓存
        deleteCache(RedisConstant.DATASCOPE, roleId, tenantId);
    }

    /*
     * 分配用户
     */
    @Override
    public void assUser(RoleAssDTO roleUserDTO) {
        Long tenantId = roleUserDTO.getTenantId();
        Long roleId = roleUserDTO.getRoleId();
        isSysRole(roleId, tenantId);
        //清除之前的用户
        roleMapper.emptyUser(roleId, tenantId);
        if(!CollectionUtil.isEmpty(roleUserDTO.getUserIds())){
            List<UserRole> userRoles = roleUserDTO.getUserIds().stream()
                    .map(userId -> UserRole.builder().userId(userId).roleId(roleId).build()).toList();
            roleMapper.assUser(userRoles,roleUserDTO.getTenantId());
        }
    }

    public void isSysRole(Long roleId, Long tenantId){
        Role role = roleMapper.getRoleById(roleId, tenantId);
        User user = BaseContext.getCurrentUser();
        //平台用户无法修改系统管理员权限
        if(!mangerProperties.getUserId().contains(user.getId())){
            if(role.getRoleType().equals("00")){
                throw new ServiceException("无法修改系统管理员");
            }
        }
    }
    public void deleteCache(String key, Long roleId, Long tenantId){
        List<UserVO> users = roleMapper.selectUserByRoleId(roleId,tenantId);
        List<String> userKey = users.stream().
                map(e -> key.concat(e.getId().toString())).toList();
        redisTemplate.delete(userKey);
        Role role = new Role();
        role.setId(roleId);
        //角色操作记录更新
        roleMapper.update(role, tenantId);
    }

    @Override
    public RoleVO getUser(RoleDTO roleInfoDTO, List<DepartmentVO> departmentVOS) {
        RoleVO roleVO = new RoleVO();
        Long roleId = roleInfoDTO.getId();
        Long tenantId = roleInfoDTO.getTenantId();
        Role role = roleMapper.getRoleById(roleId, tenantId);
        if(ObjectUtil.isNull(role)){
            throw new ServiceException("你查找的角色不存在");
        }
        BeanUtils.copyProperties(role, roleVO);
        // 查询角色关联用户集合
        List<UserVO> userVOS = roleMapper.selectUserByRoleId(roleId, tenantId);
        //如果存在
        if(!CollectionUtil.isEmpty(userVOS)){
            // Map<Long, List<UserVO>> allUsersMap
            Map<Long, UserVO> hasUserMap = userVOS.stream()
                    .collect(Collectors.toMap(UserVO::getId, userVO -> {
                        userVO.setHasUser(1);
                        return userVO;
                    }));
            departmentVOS.stream().filter(item ->CollectionUtil.isNotEmpty(item.getUsers())).forEach(e->{
                List<UserVO> users = e.getUsers();
                //创建新的users
                e.setUsers(users.stream().map(userVO -> hasUserMap.getOrDefault(userVO.getId(), userVO)).toList());
            });
//            redisTemplate.opsForHash().delete(RedisConstant.DATASCOPE)
        }
        roleVO.setUserVOS(TreeUtil.buildTree(departmentVOS, DepartmentVO::getPid, DepartmentVO::getSortNo));
        return roleVO;
    }

    @Override
    public RoleVO getMenu(RoleDTO roleInfoDTO) {
        RoleVO roleVO = new RoleVO();
        Long roleId = roleInfoDTO.getId();
        Long tenantId = roleInfoDTO.getTenantId();
        Role role = roleMapper.getRoleById(roleId, tenantId);
        if(ObjectUtil.isNull(role)){
            throw new ServiceException("你查找的角色不存在");
        }
        BeanUtils.copyProperties(role, roleVO);
        //查询所有的菜单
        List<MenuVO> allMenus = roleMapper.listMenu();
        //查询已有的菜单
        List<MenuVO> menuList = roleMapper.selectByRoleId(roleId,tenantId);
        if(!CollectionUtil.isEmpty(menuList)){
            // 使用Stream API将menuList转换为Map，key为菜单ID，value为菜单对象
            Map<Long, MenuVO> menuMap = menuList.stream()
                    .collect(Collectors.toMap(MenuVO::getId, menu -> {
                        menu.setHasMenu(1); // 设置拥有的菜单标识
                        return menu;
                    }));
            // 遍历所有菜单，如果菜单在menuMap中存在，则表示拥有，带有标识
            allMenus = allMenus.stream().map(menu -> menuMap.getOrDefault(menu.getId(), menu))
                    .collect(Collectors.toList());
        }
        //获取当前用户是否是超级管理员
        User user = BaseContext.getCurrentUser();
        //如果是普通用户
        if (!mangerProperties.getUserId().contains(user.getId())){
            allMenus = allMenus.stream().filter(e-> e.getVisible() == 1).collect(Collectors.toList());
        };
        roleVO.setMenuVOS(TreeUtil.buildTree(allMenus, MenuVO::getMenuPid, MenuVO::getSortNo));
        return roleVO;
    }

    public RoleVO getDept(RoleDTO roleDTO){
        RoleVO roleVO = new RoleVO();
        Long roleId = roleDTO.getId();
        Long tenantId = roleDTO.getTenantId();
        Role role = roleMapper.getRoleById(roleId, tenantId);
        if(ObjectUtil.isNull(role)){
            throw new ServiceException("你查找的角色不存在");
        }
        BeanUtils.copyProperties(role, roleVO);
        List<DepartmentVO> listAll = roleMapper.listDepartment(tenantId);

        List<DepartmentVO> departmentVOS = roleMapper.selectDeptByRoleId(roleId, tenantId);
        if(!CollectionUtil.isEmpty(departmentVOS)){
            Map<Long, DepartmentVO> deptMap = departmentVOS.stream()
                    .collect(Collectors.toMap(DepartmentVO::getId, departmentVO -> {
                        departmentVO.setHasDep(1);
                        return departmentVO;
                    }));
            listAll = listAll.stream().map(dept -> deptMap.getOrDefault(dept.getId(), dept))
                    .collect(Collectors.toList());
        }
        roleVO.setDeptVOS(TreeUtil.buildTree(listAll, DepartmentVO::getPid, DepartmentVO::getSortNo));
        return roleVO;
    }
}

package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.role.*;
import com.yitai.entity.MenuRole;
import com.yitai.entity.Role;
import com.yitai.entity.UserRole;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.RoleMapper;
import com.yitai.result.PageResult;
import com.yitai.service.RoleService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.MenuVO;
import com.yitai.vo.RoleVO;
import com.yitai.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    public PageResult pageQuery(RolePageQueryDTO pageQueryDTO) {
        try {
            PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
            Page<Role> page = roleMapper.pageQuery(pageQueryDTO);
            long total = page.getTotal();
            List<Role> records = page.getResult();
            return new PageResult(total,records);
        }catch (Exception e){
            throw new ServiceException("请携带正确的租户id参数");
        }
    }

    @Override
    public void save(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        int record = roleMapper.save(role, roleDTO.getTenantId());
        Long roleId = role.getId();
        List<MenuRole> menuRoles = new ArrayList<>();
        for (Long menuId : roleDTO.getMenuIds()) {
//            MenuRole menuRole = new MenuRole();
            menuRoles.add(MenuRole.builder().menuId(menuId).roleId(roleId).build());
        }
        roleMapper.assMenu(menuRoles, roleDTO.getTenantId());
    }

    @Override
    public void delete(DeleteRoleDTO deleteRoleDTO) {
        // 查询是否存在该角色
        if(ObjectUtil.isNull(roleMapper.getRoleById(deleteRoleDTO.getRoleId(), deleteRoleDTO.getTenantId()))){
            throw new ServiceException("请输入正确的角色");
        }
        // 查询角色是否关联用户
        List<UserVO> userList = roleMapper.selectUserByRoleId(deleteRoleDTO.getRoleId(), deleteRoleDTO.getTenantId());
        if(!CollectionUtil.isEmpty(userList)){
            throw new ServiceException("当前角色关联相关用户");
        }
        //查询角色是否关联菜单
        List<MenuVO> menuList = roleMapper.selectByRoleId(deleteRoleDTO.getRoleId(), deleteRoleDTO.getTenantId());
        if(!CollectionUtil.isEmpty(menuList)){
            throw new ServiceException("当前角色关联相关菜单");
        }
        roleMapper.deleteById(deleteRoleDTO);
    }

    @Override
    public void update(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        roleMapper.update(role, roleDTO.getTenantId());
        if(ObjectUtil.isNull(roleMapper.getRoleById(roleDTO.getId(),roleDTO.getTenantId()))){
            throw new ServiceException("请输入正确的角色");
        }
        //删除角色原有的菜单
        roleMapper.deleteMenuById(roleDTO.getId(), roleDTO.getTenantId());
        //重新分配新的菜单权限
        List<MenuRole> menuRoles = new ArrayList<>();
        for (Long menuId : roleDTO.getMenuIds()) {
//            MenuRole menuRole = new MenuRole();
            menuRoles.add(MenuRole.builder().menuId(menuId).roleId(roleDTO.getId()).build());
        }
        roleMapper.assMenu(menuRoles, roleDTO.getTenantId());
    }

    /**
     * 给角色分配菜单
     */
    @Override
    public void assMenu(RoleMenuDTO roleMenuDTO) {
        List<MenuRole> menuRoles = new ArrayList<>();
        Long roleId = roleMenuDTO.getRoleId();
        for (Long menuId : roleMenuDTO.getMenuIds()) {
//            MenuRole menuRole = new MenuRole();
            menuRoles.add(MenuRole.builder().menuId(menuId).roleId(roleId).build());
        }
        roleMapper.assMenu(menuRoles,roleMenuDTO.getTenantId());
    }

    @Override
    public RoleVO getRoleById(RoleInfoDTO roleInfoDTO) {
        RoleVO roleVO = new RoleVO();
        Long roleId = roleInfoDTO.getRoleId();
        Long tenantId = roleInfoDTO.getTenantId();
        Role role = roleMapper.getRoleById(roleId, tenantId);
        if(ObjectUtil.isNull(role)){
            throw new ServiceException("你查找的角色不存在");
        }
        BeanUtils.copyProperties(role, roleVO);
        //查询已经有的菜单
        List<MenuVO> menuList = roleMapper.selectByRoleId(roleId,tenantId);
        menuList.forEach(menuVO -> menuVO.setHasMenu(1));
        //查询所有的菜单
        List<MenuVO> allMenus = roleMapper.listMenu();

        // 使用Stream API将menuList转换为Map，key为菜单ID，value为菜单对象
        Map<Long, MenuVO> menuMap = menuList.stream()
                .collect(Collectors.toMap(MenuVO::getId, menu -> {
                    menu.setHasMenu(1); // 设置拥有的菜单标识
                    return menu;
                }));
        // 遍历所有菜单，如果菜单在menuMap中存在，则表示拥有，带有标识
        List<MenuVO> menuVOS = allMenus.stream()
                .map(menu -> menuMap.getOrDefault(menu.getId(), menu))
                .toList();

        // 查询角色是否关联用户
        List<UserVO> userVOS = roleMapper.selectUserByRoleId(roleId, tenantId);
        Map<Long, UserVO> userVOMap = userVOS.stream().collect(Collectors.toMap(UserVO::getId, userVO -> {
            userVO.setHasUser(1);
            return userVO;
        }));
        List<UserVO> allUsers = roleMapper.list(tenantId);

        List<UserVO> userVOS1 = allUsers.stream().map(userVO ->
                userVOMap.getOrDefault(userVO.getId(), userVO)).toList();
        roleVO.setMenuVOS(TreeUtil.buildTree(menuVOS, 0 ,MenuVO::getMenuPid));
        roleVO.setUserVOS(userVOS1);
        return roleVO;
    }

    @Override
    public void assUser(RoleUserDTO roleUserDTO) {
        List<UserRole> userRoles = new ArrayList<>();
        Long roleId = roleUserDTO.getRoleId();
        for (Long userId : roleUserDTO.getUserIds()) {
//            MenuRole menuRole = new MenuRole();
            userRoles.add(UserRole.builder().userId(userId).roleId(roleId).build());
        }
        roleMapper.assUser(userRoles,roleUserDTO.getTenantId());
    }
}

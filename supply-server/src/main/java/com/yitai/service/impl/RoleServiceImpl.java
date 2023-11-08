package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.sys.*;
import com.yitai.entity.*;
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
        List<User> userList = roleMapper.selectUserByRoleId(deleteRoleDTO.getRoleId(), deleteRoleDTO.getTenantId());
        if(!CollectionUtil.isEmpty(userList)){
            throw new ServiceException("当前角色关联相关用户");
        }
        //查询角色是否关联菜单
        List<Menu> menuList = roleMapper.selectByRoleId(deleteRoleDTO.getRoleId(), deleteRoleDTO.getTenantId());
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
    public RoleVO getRoleById(Long roleId, Long tenantId) {
        RoleVO roleVO = new RoleVO();
        Role role = roleMapper.getRoleById(roleId,tenantId);
        if(ObjectUtil.isNull(role)){
            throw new ServiceException("你查找的角色不存在");
        }
        BeanUtils.copyProperties(role, roleVO);
        List<Menu> menuList = roleMapper.selectByRoleId(roleId,tenantId);
        List<MenuVO> menuVOS = menuList.stream().map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
        // 查询角色是否关联用户
        List<User> userList = roleMapper.selectUserByRoleId(roleId, tenantId);
        List<UserVO> userVOS = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).toList();
        roleVO.setMenuVOS(TreeUtil.buildTree(menuVOS, 0 ,MenuVO::getMenuPid));
        roleVO.setUserVOS(userVOS);
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

package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.sys.RoleDTO;
import com.yitai.dto.sys.RoleMenuDTO;
import com.yitai.dto.sys.RolePageQueryDTO;
import com.yitai.entity.MenuRole;
import com.yitai.entity.Role;
import com.yitai.mapper.RoleMapper;
import com.yitai.result.PageResult;
import com.yitai.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        PageHelper.startPage(pageQueryDTO.getPage(),pageQueryDTO.getPageSize());
        Page<Role> page = roleMapper.pageQuery(pageQueryDTO);
        long total = page.getTotal();
        List<Role> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void save(RoleDTO addRoleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(addRoleDTO, role);
        roleMapper.save(role);
    }

    @Override
    public void delete(Integer roleId) {
        roleMapper.deleteById(roleId);
    }

    @Override
    public void update(RoleDTO updateRoleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(updateRoleDTO, role);
        roleMapper.update(role);
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
        roleMapper.assMenu(menuRoles);
    }
}

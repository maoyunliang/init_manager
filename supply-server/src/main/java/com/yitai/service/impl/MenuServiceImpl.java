package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.MenuDTO;
import com.yitai.dto.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.entity.MenuRole;
import com.yitai.mapper.MenuMapper;
import com.yitai.result.PageResult;
import com.yitai.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public PageResult pageQuery(MenuPageQueryDTO menuPageQueryDTO) {
        PageHelper.startPage(menuPageQueryDTO.getPage(),menuPageQueryDTO.getPageSize());
        Page<Menu> page = menuMapper.pageQuery(menuPageQueryDTO);
        long total = page.getTotal();
        List<Menu> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void save(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        //创建菜单
        menuMapper.save(menu);
        MenuRole menuRole = MenuRole.builder().roleId(888L).MenuId(menu.getId()).build();
        //赋予管理员权限
        menuMapper.givePermit(menuRole);
    }

    @Override
    public void delete(Integer menuId) {
        menuMapper.deleteById(menuId);
    }

    @Override
    public void update(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menuMapper.update(menu);
    }
}

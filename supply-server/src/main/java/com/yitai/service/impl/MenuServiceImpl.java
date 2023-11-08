package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.sys.MenuDTO;
import com.yitai.dto.sys.MenuListDTO;
import com.yitai.dto.sys.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.mapper.MenuMapper;
import com.yitai.result.PageResult;
import com.yitai.service.MenuService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.MenuVO;
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

    /*
     * 菜单分页查询
     */
    @Override
    public PageResult pageQuery(MenuPageQueryDTO menuPageQueryDTO) {
        PageHelper.startPage(menuPageQueryDTO.getPage(),menuPageQueryDTO.getPageSize());
        Page<Menu> page = menuMapper.pageQuery(menuPageQueryDTO);
        long total = page.getTotal();
        List<Menu> records = page.getResult();
        return new PageResult(total, records);
    }
    /*
     * 菜单列表
     */
    @Override
    public List<MenuVO> list(MenuListDTO menuListDTO) {
        List<MenuVO> menuList = menuMapper.list(menuListDTO);
        return TreeUtil.buildTree(menuList, MenuVO::getMenuPid);
    }

    /**
     * 新建菜单
     */
    @Override
    public void save(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        //返回记录行数
        int records = menuMapper.save(menu);
        //每次新建菜单给是否要同时给租户管理员增加权限后期可以考虑
//        MenuRole menuRole = MenuRole.builder().roleId(888L).menuId(menu.getId()).build();
//        // 插入到系统管理员
//        menuMapper.givePermit(menuRole);
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

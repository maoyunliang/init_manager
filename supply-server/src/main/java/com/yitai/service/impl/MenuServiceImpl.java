package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.dto.menu.DeleteMenuDTO;
import com.yitai.dto.menu.MenuDTO;
import com.yitai.dto.menu.MenuListDTO;
import com.yitai.dto.menu.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.entity.User;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.MenuMapper;
import com.yitai.result.PageResult;
import com.yitai.service.MenuService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

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
        User user = BaseContext.getCurrentUser();
        //返回记录行数
        int records = menuMapper.save(menu);
        //每次新增菜单删除缓存
        if(menu.getMenuType().equals("B")){
            redisTemplate.delete(RedisConstant.USER_PERMISSION.concat(user.getId().toString()));
        }
        //每次新建菜单给是否要同时给租户管理员增加权限后期可以考虑
//        MenuRole menuRole = MenuRole.builder().roleId(888L).menuId(menu.getId()).build();
//        // 插入到系统管理员
//        menuMapper.givePermit(menuRole);
    }

    @Override
    public void delete(DeleteMenuDTO menuDTO) {
        //判断是否有子菜单
        List<Menu> menus = menuMapper.containChildren(menuDTO.getMenuId());
        if(!CollectionUtil.isEmpty(menus)){
            throw new ServiceException("当前菜单存在子菜单，无法删除");
        }
        menuMapper.deleteById(menuDTO.getMenuId());
    }

    @Override
    public void update(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menuMapper.update(menu);
    }


}

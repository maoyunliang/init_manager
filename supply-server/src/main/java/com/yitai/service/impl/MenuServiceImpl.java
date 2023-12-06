package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.yitai.admin.dto.menu.MenuDTO;
import com.yitai.admin.entity.Menu;
import com.yitai.admin.entity.User;
import com.yitai.admin.vo.MenuVO;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.MenuMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
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
@Slf4j
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    MangerProperties mangerProperties;
    /*
     * 菜单分页查询
     */
//    @Override
//    public PageResult pageQuery(MenuPageQueryDTO menuPageQueryDTO) {
//        PageHelper.startPage(menuPageQueryDTO.getPage(),menuPageQueryDTO.getPageSize());
//        Page<Menu> page = menuMapper.pageQuery(menuPageQueryDTO);
//        long total = page.getTotal();
//        List<Menu> records = page.getResult();
//        return new PageResult(total, records);
//    }
    /*
     * 菜单列表
     */
    @Override
    public List<MenuVO> list(MenuDTO menuListDTO) {
        List<MenuVO> menuList = menuMapper.list(menuListDTO);
        //获取当前用户是否是超级管理员
        User user = BaseContext.getCurrentUser();
        //如果是普通用户
        if (!mangerProperties.getUserId().contains(user.getId())){
            menuList = menuList.stream().filter(e-> e.getVisible() == 1).collect(Collectors.toList());
        };

        return menuList;
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
        //每次新增菜单（超级管理员） 删除缓存
        if(menu.getMenuType().equals("B")){
            redisTemplate.delete(RedisConstant.USER_PERMISSION
                    .concat(user.getId().toString()));
        }
    }

    @Override
    public void delete(MenuDTO menuDTO) {
        //判断是否有子菜单
        List<Menu> menus = menuMapper.containChildren(menuDTO.getId());
        if(!CollectionUtil.isEmpty(menus)){
            throw new ServiceException("当前菜单存在子菜单，无法删除");
        }
        menuMapper.deleteById(menuDTO.getId());
    }

    @Override
    public void update(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menuMapper.update(menu);
    }

    public void cleanMenu(){
        log.info("定时清除任务开始执行：{}", new Date());
        menuMapper.delete();
    }
}

package com.yitai.service;

import com.yitai.dto.menu.MenuDTO;
import com.yitai.vo.MenuVO;

import java.util.List;

/**
 * ClassName: RoleService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:40
 * @Version: 1.0
 */
public interface MenuService {
//    PageResult pageQuery(MenuPageQueryDTO menuPageQueryDTO);

    /*
     * 菜单列表
     */
    List<MenuVO> list(MenuDTO menuListDTO);

    void save(MenuDTO menuDTO);

    void delete(MenuDTO menuDTO);

    void update(MenuDTO menuDTO);
}

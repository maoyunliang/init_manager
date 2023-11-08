package com.yitai.service;

import com.yitai.dto.sys.MenuDTO;
import com.yitai.dto.sys.MenuListDTO;
import com.yitai.dto.sys.MenuPageQueryDTO;
import com.yitai.result.PageResult;
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
    PageResult pageQuery(MenuPageQueryDTO menuPageQueryDTO);

    /*
     * 菜单列表
     */
    List<MenuVO> list(MenuListDTO menuListDTO);

    void save(MenuDTO menuDTOList);

    void delete(Integer roleId);

    void update(MenuDTO menuDTO);
}

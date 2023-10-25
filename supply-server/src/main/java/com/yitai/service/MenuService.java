package com.yitai.service;

import com.yitai.dto.sys.MenuDTO;
import com.yitai.dto.sys.MenuPageQueryDTO;
import com.yitai.result.PageResult;

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

    void save(MenuDTO menuDTOList);

    void delete(Integer roleId);

    void update(MenuDTO menuDTO);

}

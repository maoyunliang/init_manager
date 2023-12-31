package com.yitai.service;

import com.yitai.dto.sys.RoleDTO;
import com.yitai.dto.sys.RoleMenuDTO;
import com.yitai.dto.sys.RolePageQueryDTO;
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
public interface RoleService {
    PageResult pageQuery(RolePageQueryDTO pageQueryDTO);

    void save(RoleDTO addRoleDTO);

    void delete(Integer roleId);

    void update(RoleDTO updateRoleDTO);

    void assMenu(RoleMenuDTO roleMenuDTO);
}

package com.yitai.service;

import com.yitai.dto.role.*;
import com.yitai.result.PageResult;
import com.yitai.vo.RoleVO;

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

    void delete(DeleteRoleDTO deleteRoleDTO);

    void update(RoleDTO updateRoleDTO);

    void assMenu(RoleMenuDTO roleMenuDTO);

//    List<Menu> selectByRoleId(Long roleId, Long tenantId);

    RoleVO getRoleById(RoleInfoDTO roleInfoDTO);

    void assUser(RoleUserDTO roleUserDTO);
}

package com.yitai.service;

import com.yitai.admin.dto.role.RoleAssDTO;
import com.yitai.admin.dto.role.RoleDTO;
import com.yitai.admin.dto.role.RolePageQueryDTO;
import com.yitai.result.PageResult;
import com.yitai.admin.vo.DepartmentVO;
import com.yitai.admin.vo.RoleVO;

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
public interface RoleService {
    PageResult pageQuery(RolePageQueryDTO pageQueryDTO);

    void save(RoleDTO addRoleDTO);

    void delete(RoleDTO deleteRoleDTO);

    void update(RoleDTO updateRoleDTO);

    void assMenu(RoleAssDTO roleMenuDTO);

//    List<Menu> selectByRoleId(Long roleId, Long tenantId);

    RoleVO getUser(RoleDTO roleInfoDTO, List<DepartmentVO> departmentVOS);

    RoleVO getMenu(RoleDTO roleInfoDTO);

    void assUser(RoleAssDTO roleUserDTO);
}

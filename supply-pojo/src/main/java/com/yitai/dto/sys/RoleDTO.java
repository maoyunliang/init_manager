package com.yitai.dto.sys;

import lombok.Data;

/**
 * ClassName: AddRoleDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 11:27
 * @Version: 1.0
 */
@Data
public class RoleDTO {
    private Long id;
    private Long tenantId;
    private String roleName;
    private String roleType;
    private String roleDesc;
}

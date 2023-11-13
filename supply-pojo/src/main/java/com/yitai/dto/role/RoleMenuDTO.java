package com.yitai.dto.role;

import lombok.Data;

import java.util.List;

/**
 * ClassName: RoleMenuDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/24 12:35
 * @Version: 1.0
 */
@Data
public class RoleMenuDTO {
    private Long id;
    private Long tenantId;
    private Long roleId;
    private List<Long> menuIds;
}

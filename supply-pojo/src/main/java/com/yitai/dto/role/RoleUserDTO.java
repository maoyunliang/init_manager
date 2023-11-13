package com.yitai.dto.role;

import lombok.Data;

import java.util.List;

/**
 * ClassName: RoleUserDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 8:52
 * @Version: 1.0
 */
@Data
public class RoleUserDTO {
    private Long id;
    private Long roleId;
    private Long tenantId;
    private List<Long> userIds;
}

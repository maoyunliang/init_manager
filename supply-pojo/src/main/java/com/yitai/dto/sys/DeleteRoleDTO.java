package com.yitai.dto.sys;

import lombok.Data;

/**
 * ClassName: DeleteRoleDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 10:47
 * @Version: 1.0
 */
@Data
public class DeleteRoleDTO {
    private Long roleId;
    private Long tenantId;
}

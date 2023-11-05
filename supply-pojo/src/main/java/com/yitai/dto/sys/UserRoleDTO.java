package com.yitai.dto.sys;

import lombok.Data;

import java.util.List;

/**
 * ClassName: UserRoleDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/24 11:24
 * @Version: 1.0
 */
@Data
public class UserRoleDTO {
    private Long id;
    private Long userId;
    private Long tenantId;
    private List<Long> roleIds;
}

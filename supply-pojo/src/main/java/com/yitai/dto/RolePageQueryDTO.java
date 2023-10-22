package com.yitai.dto;

import lombok.Data;

/**
 * ClassName: RolePageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:38
 * @Version: 1.0
 */
@Data
public class RolePageQueryDTO {
    private String roleName;
    private String roleType;
    private int page =1 ;
    private int pageSize =10;
}

package com.yitai.admin.dto.tenant;

import lombok.Data;

/**
 * ClassName: TenantDTO
 * Package: com.yitai.dto.tenant
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:10
 * @Version: 1.0
 */
@Data
public class TenantDTO {
    private Long id;
    private String tenantName;
    private String contact;
    private String location;
    private String address;
    private String qualification;
    private String tenantLogo;
    private String remark;
    private Integer status;
}

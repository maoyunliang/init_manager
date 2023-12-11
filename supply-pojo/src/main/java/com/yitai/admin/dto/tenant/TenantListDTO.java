package com.yitai.admin.dto.tenant;

import com.yitai.base.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: TenantListDTO
 * Package: com.yitai.dto.tenant
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:14
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantListDTO extends BaseBody {
    private Long id;
    private String tenantName;
    private String contact;
    private Integer status;
    private int page = 1;
    private int pageSize = 10;
}

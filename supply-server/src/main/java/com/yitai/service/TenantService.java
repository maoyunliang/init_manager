package com.yitai.service;

import com.yitai.dto.tenant.TenantDTO;
import com.yitai.dto.tenant.TenantListDTO;
import com.yitai.result.PageResult;

/**
 * ClassName: TenantService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:08
 * @Version: 1.0
 */
public interface TenantService {
    PageResult page(TenantListDTO tenantListDTO);

    void save(TenantDTO tenantDTO);

    void update(TenantDTO tenantDTO);

    void delete(Long tenantId);
}

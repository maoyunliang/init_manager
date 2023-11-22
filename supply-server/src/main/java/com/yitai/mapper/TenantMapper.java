package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.dto.tenant.TenantListDTO;
import com.yitai.entity.Tenant;
import com.yitai.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: TenantMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:09
 * @Version: 1.0
 */
@Mapper
public interface TenantMapper {
    Page<Tenant> pageQuery(TenantListDTO tenantListDTO);

    @AutoFill(value = OperationType.INSERT)
    void save(Tenant tenant);

    @AutoFill(value = OperationType.UPDATE)
    void update(Tenant tenant);

    void delete(Long tenantId);
}

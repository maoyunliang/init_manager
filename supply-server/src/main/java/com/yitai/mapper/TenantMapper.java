package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.admin.AutoFill;
import com.yitai.annotation.admin.TableShard;
import com.yitai.admin.dto.tenant.TenantListDTO;
import com.yitai.admin.entity.Department;
import com.yitai.admin.entity.Tenant;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    int save(Tenant tenant);

    @AutoFill(value = OperationType.UPDATE)
    void update(Tenant tenant);

    void delete(Long tenantId);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    void insertDept(@Param("department") Department department, @Param("tenantId") Long tenantId);
}

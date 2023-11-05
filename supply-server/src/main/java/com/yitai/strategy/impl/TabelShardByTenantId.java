package com.yitai.strategy.impl;

import com.yitai.strategy.TableShardStrategy;

/**
 * ClassName: TabelShardByTenantId
 * Package: com.yitai.strategy.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 14:27
 * @Version: 1.0
 */
public class TabelShardByTenantId implements TableShardStrategy {
    @Override
    public String getTableShardName(String tableName, Long tenantId) {
        return tableName + "_" + tenantId;
    }
}

package com.yitai.strategy;

/**
 * ClassName: TableShardStrategy
 * Package: com.yitai.interface1
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 14:25
 * @Version: 1.0
 */
public interface TableShardStrategy {
    // 获取表名
    String getTableShardName(String tableName, Long tenantId);
}

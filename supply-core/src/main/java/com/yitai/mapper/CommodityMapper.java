package com.yitai.mapper;

import com.yitai.annotation.admin.TableShard;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CommodityReq;
import com.yitai.annotation.admin.AutoFill;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommodityMapper {
    @TableShard(type = ShardType.TABLE)
    List<CommodityDTO> list(CommodityReq req);

    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.INSERT)
    int save(@Param("entity") Commodity commodity);
    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.UPDATE)
    int update(@Param("entity") CommodityReq req);
    @TableShard(type = ShardType.TABLE)
    void removeByIds(List<Long> ids);
}

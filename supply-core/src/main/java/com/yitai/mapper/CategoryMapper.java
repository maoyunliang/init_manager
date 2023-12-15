package com.yitai.mapper;

import com.yitai.annotation.admin.AutoFill;
import com.yitai.annotation.admin.TableShard;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CategoryReq;
import com.yitai.core.req.CommodityReq;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @TableShard(type = ShardType.TABLE)
    List<Category> list(CategoryReq req);

    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.INSERT)
    int save(@Param("entity") Category category);
    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.UPDATE)
    int update(@Param("entity") Category req);

    @TableShard(type = ShardType.TABLE)
    void deleteById(Long ids);
}

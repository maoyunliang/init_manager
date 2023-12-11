package com.yitai.mapper;

import com.yitai.annotation.admin.AutoFill;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CommodityReq;
import com.yitai.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> list(@Param("req") Category req);

    @AutoFill(value = OperationType.INSERT)
    int save(@Param("entity") Category category);

    @AutoFill(value = OperationType.UPDATE)
    int update(@Param("entity") Category req);

    void deleteById(Long ids);
}

package com.yitai.mapper;

import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CommodityReq;
import com.yitai.annotation.admin.AutoFill;
import com.yitai.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommodityMapper {
    List<CommodityDTO> list(@Param("req") CommodityReq req);

    @AutoFill(value = OperationType.INSERT)
    int save(@Param("entity") Commodity commodity);

    @AutoFill(value = OperationType.UPDATE)
    int update(@Param("entity") CommodityReq req);

    void removeByIds(List<Long> ids);
}

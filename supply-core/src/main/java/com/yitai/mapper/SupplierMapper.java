package com.yitai.mapper;

import com.yitai.annotation.admin.AutoFill;
import com.yitai.core.entity.Supplier;
import com.yitai.core.req.SupplierReq;
import com.yitai.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SupplierMapper {

    List<Supplier> list(SupplierReq req);

    @AutoFill(value = OperationType.INSERT)
    int save(@Param("entity") Supplier supplier);

    @AutoFill(value = OperationType.UPDATE)
    int update(@Param("entity") Supplier supplier);

    void removeById(Long id);
}

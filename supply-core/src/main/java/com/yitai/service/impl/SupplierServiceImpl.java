package com.yitai.service.impl;


import com.yitai.core.entity.Supplier;
import com.yitai.core.req.SupplierReq;
import com.yitai.mapper.SupplierMapper;
import com.yitai.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierMapper supplierMapper;


    @Override
    public List<Supplier> list(SupplierReq req) {
        return supplierMapper.list(req);
    }

    @Override
    public void remove(Long id) {
        supplierMapper.removeById(id);
    }

    @Override
    public void save(Supplier supplier) {
        supplierMapper.save(supplier);
    }
}

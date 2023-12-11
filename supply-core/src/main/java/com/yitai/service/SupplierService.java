package com.yitai.service;

import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Supplier;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.SupplierReq;

import java.util.List;

/**
 * ClassName: JobService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:30
 * @Version: 1.0
 */
public interface SupplierService {
    List<Supplier> list(SupplierReq req);

    void remove(Long id);

    void save(Supplier supplier);
}

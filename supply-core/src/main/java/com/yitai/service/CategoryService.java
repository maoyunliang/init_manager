package com.yitai.service;

import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.req.CategoryReq;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;

import java.util.List;

public interface CategoryService {
    List<Category> list(CategoryReq req);

    void removeBatchIds(List<Long> ids);

    void save(CategoryReq entity);

    void batchSave(List<Category> list);

}

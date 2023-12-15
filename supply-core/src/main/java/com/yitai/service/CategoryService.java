package com.yitai.service;

import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;

import java.util.List;

public interface CategoryService {
    List<Category> list(Category req);

    void removeBatchIds(List<Long> ids);

    void save(Category entity);

    void batchSave(List<Category> list);

}

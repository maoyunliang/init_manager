package com.yitai.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Category;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CategoryReq;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.CategoryMapper;
import com.yitai.mapper.CommodityMapper;
import com.yitai.service.CategoryService;
import com.yitai.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Category> list(Category req) {
        return null;
    }

    @Override
    public void removeBatchIds(List<Long> ids) {

    }

    @Override
    public void save(Category entity) {

    }

    @Override
    public void batchSave(List<Category> list) {

    }
}

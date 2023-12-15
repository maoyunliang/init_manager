package com.yitai.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yitai.admin.entity.Role;
import com.yitai.core.entity.Category;
import com.yitai.core.req.CategoryReq;
import com.yitai.mapper.CategoryMapper;
import com.yitai.mapper.CommodityMapper;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.exception.ServiceException;
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
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public PageInfo<CommodityDTO> listByPage(CommodityReq req) {
        PageHelper.startPage(req.getPage(), req.getPageSize());
        Page<CommodityDTO> records = commodityMapper.list(req);
        return new PageInfo(records);
    }

    @Override
    public List<CommodityDTO> list(CommodityReq req) {
        return commodityMapper.list(req);
    }

    @Override
    public void removeBatchIds(List<Long> ids) {
        //验证是否在标期内，是否可以删除
        if (checkInDeal(ids)) {
            throw new ServiceException("货品履行标期中，不能删除");
        }
        commodityMapper.removeByIds(ids);
    }

    @Override
    public void save(CommoditySaveReq req) {
        if (req.getId() == null && CollectionUtil.isEmpty(req.getIds())) {
            Commodity commodity = new Commodity();
            BeanUtil.copyProperties(req, commodity);
            commodityMapper.save(commodity);
        } else {
            commodityMapper.update(req);
        }
    }

    @Override
    public void batchSave(List<CommoditySaveReq> list) {
        //把excel中类目名称解析出类目id
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        Long tenantId = list.get(0).getTenantId();
        List<Category> categories = Lists.newArrayList();
        List<CommoditySaveReq> validList = list.stream().filter(e -> StringUtils.isNotEmpty(e.getCommodityNo()) && StringUtils.isNotEmpty(e.getCommodityName())
                && StringUtils.isNotEmpty(e.getUnit())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(validList)) {
            List<String> categoryNames = validList.stream().map(CommoditySaveReq::getCategoryName).collect(Collectors.toList());
            CategoryReq req = new CategoryReq();
            req.setCategoryNames(categoryNames);
            req.setTenantId(tenantId);
            categories = categoryMapper.list(req);
        }

        if (CollectionUtil.isEmpty(categories)) {
            return;
        }
        Map<String, Long> categoryNameMap = categories.stream().collect(Collectors.toMap(Category::getCategoryName, Category::getId));

        list.stream().forEach(e -> {
            e.setCategoryId(categoryNameMap.get(e.getCategoryName()));
            e.setStatus(1);
        });

        List<Commodity> saveList = BeanUtil.copyToList(list, Commodity.class);
        commodityMapper.batchSave(saveList, tenantId);
    }

    private boolean checkInDeal(List<Long> ids) {
        return false;
    }
}

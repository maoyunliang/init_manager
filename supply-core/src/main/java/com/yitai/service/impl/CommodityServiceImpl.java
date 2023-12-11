package com.yitai.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.entity.Commodity;
import com.yitai.core.req.CommodityReq;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.mapper.CommodityMapper;
import com.yitai.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommodityServiceImpl implements CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;


    @Override
    public List<CommodityDTO> list(CommodityReq req) {
        return commodityMapper.list(req);
    }

    @Override
    public void removeBatchIds(List<Long> ids) {
        commodityMapper.removeByIds(ids);
    }

    @Override
    public void save(CommoditySaveReq req) {
        Commodity commodity = new Commodity();
        BeanUtil.copyProperties(commodity,commodity);
        commodityMapper.save(commodity);
    }
}

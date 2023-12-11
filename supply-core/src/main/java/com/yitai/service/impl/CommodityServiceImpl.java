package com.yitai.service.impl;


import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import com.yitai.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: JobServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:30
 * @Version: 1.0
 */
@Slf4j
@Service
public class CommodityServiceImpl implements CommodityService {
//    @Autowired
//    private CommodityMapper commodityMapper;


    @Override
    public List<SysJob> list(JobDTO jobDTO) {
        return null;
    }

    @Override
    public void removeBatchIds(List<Integer> ids) {
//        commodityMapper.removeBatchIds(ids);
    }
}

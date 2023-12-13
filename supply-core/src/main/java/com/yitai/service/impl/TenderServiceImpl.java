package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.core.dto.TenderDTO;
import com.yitai.core.vo.TenderVO;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.TenderMapper;
import com.yitai.result.PageResult;
import com.yitai.service.TenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: TenderServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 11:56
 * @Version: 1.0
 */
@Service
@Slf4j
public class TenderServiceImpl implements TenderService {
    @Autowired
    TenderMapper tenderMapper;

    @Override
    public PageResult page(TenderDTO tenderDTO) {
        if (tenderDTO.getTenantId() == 0){
            throw new ServiceException("请输入正确的租户id");
        }
        PageHelper.startPage(tenderDTO.getPage(), tenderDTO.getPageSize());
        Page<TenderVO> page = tenderMapper.page(tenderDTO);
        long total = page.getTotal();
        List<TenderVO> records = page.getResult();
        records = records.stream().peek(t->{
            String beginTime = t.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss"));
            String endTime = t.getEndTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:ss"));
            t.setValidityTime(beginTime+"至"+endTime);
            //设置报价的供应商
            t.setSuppliersNums(t.getSuppliers().size());
            //已报价的数量
            long quotedNums = t.getSuppliers().entrySet().stream().filter(e -> e.getValue() == 1).count();
            t.setQuotedNums(Integer.parseInt(String.valueOf(quotedNums)));
        }).collect(Collectors.toList());
        return new PageResult(total, records);
    }

    @Override
    public List<TenderVO> list(Long tenantId, List<Long> idList) {
        return tenderMapper.list(tenantId, idList);
    }
}

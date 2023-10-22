package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.LogPageQueryDTO;
import com.yitai.entity.Logs;
import com.yitai.mapper.LogMapper;
import com.yitai.result.PageResult;
import com.yitai.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: LogServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 14:11
 * @Version: 1.0
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    LogMapper logMapper;

    @Override
    public PageResult pageQuery(LogPageQueryDTO logPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(logPageQueryDTO.getPage(), logPageQueryDTO.getPageSize());
        Page<Logs> page = logMapper.pageQuery(logPageQueryDTO);
        long total = page.getTotal();
        List<Logs> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void removeById(Integer id) {
        logMapper.removeById(id);
    }

    @Override
    public void removeBatchIds(List<Integer> ids) {
        logMapper.removeBatchIds(ids);
    }

    @Override
    public void save(Logs logs) {
        logMapper.save(logs);
    }
}

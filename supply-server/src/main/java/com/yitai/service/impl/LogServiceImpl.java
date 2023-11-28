package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.mapper.LogMapper;
import com.yitai.dto.LogPageQueryDTO;
import com.yitai.entity.LoginLogs;
import com.yitai.entity.OperationLog;
import com.yitai.exception.ServiceException;
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
        if (logPageQueryDTO.getTenantId() == 0){
            throw new ServiceException("请输入正确的租户id");
        }
        //开始分页查询
        PageHelper.startPage(logPageQueryDTO.getPage(), logPageQueryDTO.getPageSize());
        Page<OperationLog> page = logMapper.pageQuery(logPageQueryDTO);
        long total = page.getTotal();
        List<OperationLog> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void removeBatchIds(List<Integer> ids) {
        logMapper.removeBatchIds(ids);
    }

    @Override
    public void save2(OperationLog logs) {
        logMapper.save2(logs);
    }

    @Override
    public List<OperationLog> list(Long tenantId) {
        return logMapper.list(tenantId);
    }

    @Override
    public void save1(LoginLogs logs) {
        logMapper.save1(logs);
    }


    @Override
    public PageResult pageQuery1(LogPageQueryDTO logPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(logPageQueryDTO.getPage(), logPageQueryDTO.getPageSize());
        Page<LoginLogs> page = logMapper.pageQuery1(logPageQueryDTO);
        long total = page.getTotal();
        List<LoginLogs> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    public void removeBatchIds1(List<Integer> ids) {
        logMapper.removeBatchIds1(ids);
    }
}

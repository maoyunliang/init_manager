package com.yitai.service.impl;

import com.yitai.exception.ServiceException;
import com.yitai.mapper.JobLogMapper;
import com.yitai.quartz.entity.SysJobLog;
import com.yitai.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: JobLogServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 19:30
 * @Version 1.0
 */
@Service
public class JobLogServiceImpl implements JobLogService {
    @Autowired
    JobLogMapper jobLogMapper;
    @Override
    public void save(SysJobLog sysJobLog) {
        int record = jobLogMapper.save(sysJobLog);
        if(record < 1){
            throw new ServiceException("插入异常");
        }
    }
}

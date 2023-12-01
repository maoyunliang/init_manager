package com.yitai.service.impl;


import com.github.pagehelper.PageHelper;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.JobMapper;
import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import com.yitai.service.JobService;
import com.yitai.utils.CronUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;
    @Override
    public List<SysJob> list(JobDTO jobDTO) {
        //开始分页查询
        PageHelper.startPage(jobDTO.getPage(), jobDTO.getPageSize());
        return jobMapper.page(jobDTO);
    }

    @Override
    public void save(JobDTO jobDTO) {
        String cronExpression = jobDTO.getCronExpression();
        String jobName = jobDTO.getJobName();
        if(!CronUtils.isValid(cronExpression)){
            throw new ServiceException("新建任务"+jobName+"：cron表达式不正确");
        }
        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(jobDTO, sysJob);
        int record = jobMapper.save(sysJob, jobDTO.getTenantId());
    }

    @Override
    public void update(JobDTO jobDTO) {
        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(jobDTO, sysJob);
        jobMapper.update(sysJob, jobDTO.getTenantId());
    }
}

package com.yitai.service.impl;


import com.github.pagehelper.PageHelper;
import com.yitai.mapper.JobMapper;
import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import com.yitai.service.JobService;
import com.yitai.utils.ScheduleUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     * 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException {
        log.info("==========定时任务初始化===========");
        scheduler.clear();
        List<SysJob> jobList = jobMapper.listAll();
        for (SysJob job : jobList) {
            ScheduleUtil.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public List<SysJob> list(JobDTO jobDTO) {
        //开始分页查询
        PageHelper.startPage(jobDTO.getPage(), jobDTO.getPageSize());
        return jobMapper.page(jobDTO);
    }

    @Override
    public void save(JobDTO jobDTO) throws SchedulerException {
        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(jobDTO, sysJob);
        int record = jobMapper.save(sysJob);
        if(record > 0){
            ScheduleUtil.createScheduleJob(scheduler, sysJob);
        }
    }

    @Override
    public void update(JobDTO jobDTO) throws SchedulerException {
        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(jobDTO, sysJob);
        int record = jobMapper.update(sysJob);
        if(record > 0){
            ScheduleUtil.createScheduleJob(scheduler, sysJob);
        }
    }

    @Override
    public void removeBatchIds(List<Integer> ids) {
        jobMapper.removeBatchIds(ids);
    }
}

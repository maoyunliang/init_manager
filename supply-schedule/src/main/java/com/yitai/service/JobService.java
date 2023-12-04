package com.yitai.service;

import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * ClassName: JobService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:30
 * @Version: 1.0
 */
public interface JobService {
    List<SysJob> list(JobDTO jobDTO);

    void save(JobDTO jobDTO) throws SchedulerException;

    void update(JobDTO jobDTO) throws SchedulerException;

    void removeBatchIds(List<Integer> ids);
}

package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.exception.ServiceException;
import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import com.yitai.result.Result;
import com.yitai.service.JobService;
import com.yitai.utils.CronUtils;
import com.yitai.utils.ScheduleUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: FileController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 10:02
 * @Version: 1.0
 */

@Tag(name = "定时任务管理接口")
@RequestMapping("admin/job")
@RestController
@Slf4j
public class JobController {

    @Autowired
    private JobService jobService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "定时任务列表")
    @HasPermit(permission = "run:job:list")
    public Result<?> list(@RequestBody JobDTO jobDTO){
        List<SysJob> list = jobService.list(jobDTO);
        return Result.success(list);
    }

    @PostMapping("/update")
    @Operation(summary = "更新定时任务")
    @HasPermit(permission = "run:job:update")
    public Result<?> update(@RequestBody JobDTO jobDTO) throws SchedulerException {
        if(!CronUtils.isValid(jobDTO.getCronExpression())){
            throw new ServiceException("修改任务"+jobDTO.getJobName()+"'失败，cron表达式不正确");
        }else if(!ScheduleUtil.whiteList(jobDTO.getInvokeTarget())){
            throw new ServiceException("修改任务'" + jobDTO.getJobName() + "'失败，目标字符串不在白名单内");
        }
        jobService.update(jobDTO);
        return Result.success();
    }

    @PostMapping("/save")
    @Operation(summary = "添加定时任务")
    @HasPermit(permission = "run:job:add")
    public Result<?> save(@RequestBody JobDTO jobDTO) throws SchedulerException {
        if(!CronUtils.isValid(jobDTO.getCronExpression())){
            throw new ServiceException("新增任务"+jobDTO.getJobName()+"'失败，cron表达式不正确");
        }else if(!ScheduleUtil.whiteList(jobDTO.getInvokeTarget())){
            throw new ServiceException("新增任务'" + jobDTO.getJobName() + "'失败，目标字符串不在白名单内");
        }
        jobService.save(jobDTO);
        return Result.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除定时任务")
    @HasPermit(permission = "run:job:delete")
    public Result<?> delete(@RequestBody List<Integer> ids) {
        jobService.removeBatchIds(ids);
        return Result.success();
    }
}

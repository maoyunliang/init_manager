package com.yitai.task;

import cn.hutool.core.thread.ThreadUtil;
import com.yitai.constant.ScheduleConstant;
import com.yitai.quartz.entity.SysJob;
import com.yitai.quartz.entity.SysJobLog;
import com.yitai.service.JobLogService;
import com.yitai.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ClassName: AbstractQuartzJob
 * Package: com.yitai.task
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 18:23
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<LocalDateTime> TIME = new ThreadLocal<>();
    @Override
    public void execute(JobExecutionContext context) {
        SysJob sysJob = new SysJob();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstant.TASK_PROPERTIES), sysJob);
        try {
            before();
            doExecute(context, sysJob);
            after(sysJob, null);
        }
        catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(sysJob, e);
        }
    }

    /**
     * 执行前
     */
    private void before() {
        TIME.set(LocalDateTime.now());
        log.info("----------开始执行任务---------");
    }

    /**
     * 继承类真实执行方法（需重写）
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;

    /**
     * 执行后日志记录
     */
    private void after(SysJob sysJob, Exception e) {
        LocalDateTime startTime = TIME.get();
        long runMs  = Duration.between(startTime,LocalDateTime.now()).toMillis();
        log.info("定时任务接口请求结束 -> 本次请求耗时"+ runMs);
        SysJobLog sysJobLog = SysJobLog.builder()
                .jobName(sysJob.getJobName()).jobGroup(sysJob.getJobGroup())
                .jobMessage(sysJob.getJobName() + " 总共耗时：" + runMs + "毫秒")
                .invokeTarget(sysJob.getInvokeTarget())
                .startTime(startTime).endTime(LocalDateTime.now())
                .build();
        if (e != null) {
            sysJobLog.setStatus(ScheduleConstant.FAIL);
            String errorMsg = StringUtils.substring(e.getMessage(), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }
        else{
            sysJobLog.setStatus(ScheduleConstant.SUCCESS);
            sysJobLog.setExceptionInfo("null");
        }
        TIME.remove();
        ThreadUtil.execAsync(()->{
            System.out.println(sysJobLog);
            // 写入数据库当中
            SpringUtils.getBean(JobLogService.class).save(sysJobLog);
            }
        );
    }
}

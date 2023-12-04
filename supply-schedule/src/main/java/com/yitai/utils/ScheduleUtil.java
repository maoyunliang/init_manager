package com.yitai.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yitai.constant.ScheduleConstant;
import com.yitai.enumuration.Status;
import com.yitai.exception.ServiceException;
import com.yitai.quartz.entity.SysJob;
import com.yitai.task.QuartzDisallowConcurrentExecution;
import com.yitai.task.QuartzJobExecution;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;

/**
 * ClassName: ScheduleUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/4 9:22
 * @Version: 1.0
 */
public class ScheduleUtil {
    public static void createScheduleJob(Scheduler scheduler, SysJob sysJob) throws SchedulerException {
        Class<? extends Job> jobClass = getQuartzJobClass(sysJob);
        //1.构建job信息
        Long jobId = sysJob.getId();
        String jobGroup = sysJob.getJobGroup();
        JobKey jobKey = getJobKey(jobId, jobGroup);
        TriggerKey triggerKey = getTrigger(jobId, jobGroup);
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
        //2.表达是调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(sysJob, cronScheduleBuilder);

        //3.trigger构建器
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder).build();
        //4. 存入相关属性至JobDataMap
        jobDetail.getJobDataMap().put(ScheduleConstant.TASK_PROPERTIES, sysJob);
        //判断是否存在
        if(scheduler.checkExists(jobKey)){
            //存在即删除再创建
            scheduler.deleteJob(jobKey);
        }
        // 判断任务是否过期
        if (ObjectUtil.isNotEmpty(CronUtils.getNextExecution(sysJob.getCronExpression())))
        {
            // 执行调度任务
            scheduler.scheduleJob(jobDetail, trigger);
        }
        // 暂停任务
        if (sysJob.getStatus().equals(Status.PAUSE.getValue()))
        {
            scheduler.pauseJob(jobKey);
        }
    }



    /**
     * 得到quartz任务类
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob){
        return sysJob.getConcurrent() == 1 ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup)
    {
        return JobKey.jobKey(ScheduleConstant.TASK_CLASS_NAME + jobId, jobGroup);
    }

    private static TriggerKey getTrigger(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstant.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb) {
        return switch (job.getMisfirePolicy()) {
            // 默认
            case ScheduleConstant.MISFIRE_DEFAULT -> cb;
            // 立即触发执行
            case ScheduleConstant.MISFIRE_IGNORE_MISFIRES -> cb.withMisfireHandlingInstructionIgnoreMisfires();
            // 触发一次执行
            case ScheduleConstant.MISFIRE_FIRE_AND_PROCEED -> cb.withMisfireHandlingInstructionFireAndProceed();
            // 不触发立即执行
            case ScheduleConstant.MISFIRE_DO_NOTHING -> cb.withMisfireHandlingInstructionDoNothing();
            default -> throw new ServiceException("任务策略：" + job.getMisfirePolicy()
                    + "无法应用于任务表达式");
        };
    }

    /**
     * 检查包名是否为白名单配置
     *
     * @param invokeTarget 目标字符串
     * @return 结果
     */
    public static boolean whiteList(String invokeTarget) {
        String packageName = StrUtil.subBefore(invokeTarget, "(", false);
        int count = StrUtil.count(packageName, ".");
        if (count > 1) {
            return StrUtil.containsAnyIgnoreCase(invokeTarget, ScheduleConstant.JOB_WHITELIST_STR);
        }
        Object obj = SpringUtils.getBean(StrUtil.split(invokeTarget, ".").get(0));
        String beanPackageName = obj.getClass().getPackage().getName();
        return StringUtils.containsAnyIgnoreCase(beanPackageName, ScheduleConstant.JOB_WHITELIST_STR)
                && !StringUtils.containsAnyIgnoreCase(beanPackageName, ScheduleConstant.JOB_ERROR_STR);
    }
}

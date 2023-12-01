package com.yitai.task;

import org.quartz.*;

/**
 * ClassName: SysTask
 * Package: com.yitai.task
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/1 11:46
 * @Version: 1.0
 */
public class SysTask implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = JobBuilder.newJob(SysTask.class).build();
        System.out.println("任务");
    }
}

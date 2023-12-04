package com.yitai.task;

import com.yitai.quartz.entity.SysJob;
import com.yitai.utils.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * ClassName: QuartzDisallowConcurrentExecution
 * Package: com.yitai.task
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/4 9:19
 * @Version: 1.0
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        //反射类完成执行任务
        JobInvokeUtil.invokeMethod(sysJob);
    }
}

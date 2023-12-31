package com.yitai.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ClassName: MyTask
 * Package: com.yitai.task
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/28 10:47
 * @Version: 1.0
 */
@Component
@Slf4j
public class MyTask {
    @Scheduled(cron = "0 23 17 * * ?")
    public void executeTask(){
        log.info("定时任务开始执行：{}", new Date());
    }
}

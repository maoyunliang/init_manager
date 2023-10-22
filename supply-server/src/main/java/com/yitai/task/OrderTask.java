package com.yitai.task;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName: OrderTask
 * Package: com.yitai.task
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/7 9:29
 * @Version: 1.0
 */

@Component
@Slf4j
public class OrderTask {

    /**
     * 处理超时订单
     */
//    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOrder(){
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 查看订单状态 select * from orders where status = ? and order_time < ?
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理派送中的订单：{}", LocalDateTime.now());

    }
}

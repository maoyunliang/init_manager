package com.yitai.task;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Slf4j
public class RyTask
{
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        log.info("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i);
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        log.info("定时无参构造方法开始执行：{}", new Date());
    }
}

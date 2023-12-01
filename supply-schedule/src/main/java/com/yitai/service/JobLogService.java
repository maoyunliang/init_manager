package com.yitai.service;

import com.yitai.quartz.entity.SysJobLog;

/**
 * ClassName: JobLogService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 19:24
 * @Version 1.0
 */
public interface JobLogService {
    void save(SysJobLog sysJobLog);
}

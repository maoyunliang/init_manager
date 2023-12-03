package com.yitai.utils;

import com.yitai.quartz.entity.SysJob;

/**
 * ClassName: Test
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/3 18:13
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {
        JobInvokeUtil.invokeMethod(SysJob.builder().invokeTarget("com.yitai.service.impl.JobServiceImpl.list(1,'sex',2)").build());
    }
}

package com.yitai.pojo.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: CpuInfo
 * Package: com.yitai.entity.server
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 11:29
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CpuInfo {
    /**
     * 核心数
     */
    private int cpuNum;
    /**
     * CPU系统使用率
     */
    private String sys;

    /**
     * CPU用户使用率
     */
    private String used;

    /**
     * CPU当前等待率
     */
    private String wait;

    /**
     * CPU当前空闲率
     */
    private String free;
}

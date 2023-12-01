package com.yitai.quartz.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: SysJobLog
 * Package: com.yitai.quartz.entity
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 18:56
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysJobLog {
    private Long id;
    private String jobName;
    @Schema(description = "任务组名")
    private String jobGroup;
    @Schema(description = "调用目标字符串")
    private String invokeTarget;
    /** 日志信息 */
    @Schema(description = "日志信息")
    private String jobMessage;
    /** 执行状态（0正常 1失败） */
    @Schema(description = "执行状态 1=正常, -1=失败")
    private Integer status;
    /** 异常信息 */
    @Schema(description = "异常信息")
    private String exceptionInfo;
    /** 开始时间 */
    private LocalDateTime startTime;
    /** 结束时间 */
    private LocalDateTime endTime;
}

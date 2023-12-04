package com.yitai.quartz.dto;

import com.yitai.constant.ScheduleConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ClassName: JobDTO
 * Package: com.yitai.quartz.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 15:00
 * @Version: 1.0
 */
@Data
public class JobDTO {
    private Long id;
    @Schema(description = "任务名")
    private String jobName;
    @Schema(description = "任务组")
    private String jobGroup;
    @Schema(description = "调用目标字符串")
    private String invokeTarget;
    @Schema(description = "调用cron表达式")
    private String cronExpression;
    @Schema(description = "计划策略  0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstant.MISFIRE_DEFAULT;
    @Schema(description = "并发执行 1=允许,-1=禁止")
    private Integer concurrent;
    @Schema(description = "任务状态 1=正常,-1=暂停")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    private int page = 1;
    private int pageSize = 10;
}

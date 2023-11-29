package com.yitai.quartz.entity;

import com.yitai.base.BaseEntity;
import com.yitai.constant.ScheduleConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * ClassName: Job
 * Package: com.yitai.entity.quartz
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:36
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysJob extends BaseEntity {
    private Long id;
    private String jobName;
    @Schema(description = "任务组名")
    private String jobGroup;
    @Schema(description = "调用目标字符串")
    private String invokeTarget;
    @Schema(description = "调用目标字符串")
    private String cronExpression;
    @Schema(description = "计划策略  0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstant.MISFIRE_DEFAULT;
    @Schema(description = "并发执行 1=允许,-1=禁止")
    private Integer concurrent;
    @Schema(description = "任务状态 1=正常,-1=暂停")
    private Integer status;
}

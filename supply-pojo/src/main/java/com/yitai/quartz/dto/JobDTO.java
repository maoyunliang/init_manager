package com.yitai.quartz.dto;

import com.yitai.base.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: JobDTO
 * Package: com.yitai.quartz.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 15:00
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobDTO extends BaseBody {
    private String jobName;
    private String jobGroup;
    private Integer status;
}

package com.yitai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: OperationLog
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 11:27
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {
    private Integer id;
    private String operation;
    private String type;
    private String ip;
    private String user;
    private double duration;
    private String time;
    private Long tenantId;
}

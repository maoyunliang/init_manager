package com.yitai.dto.sys;

import lombok.Data;

/**
 * ClassName: LogPageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 14:55
 * @Version: 1.0
 */
@Data
public class LogPageQueryDTO {
    private String operation;
    private Long tenantId;
    private String user;
    private String ip;
    private int page =1 ;
    private int pageSize =10;
}

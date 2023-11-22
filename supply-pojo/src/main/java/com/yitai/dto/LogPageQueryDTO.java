package com.yitai.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: LogPageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 14:55
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogPageQueryDTO extends BaseBody{
    private String type;
    private String user;
    private String ip;
    private int page =1 ;
    private int pageSize =10;
}

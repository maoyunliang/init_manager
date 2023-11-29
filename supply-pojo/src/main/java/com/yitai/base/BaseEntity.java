package com.yitai.base;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: BaseEntity
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:45
 * @Version: 1.0
 */
@Data
public class BaseEntity {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

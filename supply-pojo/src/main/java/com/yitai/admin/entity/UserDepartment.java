package com.yitai.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: UserDepartment
 * Package: com.yitai.admin.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/7 16:23
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDepartment {
    private Long id;
    private Long userId;
    private Long deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

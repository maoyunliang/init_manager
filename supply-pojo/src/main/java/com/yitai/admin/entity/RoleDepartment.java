package com.yitai.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: RoleDepartment
 * Package: com.yitai.admin.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/7 8:59
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDepartment {
    private Long id;
    private Long roleId;
    private Long deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

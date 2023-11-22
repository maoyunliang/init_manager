package com.yitai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: Role
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:45
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Long id;
    private String roleName;
    @Schema(name = "权限标识")
    private String identity;
    private String roleType;
    private String roleDesc;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

package com.yitai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: Department
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:23
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Long id;
    private String departmentName;
    private String departmentNo;
    private String remark;
    private Long pid;
    private String leader;
    private Long sortNo;
    private Long status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

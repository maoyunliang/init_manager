package com.yitai.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: DepartmentVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:30
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentVO {
    private Long id;
    private String departmentName;
    private Long pid;
    private String remark;
    private String username;
    private Long leader;
    private Long sortNo;
    private Integer status;

    @Schema(description = "角色是否关联部门")
    @Builder.Default
    private Integer HasDep = -1;

    private LocalDateTime createTime;
    private List<DepartmentVO> children;
    private List<UserVO> users;
}

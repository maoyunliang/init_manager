package com.yitai.vo;

import lombok.Data;

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
public class DepartmentVO {
    private Long id;
    private String departmentName;
    private Long pid;
    private String remark;
    private String username;
    private Long leader;
    private Long sortNo;
    private Integer status;
    private LocalDateTime createTime;
    private List<DepartmentVO> children;
    private List<UserVO> users;
}

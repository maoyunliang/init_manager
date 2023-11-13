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
    private String departmentNo;
    private String remark;
    private Long pid;
    private String leader;
    private Long sortNo;
    private Long status;
    private LocalDateTime createTime;
    private List<DepartmentVO> children;
}

package com.yitai.dto.department;

import lombok.Data;

/**
 * ClassName: DepartmentDTO
 * Package: com.yitai.dto.department
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:47
 * @Version: 1.0
 */
@Data
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private String departmentNo;
    private String remark;
    private Long pid;
    private String leader;
    private Long sortNo;
    private Long status;
    private Long tenantId;
}

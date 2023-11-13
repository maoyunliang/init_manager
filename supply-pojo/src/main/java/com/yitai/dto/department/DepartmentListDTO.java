package com.yitai.dto.department;

import lombok.Data;

/**
 * ClassName: DepartmentListDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:27
 * @Version: 1.0
 */
@Data
public class DepartmentListDTO {
    private String departmentName;
    private Long status;
    private Long tenantId;
}

package com.yitai.dto.department;

import lombok.Data;

/**
 * ClassName: DepartmentUserDTO
 * Package: com.yitai.dto.department
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 17:39
 * @Version: 1.0
 */
@Data
public class DepartmentUserDTO {
    private Long deptId;
    private Long id;
    private String username;
    private String phone;
}

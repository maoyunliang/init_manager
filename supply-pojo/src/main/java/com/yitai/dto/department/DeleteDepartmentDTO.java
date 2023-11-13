package com.yitai.dto.department;

import lombok.Data;

/**
 * ClassName: DeleteDepartMentDTO
 * Package: com.yitai.dto.department
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 16:19
 * @Version: 1.0
 */
@Data
public class DeleteDepartmentDTO {
    private Long departmentId;
    private Long tenantId;
}

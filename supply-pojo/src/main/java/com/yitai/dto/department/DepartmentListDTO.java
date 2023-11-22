package com.yitai.dto.department;

import com.yitai.dto.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: DepartmentListDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:27
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentListDTO extends BaseBody {
    private String departmentName;
    private Integer status;
}

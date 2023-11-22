package com.yitai.dto.role;

import com.yitai.dto.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ClassName: AddRoleDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 11:27
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends BaseBody {
    private Long id;
    private String roleName;
    private String identity;
    private String roleDesc;
    private Integer status;
    private List<Long> menuIds;
}

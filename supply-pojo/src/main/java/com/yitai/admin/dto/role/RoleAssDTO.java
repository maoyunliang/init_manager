package com.yitai.admin.dto.role;

import com.yitai.base.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ClassName: RoleUserDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 8:52
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleAssDTO extends BaseBody {
    private Long id;
    private Long roleId;
    private List<Long> userIds;
    private List<Long> menuIds;
    private List<Long> deptIds;
}

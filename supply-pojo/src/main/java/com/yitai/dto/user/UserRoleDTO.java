package com.yitai.dto.user;

import com.yitai.dto.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ClassName: UserRoleDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/24 11:24
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRoleDTO extends BaseBody {
    private Long id;
    private Long userId;
    private List<Long> roleIds;
}

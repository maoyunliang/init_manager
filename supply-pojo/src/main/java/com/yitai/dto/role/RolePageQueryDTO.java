package com.yitai.dto.role;

import com.yitai.dto.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: RolePageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:38
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePageQueryDTO extends BaseBody {
    private String roleName;
    private String identity;
    private Integer status;
    private int page =1 ;
    private int pageSize =10;
}

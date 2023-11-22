package com.yitai.dto.user;

import com.yitai.dto.BaseBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ClassName: EmployeePageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/26 8:59
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageQueryDTO extends BaseBody {
    private String username;
    private int page =1 ;
    private int pageSize =10;
}

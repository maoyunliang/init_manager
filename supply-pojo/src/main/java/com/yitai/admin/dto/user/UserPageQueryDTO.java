package com.yitai.admin.dto.user;

import com.yitai.base.BaseBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String phone;
    //部门id集合
    private List<Integer> departIds;
    private int page =1 ;
    private int pageSize =10;
}

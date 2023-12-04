package com.yitai.admin.dto.user;

import com.yitai.base.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ClassName: EmployeeDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 15:49
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseBody {
    private Long id;
    private String realname;
    private String username;
    private String idNumber;
    private String phone;
    private String email;
    private String sex;
    private String avatar;
    private List<Integer> deptIds;
    private List<Integer> roleIds;
}
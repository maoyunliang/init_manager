package com.yitai.dto.sys;

import lombok.Data;

/**
 * ClassName: EmployeeDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 15:49
 * @Version: 1.0
 */
@Data
public class UserDTO {
    private Long id;
    private String realname;
    private String username;
    private String idNumber;
    private String phone;
    private String email;
    private String sex;
    private String avatar;
}

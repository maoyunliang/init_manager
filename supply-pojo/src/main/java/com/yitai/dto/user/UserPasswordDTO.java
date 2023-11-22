package com.yitai.dto.user;

import lombok.Data;

/**
 * ClassName: UserPasswordDTO
 * Package: com.yitai.dto.user
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 17:05
 * @Version: 1.0
 */
@Data
public class UserPasswordDTO {
    private String oldPassword;
    private String newPassword;
}

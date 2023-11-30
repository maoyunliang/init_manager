package com.yitai.admin.dto.user;

import lombok.Data;

/**
 * ClassName: LoginMessageDTO
 * Package: com.yitai.dto.sys
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/30 11:21
 * @Version: 1.0
 */
@Data
public class LoginMessageDTO {
    private String phoneNumber;
    private String verifyCode;
}

package com.yitai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: User
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/17 16:20
 * @Version: 1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String openid;
    private String password;
    private String realname;
    private String sex;
    private String idNumber;
    private String avatar;
    private String phone;
    private String email;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

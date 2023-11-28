package com.yitai.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: UserLoginVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/17 16:19
 * @Version: 1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录返回的数据格式")
public class UserLoginVO {
    @Schema(name = "用户id")
    private Long id;
    @Schema(name = "用户名")
    private String username;
    @Schema(name = "昵称(姓名)")
    private String realname;
    @Schema(name = "jwt令牌")
    private String token;
}

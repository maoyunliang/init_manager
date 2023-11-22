package com.yitai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: UserVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/1 9:23
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息返回的数据格式")
public class UserVO {
    @Schema(name = "用户id")
    private Long id;
    @Schema(name = "用户名")
    private String username;
    @Schema(name = "昵称(姓名)")
    private String realname;
    @Schema(name = "微信openid")
    private String openid;
    @Schema(name = "性别")
    private String sex;
    @Schema(name = "身份证号")
    private String idNumber;
    @Schema(name = "头像")
    private String avatar;
    @Schema(name = "手机号")
    private String phone;
    @Schema(name = "邮箱号")
    private String email;
    @Schema(name = "关联部门")
    private List<String> departments;
    @Schema(name = "状态")
    private Integer status;
    @Schema(name = "角色关联包含")
    private Integer hasUser = -1;
}

package com.yitai.admin.vo;

import com.yitai.annotation.excel.ExcelExport;
import com.yitai.annotation.excel.ExcelSheet;
import com.yitai.annotation.excel.Watermark;
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
@ExcelSheet(watermark = @Watermark(type = 1))
public class UserVO {
    @Schema(description = "用户id")
    private Long id;

    @ExcelExport(value = "用户名")
    @Schema(description = "用户名")
    private String username;

    @ExcelExport(value = "昵称(姓名)")
    @Schema(description = "昵称(姓名)")
    private String realname;

    @Schema(description = "微信openid")
    private String openid;

    @ExcelExport(value = "性别")
    @Schema(description = "性别")
    private String sex;

    @ExcelExport(value = "身份证号")
    @Schema(description = "身份证号")
    private String idNumber;

    @ExcelExport(value = "头像")
    @Schema(description = "头像")
    private String avatar;

    @ExcelExport(value = "手机号")
    @Schema(description = "手机号")
    private String phone;

    @ExcelExport(value = "邮箱号")
    @Schema(description = "邮箱号")
    private String email;

    @ExcelExport(value = "关联部门")
    @Schema(description = "关联部门")
    private List<String> departments;

    @ExcelExport(value = "关联角色")
    @Schema(description = "关联角色")
    private List<String> roles;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "角色关联包含")
    @Builder.Default
    private Integer hasUser = -1;
}

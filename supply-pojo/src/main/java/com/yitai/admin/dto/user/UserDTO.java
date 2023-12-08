package com.yitai.admin.dto.user;

import com.yitai.annotation.excel.ExcelImport;
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
    @ExcelImport(value = "用户名")
    private String username;
    @ExcelImport(value = "昵称(姓名)")
    private String realname;
    @ExcelImport(value = "身份证号")
    private String idNumber;
    @ExcelImport(value = "手机号")
    private String phone;
    @ExcelImport(value = "邮箱号")
    private String email;
    @ExcelImport(value = "性别")
    private String sex;
    @ExcelImport(value = "用户状态")
    private Integer status;
    @ExcelImport(value = "头像")
    private String avatar;
    @ExcelImport(value = "关联部门")
    private List<Long> deptIds;
    @ExcelImport(value = "关联角色")
    private List<Long> roleIds;
}

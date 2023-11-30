package com.yitai.admin.dto.department;

import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Classdescription: DepartmentDTO
 * Package: com.yitai.dto.department
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:47
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentDTO extends BaseBody {
    @Schema(description = "部门主键")
    private Long id;
    @Schema(description = "部门名称")
    private String departmentName;
    @Schema(description = "部门编号")
    private String departmentNo;
    @Schema(description = "部门备注")
    private String remark;
    @Schema(description = "部门父id")
    private Long pid;
    @Schema(description = "部门负责人id")
    private Long leader;
    @Schema(description = "部门负责人姓名")
    private String username;
    @Schema(description = "部门状态")
    private Integer status;
    @Schema(description = "部门排序")
    private Long sortNo;
}

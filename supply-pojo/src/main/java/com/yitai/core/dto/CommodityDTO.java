package com.yitai.core.dto;

import com.yitai.annotation.excel.ExcelExport;
import com.yitai.annotation.excel.ExcelSheet;
import com.yitai.annotation.excel.Watermark;
import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
@ExcelSheet
public class CommodityDTO extends BaseBody {
    @Schema(description = "主键")
    private Long id;
    @ExcelExport(value = "货品名称")
    @Schema(description = "货品名称")
    private String commodityName;
    @ExcelExport(value = "货品编码")
    @Schema(description = "货品编码")
    private String commodityNo;
    @Schema(description = "供应商id")
    private Long supplyId;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "类目id")
    private Long categoryId;
    @ExcelExport(value = "货品类别")
    @Schema(description = "类目名称")
    private String categoryName;
    @ExcelExport(value = "单位")
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "部门状态")
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private Integer isDel;
}

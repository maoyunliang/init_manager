package com.yitai.core.req;

import com.yitai.annotation.excel.ExcelImport;
import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CommoditySaveReq extends BaseBody {
    @Schema(description = "主键")
    private Long id;

    @ExcelImport(value = "货品名称")
    @Schema(description = "名称")
    private String commodityName;

    @ExcelImport(value = "货品编码")
    @Schema(description = "编码")
    private String commodityNo;

    @Schema(description = "状态")
    private Integer status;

    @ExcelImport(value = "货品类别")
    @Schema(description = "类目名称")
    private String categoryName;


    @Schema(description = "类目id")
    private Long categoryId;

    @ExcelImport(value = "规格单位")
    @Schema(description = "单位")
    private String unit;

    @Schema(description = "备注")
    private String remark;
}

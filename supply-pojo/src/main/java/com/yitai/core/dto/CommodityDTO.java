package com.yitai.core.dto;

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
public class CommodityDTO extends BaseBody {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "商品名称")
    private String CommodityName;
    @Schema(description = "商品编码")
    private String CommodityNo;
    @Schema(description = "供应商id")
    private Long supplyId;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "类目id")
    private Long categoryId;
    @Schema(description = "类目名称")
    private Long categoryName;
    @Schema(description = "单位")
    private String unit;
    @Schema(description = "部门状态")
    private Integer status;
}

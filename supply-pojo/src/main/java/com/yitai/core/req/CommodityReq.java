package com.yitai.core.req;

import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityReq extends BaseBody {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "名称")
    private String commodityName;
    @Schema(description = "编码")
    private String commodityNo;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "类目名称")
    private String categoryName;

    @Schema(description = "ids")
    private List<Long> ids;

    @Schema(description = "exportFields")
    private List<String> exportFields;

    private Integer page;
    private Integer pageSize;
}

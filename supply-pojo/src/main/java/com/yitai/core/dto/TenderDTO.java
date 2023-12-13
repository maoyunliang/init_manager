package com.yitai.core.dto;

import com.yitai.base.BaseBody;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: TenderDTO
 * Package: com.yitai.core.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 11:48
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TenderDTO extends BaseBody {
    private String id;
    private String tenderNo;
    private String tenderName;
    private List<Long> deptIds;
    @Schema(description = "邀请供应商列表")
    private List<Long> supplierIds;
    @Schema(description = "已报价供应商")
    private List<Long> quotedIds;
    @Schema(description = "招标开始时间")
    private LocalDateTime beginTime;
    @Schema(description = "招标截止时间")
    private LocalDateTime endTime;
    @Schema(description = "招标状态 1=草稿, 2=进行中, 3=已完成")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    private Integer page;
    private Integer pageSize;
}

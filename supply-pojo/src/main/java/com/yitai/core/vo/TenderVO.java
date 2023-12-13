package com.yitai.core.vo;

import com.yitai.annotation.excel.ExcelExport;
import com.yitai.annotation.excel.ExcelSheet;
import com.yitai.annotation.excel.Watermark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TenderVO
 * Package: com.yitai.core.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 11:49
 * @Version: 1.0
 */
@Data
@ExcelSheet(watermark = @Watermark(type = 1, src = "江西以太科技园有限公司"))
public class TenderVO {
    private String id;
    @ExcelExport(value = "招标单号")
    @Schema(description = "招标单号")
    private String tenderNo;
    @ExcelExport(value = "招标名称")
    @Schema(description = "招标名称")
    private String tenderName;
    @ExcelExport(value = "关联部门")
    @Schema(description = "关联部门")
    private List<String> deptNames;
    @ExcelExport(value = "招标列表")
    @Schema(description = "招标列表")
    private String tenderList;
    @ExcelExport(value = "招标有效期")
    @Schema(description = "招标有效期")
    private String validityTime;
    @ExcelExport(value = "招标开始时间")
    @Schema(description = "招标开始时间")
    private LocalDateTime beginTime;
    @ExcelExport(value = "招标截止时间")
    @Schema(description = "招标截止时间")
    private LocalDateTime endTime;
    @ExcelExport(value = "邀请供应商列表")
    @Schema(description = "邀请供应商列表")
    private Map<String, Integer> suppliers;
    @ExcelExport(value = "邀请供应商数量")
    @Schema(description = "邀请供应商数量")
    private Integer suppliersNums;
    @ExcelExport(value = "报价供应商数量")
    @Schema(description = "报价供应商数量")
    private Integer quotedNums;
    @ExcelExport(value = "发起人")
    @Schema(description = "发起人")
    private String createUser;
    @ExcelExport(value = "发布时间")
    @Schema(description = "发布时间")
    private LocalDateTime createTime;
    @ExcelExport(value = "招标状态")
    @Schema(description = "招标状态 1=草稿, 2=进行中, 3=已完成")
    private Integer status;
}

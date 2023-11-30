package com.yitai.admin.entity;

import com.yitai.annotation.excel.ExcelExport;
import com.yitai.annotation.excel.ExcelSheet;
import com.yitai.annotation.excel.Watermark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: OperationLog
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 11:27
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelSheet(watermark = @Watermark(type = 1, src = "江西以太科技园有限公司"))
public class OperationLog {
    private Integer id;
    @ExcelExport(value = "操作详情")
    private String operation;
    @ExcelExport(value = "操作类型")
    private String type;
    @ExcelExport(value = "ip地址")
    private String ip;
    @ExcelExport(value = "操作人")
    private String user;
    @ExcelExport(value = "操作耗时")
    private double duration;
    @ExcelExport(value = "操作时间")
    private String time;
    private Long tenantId;
}

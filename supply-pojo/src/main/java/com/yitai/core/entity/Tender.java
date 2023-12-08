package com.yitai.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: Tender
 * Package: com.yitai.core
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/7 17:23
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tender {
    private String id;
    private String tenderNo;
    private String tenderName;
    private List<Long> deptIds;

    @Schema(description = "邀请供应商列表")
    private List<Long> supplierIds;

    @Schema(description = "已报价供应商")
    private List<Long> quoted;

    @Schema(description = "招标状态 1=正常,-1=暂停")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

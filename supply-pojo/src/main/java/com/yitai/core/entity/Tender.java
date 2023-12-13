package com.yitai.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Schema(description = "招标开始时间")
    private LocalDateTime beginTime;
    @Schema(description = "招标截止时间")
    private LocalDateTime endTime;
    @Schema(description = "招标状态 1=草稿, 2=进行中, 3=已完成")
    private Integer status;
    @Schema(description = "备注")
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

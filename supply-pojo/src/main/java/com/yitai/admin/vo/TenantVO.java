package com.yitai.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: TenantVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 9:03
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "租户信息返回数据格式")
public class TenantVO {
    @Schema(description = "租户id")
    private Long id;
    @Schema(description = "租户名")
    private String tenantName;
    @Schema(description = "租户logo")
    private String tenantLogo;
    @Schema(description = "联系方式")
    private String contact;
    @Schema(description = "地区")
    private String location;
    @Schema(description = "详细地址")
    private String address;
    @Schema(description = "相关资质")
    private String qualification;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "状态")
    private Integer status;
}

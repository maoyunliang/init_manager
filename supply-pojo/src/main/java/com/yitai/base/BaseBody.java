package com.yitai.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ClassName: BaseBody
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/22 11:38
 * @Version: 1.0
 */
@Data
public class BaseBody {
    @Schema(description = "租户主键")
    private Long tenantId;
}

package com.yitai.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: RouterVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/28 8:58
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterVO {
    private Long id;
    private Long tenantId;
    @Schema(name = "路由")
    private List<MenuVO> menuVOS;
    @Schema(name = "权限列表")
    private List<String> permiList;
}

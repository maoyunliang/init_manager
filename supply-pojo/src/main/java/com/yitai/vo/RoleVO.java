package com.yitai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * ClassName: RoleVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/6 11:23
 * @Version: 1.0
 */
@Data
public class RoleVO {
    private Long id;
    private String roleName;
    private String identity;
    private String roleType;
    private String roleDesc;
    private Integer status;
    @Schema(name = "关联菜单")
    private List<MenuVO> menuVOS;
    @Schema(name = "关联用户")
    private List<DepartmentVO> userVOS;
    @Schema(name = "关联部门")
    private List<Long> deptIds;
}

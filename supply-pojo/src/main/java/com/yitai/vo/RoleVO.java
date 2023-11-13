package com.yitai.vo;

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
    private String roleType;
    private String roleDesc;
    private String roleStatus;
    private List<MenuVO> menuVOS;
    private List<UserVO> userVOS;
}

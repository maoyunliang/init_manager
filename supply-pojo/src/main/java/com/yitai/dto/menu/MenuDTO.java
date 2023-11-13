package com.yitai.dto.menu;

import lombok.Data;

/**
 * ClassName: MenuDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 16:47
 * @Version: 1.0
 */
@Data
public class MenuDTO {
    private Long id;
    private String menuName;
    private String menuPath;
    private String menuRouter;
    private String identify;
    private String menuType;
    private String icon;
    private Long status;
    private Long menuPid;
    private Long sortNo;
    private Long tenantId;
}

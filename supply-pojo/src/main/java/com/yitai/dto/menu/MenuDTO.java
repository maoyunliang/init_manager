package com.yitai.dto.menu;

import com.yitai.dto.BaseBody;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName: MenuDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 16:47
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDTO extends BaseBody {
    private Long id;
    private String menuName;
    private String menuPath;
    private String menuRouter;
    private String identify;
    private String menuType;
    private String icon;
    private Integer status;
    private Integer visible;
    private Long menuPid;
    private Long sortNo;
}

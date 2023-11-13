package com.yitai.dto.menu;

import lombok.Data;

/**
 * ClassName: RolePageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:38
 * @Version: 1.0
 */
@Data
public class MenuPageQueryDTO {
    private String menuName;
    private String menuType;
    private int page =1 ;
    private int pageSize =10;
}

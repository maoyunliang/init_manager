package com.yitai.dto;

import lombok.Data;

import java.util.List;

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
    private String menuType;
    private Long menuPid;
    private Long sortNo;
    private List<MenuDTO> children;
}

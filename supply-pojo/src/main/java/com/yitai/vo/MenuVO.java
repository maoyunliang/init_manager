package com.yitai.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: MenuVO
 * Package: com.yitai.vo
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 16:44
 * @Version: 1.0
 */
@Data
public class MenuVO implements Serializable {
    private Long id;
    private String menuName;
    private String menuPath;
    private String menuType;
    private Long menuPid;
    private Long sortNo;
    private List<MenuVO> children;
}

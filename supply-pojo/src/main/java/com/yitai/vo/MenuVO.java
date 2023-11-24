package com.yitai.vo;

import com.yitai.annotation.ExcelExport;
import com.yitai.annotation.ExcelSheet;
import com.yitai.annotation.Watermark;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@ExcelSheet(watermark = @Watermark(type = 1, src = "江西以太科技园有限公司"))
public class MenuVO implements Serializable {
    private Long id;
    @ExcelExport(value = "菜单名称")
    private String menuName;
    @ExcelExport(value = "菜单路径")
    private String menuPath;
    @ExcelExport(value = "菜单路由")
    private String menuRouter;
    private String identify;
    private String menuType;
    private String icon;
    private Long menuPid;
    private Long sortNo;
    private Long status;
    private Integer hasMenu = -1;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<MenuVO> children;
}

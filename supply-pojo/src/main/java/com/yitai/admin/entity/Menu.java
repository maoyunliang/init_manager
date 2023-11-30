package com.yitai.admin.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: Menu
 * Package: com.yitai.entity
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 11:55
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private Long id;
    private String menuName;
    private String menuPath;
    private String menuRouter;
    private String identify;
    private String menuType;
    private String icon;
    private Long menuPid;
    private Long sortNo;
    private Integer status;
    @Schema(description = "是否可见")
    private Integer visible;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private Integer isDel;
}

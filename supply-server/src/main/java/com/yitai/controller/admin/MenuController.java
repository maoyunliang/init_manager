package com.yitai.controller.admin;

import com.yitai.annotation.AutoLog;
import com.yitai.annotation.HasPermit;
import com.yitai.dto.sys.MenuDTO;
import com.yitai.dto.sys.MenuPageQueryDTO;
import com.yitai.enumeration.LogType;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: menuController
 * Package: com.yitai.controller.menu
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:36
 * @Version: 1.0
 */
@Tag(name = "菜单管理相关接口")
@RestController
@RequestMapping("admin/sys/menu")
@Slf4j
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Operation(summary = "菜单分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(@ModelAttribute MenuPageQueryDTO menuPageQueryDTO){
        log.info("分页查询:{}", menuPageQueryDTO);
        PageResult pageResult = menuService.pageQuery(menuPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "新建菜单接口")
    @PostMapping("/add")
    @HasPermit(permission = "sys:menu:add")
    @AutoLog(operation = "新增菜单操作", type = LogType.ADD)
    public Result<String> save(@RequestBody MenuDTO menuDTO){
        log.info("新增菜单:{}", menuDTO);
        menuService.save(menuDTO);
        return Result.success();
    }

    @Operation(summary = "编辑菜单接口")
    @PostMapping("/update")
    @HasPermit(permission = "sys:menu:update")
    @AutoLog(operation = "编辑菜单操作", type = LogType.UPDATE)
    public Result<String> update(@RequestBody MenuDTO menuDTO){
        log.info("编辑菜单:{}", menuDTO);
        menuService.update(menuDTO);
        return Result.success();
    }

    @Operation(summary = "删除菜单接口")
    @PostMapping("/delete/{menuId}")
    @HasPermit(permission = "sys:menu:deleteById")
    @AutoLog(operation = "删除菜单操作", type = LogType.DELETE)
    public Result<?> delete(@PathVariable Integer menuId){
        log.info("删除菜单");
        menuService.delete(menuId);
        return Result.success();
    }
}

package com.yitai.controller.admin;

import com.yitai.annotation.AutoLog;
import com.yitai.annotation.HasPermit;
import com.yitai.dto.sys.*;
import com.yitai.enumeration.LogType;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: RoleController
 * Package: com.yitai.controller.role
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:36
 * @Version: 1.0
 */
@Tag(name = "角色管理相关接口")
@RestController
@RequestMapping("admin/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Operation(summary = "角色分页查询")
    @PostMapping ("/page")
    public Result<PageResult> page(@RequestBody RolePageQueryDTO pageQueryDTO){
        log.info("分页查询:{}", pageQueryDTO);
        PageResult pageResult = roleService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "新建角色接口")
    @PostMapping("/add")
    @HasPermit(permission = "sys:role:add")
    @AutoLog(operation = "新增角色操作", type = LogType.ADD)
    public Result<String> save(@RequestBody RoleDTO roleDTO){
        log.info("新增角色:{}", roleDTO);
        roleService.save(roleDTO);
        return Result.success();
    }

    @Operation(summary = "编辑角色接口")
    @PostMapping("/update")
    @HasPermit(permission = "sys:role:update")
    @AutoLog(operation = "编辑角色操作", type = LogType.UPDATE)
    public Result<String> update(@RequestBody RoleDTO updateRoleDTO){
        log.info("编辑角色:{}", updateRoleDTO);
        roleService.update(updateRoleDTO);
        return Result.success();
    }

    @Operation(summary = "删除角色接口")
    @PostMapping("/delete")
    @HasPermit(permission = "sys:role:delete")
    @AutoLog(operation = "删除角色操作", type = LogType.DELETE)
    public Result<?> delete(@RequestBody DeleteRoleDTO deleteRoleDTO){
        log.info("删除角色：{}", deleteRoleDTO);
        roleService.delete(deleteRoleDTO);
        return Result.success();
    }

    @Operation(summary = "分配菜单")
    @PostMapping("/assMenu")
    @HasPermit(permission = "sys:role:assMenu")
    @AutoLog(operation = "给角色分配菜单", type = LogType.ASSIGN)
    public Result<?> assMenu(@RequestBody RoleMenuDTO roleMenuDTO){
        log.info("分配菜单：{}", roleMenuDTO);
        roleService.assMenu(roleMenuDTO);
        return Result.success();
    }

    @Operation(summary = "分配用户")
    @PostMapping("/assUser")
    @HasPermit(permission = "sys:role:assRole")
    @AutoLog(operation = "给角色分配用户", type = LogType.ASSIGN)
    public Result<?> assUser(@RequestBody RoleUserDTO roleUserDTO){
        log.info("分配用户：{}", roleUserDTO);
        roleService.assUser(roleUserDTO);
        return Result.success();
    }

    @Operation(summary = "根据Id获取角色信息（关联菜单、 关联用户）")
    @PostMapping("/getRoleById")
    public Result<?> getRoleMenuById(@RequestParam Long roleId, @RequestParam Long tenantId){
        log.info("根据Id获取角色关联菜单信息：{}", roleId);
        return Result.success(roleService.getRoleById(roleId, tenantId));
    }
}

package com.yitai.controller.admin;

import com.yitai.admin.dto.role.RoleAssDTO;
import com.yitai.admin.dto.role.RoleDTO;
import com.yitai.admin.dto.role.RolePageQueryDTO;
import com.yitai.service.DepartmentService;
import com.yitai.service.RoleService;
import com.yitai.annotation.admin.AutoLog;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.enumeration.LogType;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.admin.vo.DepartmentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    private DepartmentService departmentService;
    @Operation(summary = "角色分页查询")
    @PostMapping ("/page")
    @HasPermit(permission = "sys:role:list")
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
    public Result<?> delete(@RequestBody RoleDTO deleteRoleDTO){
        log.info("删除角色：{}", deleteRoleDTO);
        roleService.delete(deleteRoleDTO);
        return Result.success();
    }

    @Operation(summary = "分配菜单")
    @PostMapping("/assMenu")
    @HasPermit(permission = "sys:role:assMenu")
    @AutoLog(operation = "给角色分配菜单", type = LogType.ASSIGN)
    public Result<?> assMenu(@RequestBody RoleAssDTO roleMenuDTO){
        log.info("分配菜单：{}", roleMenuDTO);
        roleService.assMenu(roleMenuDTO);
        return Result.success();
    }

    @Operation(summary = "分配用户")
    @PostMapping("/assUser")
    @HasPermit(permission = "sys:role:assUser")
    @AutoLog(operation = "给角色分配用户", type = LogType.ASSIGN)
    public Result<?> assUser(@RequestBody RoleAssDTO roleUserDTO){
        log.info("分配用户：{}", roleUserDTO);
        roleService.assUser(roleUserDTO);
        return Result.success();
    }

    @Operation(summary = "菜单权限按钮-->关联菜单接口列表")
    @HasPermit(permission = "sys:role:assMenu")
    @PostMapping("/getMenu")
    public Result<?> getMenu(@RequestBody RoleDTO roleInfoDTO){
        log.info("根据Id获取角色关联菜单信息：{}", roleInfoDTO);
        return Result.success(roleService.getMenu(roleInfoDTO));
    }

    @Operation(summary = "人员按钮-->关联用户接口列表")
    @HasPermit(permission = "sys:role:assUser")
    @PostMapping("/getUser")
    public Result<?> getUser(@RequestBody RoleDTO roleInfoDTO){
        log.info("根据Id获取角色关联用户信息：{}", roleInfoDTO);
        List<DepartmentVO> departmentVOS = departmentService.getUserByTree(roleInfoDTO.getTenantId());
        return Result.success(roleService.getUser(roleInfoDTO, departmentVOS));
    }

    @Operation(summary = "数据权限按钮-->关联部门接口列表")
    @HasPermit(permission = "sys:role:assDept")
    @PostMapping("/getDept")
    public Result<?> getDept(@RequestBody RoleDTO roleDTO){
        log.info("根据Id获取角色关联部门信息：{}", roleDTO);
        return Result.success(roleService.getDept(roleDTO));
    }
}

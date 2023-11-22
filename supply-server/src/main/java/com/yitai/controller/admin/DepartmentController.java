package com.yitai.controller.admin;

import com.yitai.annotation.HasPermit;
import com.yitai.dto.BaseBody;
import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.result.Result;
import com.yitai.service.DepartmentService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.DepartmentVO;
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
 * ClassName: DepartmentController
 * Package: com.yitai.controller.admin
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:17
 * @Version: 1.0
 */
@Tag(name = "部门管理接口")
@RequestMapping("admin/department")
@RestController
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Operation(summary = "部门列表")
    @HasPermit(permission = "sys:dep:list")
    @PostMapping("/list")
    public Result<?> pageQuery(@RequestBody DepartmentListDTO departmentListDTO){
        log.info("部门列表查询:{}", departmentListDTO);
        List<DepartmentVO> departmentVOS = departmentService.list(departmentListDTO);
        return Result.success(TreeUtil.buildTree(departmentVOS, DepartmentVO::getPid));
    }

    @Operation(summary = "新增部门")
    @HasPermit(permission = "sys:dep:add")
    @PostMapping("/save")
    public Result<?> save(@RequestBody DepartmentDTO departmentDTO){
        log.info("新增部门:{}", departmentDTO);
        departmentService.save(departmentDTO);
        return Result.success();
    }

    @Operation(summary = "修改部门")
    @HasPermit(permission = "sys:dep:update")
    @PostMapping("/update")
    public Result<?> update(@RequestBody DepartmentDTO departmentDTO){
        log.info("修改部门信息:{}", departmentDTO);
        departmentService.update(departmentDTO);
        return Result.success();
    }

    @Operation(summary = "删除部门")
    @HasPermit(permission = "sys:dep:delete")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody DepartmentDTO deleteDepartmentDTO){
        log.info("删除部门:{}", deleteDepartmentDTO);
        departmentService.delete(deleteDepartmentDTO);
        return Result.success();
    }

    @Operation(summary = "获取部门树和关联人员信息")
    @PostMapping("/getUserByTree")
    public Result<?> getUserByTree(@RequestBody BaseBody baseBody){
        log.info("获取部门树和关联人员信息:");
        List<DepartmentVO> departmentVOS = departmentService.getUserByTree(baseBody.getTenantId());
        return Result.success(TreeUtil.buildTree(departmentVOS,0,DepartmentVO::getPid));
    }
}

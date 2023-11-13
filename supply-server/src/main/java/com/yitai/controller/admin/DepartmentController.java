package com.yitai.controller.admin;

import com.yitai.dto.department.DeleteDepartmentDTO;
import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.result.Result;
import com.yitai.service.DepartmentService;
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

    @Operation(summary = "部门列表查询")
    @PostMapping("/list")
    public Result<?> pageQuery(@RequestBody DepartmentListDTO departmentListDTO){
        log.info("部门列表查询:{}", departmentListDTO);
        List<DepartmentVO> departmentVOS = departmentService.list(departmentListDTO);
        return Result.success(departmentVOS);
    }

    @Operation(summary = "新增部门")
    @PostMapping("/save")
    public Result<?> save(@RequestBody DepartmentDTO departmentDTO){
        log.info("新增部门:{}", departmentDTO);
        departmentService.save(departmentDTO);
        return Result.success();
    }

    @Operation(summary = "修改部门")
    @PostMapping("/update")
    public Result<?> update(@RequestBody DepartmentDTO departmentDTO){
        log.info("修改部门信息:{}", departmentDTO);
        departmentService.update(departmentDTO);
        return Result.success();
    }

    @Operation(summary = "删除部门")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody DeleteDepartmentDTO deleteDepartmentDTO){
        log.info("删除部门:{}", deleteDepartmentDTO);
        departmentService.delete(deleteDepartmentDTO);
        return Result.success();
    }
}

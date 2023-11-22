package com.yitai.controller.admin;

import com.yitai.dto.tenant.TenantDTO;
import com.yitai.dto.tenant.TenantListDTO;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: TenantController
 * Package: com.yitai.controller.admin
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:00
 * @Version: 1.0
 */
@Tag(name = "租户管理接口")
@RequestMapping("admin/tenant")
@RestController
@Slf4j
public class TenantController {
    @Autowired
    private TenantService tenantService;

    @Operation(summary = "租户分页查询")
//    @HasPermit(permission = "sys:tenant:page")
    @PostMapping("/page")
    public Result<?> pageQuery(@RequestBody TenantListDTO tenantListDTO){
        log.info("租户分页查询:{}", tenantListDTO);
        PageResult tenantVOS = tenantService.page(tenantListDTO);
        return Result.success(tenantVOS);
    }

    @Operation(summary = "新增租户")
    @PostMapping("/save")
    public Result<?> save(@RequestBody TenantDTO tenantDTO){
        log.info("新增租户:{}", tenantDTO);
        tenantService.save(tenantDTO);
        return Result.success();
    }

    @Operation(summary = "修改租户")
    @PostMapping("/update")
    public Result<?> update(@RequestBody TenantDTO tenantDTO){
        log.info("修改租户信息:{}", tenantDTO);
        tenantService.update(tenantDTO);
        return Result.success();
    }

    @Operation(summary = "删除租户")
    @PostMapping("/delete/{tenantId}")
    public Result<?> delete(@PathVariable Long tenantId){
        log.info("删除租户:{}", tenantId);
        tenantService.delete(tenantId);
        return Result.success();
    }
}

package com.yitai.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yitai.admin.dto.user.UserDTO;
import com.yitai.result.PageResult;
import com.yitai.service.CommodityService;
import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.req.CommodityReq;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.result.Result;
import com.yitai.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "货品管理接口")
@RequestMapping("admin/commodity")
@RestController
@Slf4j
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "货品列表")
//    @HasPermit(permission = "core:commodity:list")
    public Result<PageInfo<CommodityDTO>> list(@RequestBody CommodityReq req) {
        req.setPage(req.getPage() == null ? 1 : req.getPage());
        req.setPageSize(req.getPageSize() == null ? 10 : req.getPageSize());
        PageInfo<CommodityDTO> page = commodityService.listByPage(req);
        return Result.success(page);
    }

    @PostMapping("/save")
    @Operation(summary = "保存货品")
    @HasPermit(permission = "core:commodity:save")
    public Result<?> save(@RequestBody CommoditySaveReq req) {
        commodityService.save(req);
        return Result.success();
    }


    @Operation(summary = "货品导出")
//    @HasPermit(permission = "core:commodity:export")
    @PostMapping(value = "/export")
    public void export(HttpServletResponse response, @RequestParam("tenantId") Long tenantId,
                       @RequestParam(value = "ids", required = false) List<Long> ids, @RequestParam(value = "exportFields", required = false) List<String> exportFields) {
        log.info("商品导出");
        CommodityReq req = new CommodityReq();
        req.setIds(ids);
        req.setTenantId(tenantId);
        List<CommodityDTO> list = commodityService.list(req);
        ExcelUtils.export(response, "商品导出表", list, CommodityDTO.class, exportFields);
    }

    @Operation(summary = "商品excel导入")
//    @HasPermit(permission = "core:commodity:import")
    @PostMapping("/import")
    public Result<?> importUser(@RequestParam("tenantId") Long tenantId, @RequestParam("userSheet") MultipartFile multipartFile) {
        log.info("商品导入");
        List<CommoditySaveReq> commodityList = ExcelUtils.read(multipartFile, CommoditySaveReq.class);
        commodityList.stream().forEach(e -> e.setTenantId(tenantId));
        commodityService.batchSave(commodityList);
        return Result.success(commodityList);
    }
}

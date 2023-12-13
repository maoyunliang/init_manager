package com.yitai.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理接口")
@RequestMapping("admin/commodity")
@Controller
@Slf4j
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "商品列表")
//    @HasPermit(permission = "core:commodity:list")
    public Result<List<CommodityDTO>> list(@RequestBody CommodityReq req) {
        List<CommodityDTO> list = commodityService.list(req);
        return Result.success(list);
    }

    @PostMapping("/save")
    @Operation(summary = "保存商品")
    @HasPermit(permission = "core:commodity:save")
    public Result<?> save(@RequestBody CommoditySaveReq req) {
        commodityService.save(req);
        return Result.success();
    }


    @Operation(summary = "商品导出")
    @HasPermit(permission = "core:commodity:export")
    @PostMapping(value = "/export")
    public void export(HttpServletResponse response,
                        @RequestBody CommodityReq req) {
        log.info("商品导出");
        List<CommodityDTO> list = commodityService.list(req);
        ExcelUtils.export(response, "商品导出表", list, CommodityDTO.class,req.getExportFields());
    }
}

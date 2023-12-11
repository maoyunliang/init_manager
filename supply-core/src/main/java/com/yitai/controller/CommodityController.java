package com.yitai.controller;


import com.yitai.core.dto.CommodityDTO;
import com.yitai.core.req.CommodityReq;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.core.req.CommoditySaveReq;
import com.yitai.result.Result;
import com.yitai.service.CommodityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "商品管理接口")
@RequestMapping("admin/commodity")
@RestController
@Slf4j
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "商品列表")
    @HasPermit(permission = "")
    public Result<?> list(@RequestBody CommodityReq req) {
        List<CommodityDTO> list = commodityService.list(req);
        return Result.success(list);
    }

    @PostMapping("/save")

    @Operation(summary = "保存商品")
    @HasPermit(permission = "")
    public Result<?> save(@RequestBody CommoditySaveReq req) {
        commodityService.save(req);
        return Result.success();
    }

}

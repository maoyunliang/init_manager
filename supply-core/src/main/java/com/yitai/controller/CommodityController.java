package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
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

/**
 * ClassName: FileController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 10:02
 * @Version: 1.0
 */

@Tag(name = "商品管理接口")
@RequestMapping("admin/commodity")
@RestController
@Slf4j
public class CommodityController {

    @Autowired
    private CommodityService jobService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "商品列表")
    @HasPermit(permission = "run:job:list")
    public Result<?> list(@RequestBody JobDTO jobDTO){
        List<SysJob> list = jobService.list(jobDTO);
        return Result.success(list);
    }


}

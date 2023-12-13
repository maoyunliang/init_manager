package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TenderController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/12 16:15
 * @Version: 1.0
 */
@Tag(name = "招采管理接口")
@RequestMapping("/tender")
@RestController
@Slf4j
public class TenderController {
    // 定时任务接口(租户级别)
    @GetMapping ("/list")
    @Operation(summary = "定时任务列表")
    @HasPermit(permission = "run:job:list")
    public Result<?> list(){
        return Result.success("list");
    }
}

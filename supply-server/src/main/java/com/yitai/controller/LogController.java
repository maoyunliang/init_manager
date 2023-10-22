package com.yitai.controller;

import com.yitai.dto.LogPageQueryDTO;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ClassName: LogController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 14:44
 * @Version: 1.0
 */
@Tag(name = "日志管理接口")
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 批量删除信息
     */
    @DeleteMapping("/delete/batch")
    @Operation(summary = "批量删除日志")
    public Result<?> batchDelete(@RequestBody List<Integer> ids){
        log.info("集合:{}",ids);
        logService.removeBatchIds(ids);
        return Result.success();
    }

    /**
     * 删除日志
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除日志")
    public Result<?> delete(@PathVariable Integer id){
        logService.removeById(id);
        return Result.success();
    }

    @PostMapping("/select")
    @Operation(summary = "分页模糊查询")
    public Result<?> selectByPage(@ModelAttribute LogPageQueryDTO logPageQueryDTO){
        log.info("分页查询, {}", logPageQueryDTO);
        PageResult pageResult = logService.pageQuery(logPageQueryDTO);
        return Result.success(pageResult);
    }

}

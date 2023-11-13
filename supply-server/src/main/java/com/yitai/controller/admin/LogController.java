package com.yitai.controller.admin;

import com.yitai.annotation.HasPermit;
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
@RequestMapping("admin/log")
@Slf4j
public class LogController {

    @Autowired
    LogService logService;

    /**
     * 批量删除信息
     */
    @DeleteMapping("/delete/batch")
    @HasPermit(permission = "run:log:delete")
    @Operation(summary = "批量删除操作日志")
    public Result<?> batchDelete(@RequestBody List<Integer> ids){
        log.info("批量删除操作日志:{}",ids);
        logService.removeBatchIds(ids);
        return Result.success();
    }
    /**
     * 删除日志
     */

    @PostMapping("/select")
    @HasPermit(permission = "run:log:list")
    @Operation(summary = "操作日志分页模糊查询")
    public Result<?> selectByPage(@RequestBody LogPageQueryDTO logPageQueryDTO){
        log.info("操作日志分页查询, {}", logPageQueryDTO);
        PageResult pageResult = logService.pageQuery(logPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 批量删除信息
     */
    @DeleteMapping("/delete/loginBatch")
    @HasPermit(permission = "run:loginLog:delete")
    @Operation(summary = "批量删除登录日志")
    public Result<?> batchLoginDelete(@RequestBody List<Integer> ids){
        log.info("集合:{}",ids);
        logService.removeBatchIds1(ids);
        return Result.success();
    }
    /**
     * 删除日志
     */
    @PostMapping("/selectLogin")
    @HasPermit(permission = "run:loginLog:list")
    @Operation(summary = "登录日志分页模糊查询")
    public Result<?> selectByPage1(@RequestBody LogPageQueryDTO logPageQueryDTO){
        log.info("登录日志分页查询, {}", logPageQueryDTO);
        PageResult pageResult = logService.pageQuery1(logPageQueryDTO);
        return Result.success(pageResult);
    }

}

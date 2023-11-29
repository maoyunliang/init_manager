package com.yitai.controller;

import com.yitai.annotation.admin.HasPermit;
import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import com.yitai.result.Result;
import com.yitai.service.JobService;
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

@Tag(name = "定时任务管理接口")
@RequestMapping("admin/job")
@RestController
@Slf4j
public class JobController {

    @Autowired
    private JobService jobService;

    // 定时任务接口(租户级别)
    @PostMapping("/list")
    @Operation(summary = "定时任务列表")
    @HasPermit(permission = "sys:job:list")
    public Result<?> list(@RequestBody JobDTO jobDTO){
        List<SysJob> list = jobService.list(jobDTO);
//        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
        return Result.success(list);
    }

    @PostMapping("/update")
    @Operation(summary = "更新定时任务")
    @HasPermit(permission = "sys:job:update")
    public Result<?> update(@RequestBody JobDTO jobDTO){
        jobService.update(jobDTO);
//        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
        return Result.success();
    }

    @PostMapping("/save")
    @Operation(summary = "添加定时任务")
    @HasPermit(permission = "sys:job:add")
    public Result<?> save(@RequestBody JobDTO jobDTO){
        jobService.save(jobDTO);
//        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
        return Result.success();
    }
}

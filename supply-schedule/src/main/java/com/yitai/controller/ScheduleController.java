package com.yitai.controller;

import com.yitai.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/schedule")
@RestController
@Slf4j
public class ScheduleController {
    // 头像文件上传
    @GetMapping ("/avatar")
    @Operation(summary = "文件上传")
    public Result<?> upload(){
//        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
        return Result.success("yes");
    }
}

package com.yitai.controller;

import cn.hutool.core.util.StrUtil;
import com.yitai.result.Result;
import com.yitai.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * ClassName: FileController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 10:02
 * @Version: 1.0
 */

@Tag(name = "文件管理相关接口")
@RequestMapping("/upload")
@RestController
@Slf4j
public class FileController {
    @Autowired
    FileService fileService;
    // 头像文件上传
    @PostMapping("/avatar/{location}")
    @Operation(summary = "文件上传")
    public Result<?> upload(@PathVariable String location, @RequestParam("image") MultipartFile multipartFile){
        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
        String filename = UUID.randomUUID().toString()
                .replaceAll("-","")+"."+
                StrUtil.subAfter(multipartFile.getOriginalFilename(),
                ".",true);
        filename = "images/"+location+"/" + filename;
        String result = fileService.upload(multipartFile, filename);
        return Result.success(result);
    }
}

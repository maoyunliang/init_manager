package com.yitai.controller;

import cn.hutool.core.util.StrUtil;
import com.yitai.result.Result;
import com.yitai.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("/avatar")
    @Operation(summary = "上传头像")
    public Result<?> avatar(@RequestParam("image") MultipartFile multipartFile){
        log.info("上传头像:{}", multipartFile.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replaceAll("-","")+"."+ StrUtil.subAfter(multipartFile.getOriginalFilename(),
                ".",true);
        filename = "images/avatars/" + filename;
        String result = fileService.upload(multipartFile, filename);
        return Result.success(result);
    }
}

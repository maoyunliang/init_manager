package com.yitai.controller;

import com.yitai.context.UploadStrategyContext;
import com.yitai.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final UploadStrategyContext uploadStrategyContext;
    // 头像文件上传
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<?> upload(@RequestParam("location") String location,
                            @RequestParam("file") MultipartFile multipartFile){
        log.info("文件:{} 上传位置:{}", multipartFile.getOriginalFilename(), location);
//        String filename = location+"/"+UUID.randomUUID().toString()
//                .replaceAll("-","")+"."+
//                StrUtil.subAfter(multipartFile.getOriginalFilename(),
//                ".",true);
        // 可以根据类型来做判断使用什么上传
        String result = uploadStrategyContext
                .executeUploadStrategy(multipartFile,"upload/"+location+"/","localUploadStrategyImpl");
        return Result.success(result);
    }

}

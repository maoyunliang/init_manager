package com.yitai.controller;

import com.yitai.result.Result;
import com.yitai.service.VideoServcie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: VideoController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/15 10:45
 * @Version: 1.0
 */
@Tag(name = "rtsp转flv接口")
@RequestMapping("/flv")
@RestController
@Slf4j
public class VideoController {
    @Autowired
    private VideoServcie videoServcie;

    @GetMapping ("/open")
    @Operation(summary = "转码")
    public Result<?> avatar(@RequestParam String url){
        videoServcie.open(url);
        return Result.success();
    }
}

package com.yitai.controller.admin;

import com.yitai.enumeration.SizeEnum;
import com.yitai.result.Result;
import com.yitai.utils.HardWareUtil;
import com.yitai.vo.server.ServerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: WebController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 11:25
 * @Version: 1.0
 */

@Tag(name = "系统信息相关接口")
@RequestMapping("admin/server")
@RestController
@Slf4j
public class WebController {
    @GetMapping("/info")
    @Operation(summary = "获取服务系统信息")
    public Result<?> batchDelete(){
        log.info("获取服务系统信息");
        ServerVO serverVO = ServerVO.builder().cpuInfo(HardWareUtil.getCpuInfo()).
                memoryInfo(HardWareUtil.getMemoryInfo(SizeEnum.GB)).
                jvmInfo(HardWareUtil.getJvmInfo()).
                systemDetails(HardWareUtil.getSystemInfo()).
                sysFiles(HardWareUtil.getSysFiles()).build();
        return Result.success(serverVO);
    }
}

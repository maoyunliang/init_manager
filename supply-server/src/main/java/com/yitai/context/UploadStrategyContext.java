package com.yitai.context;

import com.yitai.strategy.UploadStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * ClassName: UploadStrategyContext
 * Package: com.yitai.context
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 16:06
 * @Version: 1.0
 */

@Component
@RequiredArgsConstructor
public class UploadStrategyContext {
    private final Map<String, UploadStrategy> uploadStrategyMap;
    /**
     * 执行上传策略
     */
    public String executeUploadStrategy(MultipartFile file, final String filePath, String uploadServiceName) {
        // 执行特点的上传策略
        return uploadStrategyMap.get(uploadServiceName).uploadFile(file, filePath);
    }
}

package com.yitai.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: UploadStrategy
 * Package: com.yitai.strategy
 * Description: 文件上传策略
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 11:43
 * @Version: 1.0
 */
public interface UploadStrategy {
    /**
     * 上传文件
     * @param file 文件
     * @param filePath 文件上传路径
     * @return 文件上传完整路径
     */
    String uploadFile(MultipartFile file, final String filePath);
}

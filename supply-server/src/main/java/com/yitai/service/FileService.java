package com.yitai.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: FileService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 10:05
 * @Version: 1.0
 */
public interface FileService {
    String upload(MultipartFile multipartFile, String filename);
}

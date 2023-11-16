package com.yitai.service.impl;

import com.yitai.service.FileService;
import com.yitai.utils.KodoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName: FileServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/13 10:42
 * @Version: 1.0
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    KodoUtil kodoUtil;
    @Override
    public String upload(MultipartFile multipartFile, String filename) {
        return kodoUtil.upload(multipartFile, filename);
    }
}

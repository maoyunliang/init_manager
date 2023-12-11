package com.yitai.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.yitai.exception.ServiceException;
import com.yitai.properties.ObjectStoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ClassName: LocalUploadStrategyImpl
 * Package: com.yitai.strategy.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 15:58
 * @Version: 1.0
 */

@Slf4j
@Data
@RequiredArgsConstructor
@Service
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl {
    @Value("${server.port}")
    private Integer port;

    /**
     * 前置路径 ip/域名
     */

    private String prefixUrl;
    /**
     * 构造器注入bean
     */
    private final ObjectStoreProperties properties;

    @Override
    public void initClient() {
        try {
            prefixUrl = ResourceUtils.getURL("classpath:").getPath() + "static/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ServiceException("文件不存在");
        }
        log.info("本地上传初始化完成...");
    }

    @Override
    public boolean checkFileIsExisted(String fileRelativePath) {
        return new File(prefixUrl + fileRelativePath).exists();
    }

    @Override
    public void executeUpload(MultipartFile multipartFile, String fileRelativePath){
        File dest = checkFolderIsExisted(fileRelativePath);
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("文件上传失败");
        }
    }

    @Override
    public String getPublicNetworkAccessUrl(String fileRelativePath) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            if (StrUtil.isEmpty(properties.getLocal().getDomainUrl())) {
                return String.format("http://%s:%d/%s", host, port, fileRelativePath);
            }
            return properties.getLocal().getDomainUrl() + fileRelativePath;
        } catch (UnknownHostException e) {
            throw new ServiceException("文件上传失败");
        }
    }

    /**
     * 检查文件夹是否存在，若不存在则创建文件夹，最终返回上传文件
     *
     * @param fileRelativePath 文件相对路径
     * @return {@link  File} 文件
     */
    private File checkFolderIsExisted(String fileRelativePath) {
        File rootPath = new File(prefixUrl + fileRelativePath);
        if (!rootPath.exists()) {
            if (!rootPath.mkdirs()) {
                throw new ServiceException("文件夹创建失败");
            }
        }
        return rootPath;
    }
}

package com.yitai.strategy.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.yitai.exception.ServiceException;
import com.yitai.properties.ObjectStoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ClassName: KodoUploadStategyImpl
 * Package: com.yitai.strategy.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 14:51
 * @Version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Slf4j
@Service
public class KodoUploadStrategyImpl extends AbstractUploadStrategyImpl{
    /**
     * 构造器注入
     */
    private final ObjectStoreProperties properties;

    /**
     * 上传Manger
     */
    private UploadManager uploadManager;

    /**
     * 存储桶Manger
     */
    private BucketManager bucketManager;
    /**
     * upToken
     */
    private String upToken;


    @Override
    public void initClient() {
        Auth auth = Auth.create(properties.getKodo().getAccesskey(), properties.getKodo().getSecretkey());
        upToken = auth.uploadToken(properties.getKodo().getBucket());
        Configuration cfg = new Configuration(Region.huadongZheJiang2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(cfg);
        bucketManager = new BucketManager(auth, cfg);
        log.info("七牛云对象存储初始化成功...");
    }

    @Override
    public boolean checkFileIsExisted(String fileRelativePath) {
        try {
            if (null == bucketManager.stat(properties.getKodo().getBucket(), fileRelativePath)) {
                return false;
            }
        } catch (QiniuException e) {
            return false;
        }
        return true;
    }

    @Override
    public void executeUpload(MultipartFile multipartFile, String fileRelativePath) {
        try{
            uploadManager.put(multipartFile.getInputStream(), fileRelativePath, upToken, null, null);
        }catch (IOException e){
            log.info("文件上传失败");
            throw new ServiceException("文件上传失败");
        }
    }

    @Override
    public String getPublicNetworkAccessUrl(String fileRelativePath) {
        return properties.getKodo().getDomainUrl() + fileRelativePath;
    }
}

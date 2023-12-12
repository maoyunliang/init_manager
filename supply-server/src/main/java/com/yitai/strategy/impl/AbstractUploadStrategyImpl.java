package com.yitai.strategy.impl;

import com.yitai.exception.ServiceException;
import com.yitai.strategy.UploadStrategy;
import com.yitai.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ClassName: AbstractUploadStrategyImpl
 * Package: com.yitai.strategy.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 14:12
 * @Version: 1.0
 */
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {
    @Override
    public String uploadFile(MultipartFile file, String filePath) {
        try{
            String fileMd5 = FileUtils.getMd5(file.getInputStream());
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            String fileRelativePath = filePath + fileMd5 + extName;
            //region 初始化
            initClient();
            if(!checkFileIsExisted(fileRelativePath)){
                executeUpload(file, fileRelativePath);
            }
            return getPublicNetworkAccessUrl(fileRelativePath);
        }catch (IOException e){
            throw new ServiceException("文件上传失败");
        }
    }

    /**
     * 初始化客户端
     */
    public abstract void initClient();

    /**
     * 检查文件是否已经存在（文件MD5值唯一）
     *
     * @param fileRelativePath 文件相对路径
     * @return true 已经存在  false 不存在
     */
    public abstract boolean checkFileIsExisted(String fileRelativePath);

    public abstract void executeUpload(MultipartFile multipartFile, String fileRelativePath) throws IOException;


    /**
     * 获取公网访问路径
     *
     * @param fileRelativePath 文件相对路径
     * @return 公网访问绝对路径
     */
    public abstract String getPublicNetworkAccessUrl(String fileRelativePath);
}

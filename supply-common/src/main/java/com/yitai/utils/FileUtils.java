package com.yitai.utils;

import cn.hutool.core.util.StrUtil;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * ClassName: FileUtils
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 14:14
 * @Version: 1.0
 */
public class FileUtils {

    /**
     * 获取文件md5值
     *
     * @param inputStream 文件输入流
     * @return {@link String} 文件md5值
     */
    public static String getMd5(InputStream inputStream){
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1){
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encode(md5.digest()));
        }catch (Exception e){
            e.printStackTrace();;
            return null;
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static String getExtName(String fileName){
        if(StrUtil.isBlank(fileName)){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = Objects.requireNonNull(originalFilename).split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2048) {
            accuracy = 0.6;
        } else if (size < 3072) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }

}

package com.yitai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: JwtProperties
 * Package: com.yitai.properties
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 10:49
 * @Version: 1.0
 */

@Component
@ConfigurationProperties(prefix = "yitai.jwt")
@Data
public class JwtProperties {
    /**
     * 用户端生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
}

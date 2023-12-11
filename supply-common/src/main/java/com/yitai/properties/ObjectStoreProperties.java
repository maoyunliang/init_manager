package com.yitai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: ObjectStoreProperties
 * Package: com.yitai.properties
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/11 11:46
 * @Version: 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "yitai.store")
public class ObjectStoreProperties {
    private ConfigEntity kodo;
    private ConfigEntity oss;
    private ConfigEntity local;

    @Data
    public static class ConfigEntity{
        private String accesskey;
        private String secretkey;
        private String domainUrl;
        private String bucket;
        private String endpoint;
    }
}

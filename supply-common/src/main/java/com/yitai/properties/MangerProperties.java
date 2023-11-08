package com.yitai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: MangerProperties
 * Package: com.yitai.properties
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 9:17
 * @Version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "yitai.manger")
@Data
public class MangerProperties {
    /**
     * 管理员账号相关配置
     */
    private List<Long> userId;
    private String Role;
}

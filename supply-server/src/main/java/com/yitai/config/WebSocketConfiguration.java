package com.yitai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * ClassName: WebSocketConfiguration
 * Package: com.yitai.config
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/28 14:59
 * @Version: 1.0
 */
@Configuration
public class WebSocketConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}

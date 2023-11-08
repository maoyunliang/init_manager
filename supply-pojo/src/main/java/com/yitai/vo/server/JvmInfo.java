package com.yitai.vo.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: JvmInfo jvm信息
 * Package: com.yitai.entity.server
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 11:30
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JvmInfo {
    /**
     * 当前JVM占用的堆内存总数(M)
     */
    private String total;

    /**
     * JVM最大可用堆内存总数(M)
     */
    private String max;

    /**
     * JVM空闲内存(M)
     */
    private String free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;
    /**
     * JVM参数
     */
    private List<String> jvmParameter;
}

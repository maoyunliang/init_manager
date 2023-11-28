package com.yitai.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ClassName: KV
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/24 14:28
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface KV {
    // 枚举键
    String k();

    // 枚举值
    String v();
}

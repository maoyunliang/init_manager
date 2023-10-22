package com.yitai.annotation;

import com.yitai.enumeration.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoLog
 * Package: com.yitai.annotation
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/7 15:24
 * @Version: 1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLog {
//    String value() default "";
    String operation();
    LogType type();
}

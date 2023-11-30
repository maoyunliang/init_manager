package com.yitai.annotation.admin;

import com.yitai.enumeration.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoLoginLog
 * Package: com.yitai.annotation
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 11:19
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLog {
    String operation();
    LogType type();
}

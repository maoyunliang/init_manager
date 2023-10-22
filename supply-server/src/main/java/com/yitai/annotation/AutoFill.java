package com.yitai.annotation;

import com.yitai.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoFill
 * Package: com.yitai.annotation
 * Description: 用于标识方法
 *
 * @Author: 毛云亮
 * @Create: 2023/9/26 16:07
 * @Version: 1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    //数据库操作类型： UPDATE INSERT
    OperationType value();
}

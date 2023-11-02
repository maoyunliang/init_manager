package com.yitai.annotation;

import com.yitai.enumeration.ShardType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: TableShard
 * Package: com.yitai.annotation
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 14:22
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShard {
    ShardType type();

    String tableName();
}

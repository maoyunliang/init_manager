package com.yitai.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: ExcelSheet
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/24 14:23
 * @Version: 1.0
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
    // sheet名称
    @AliasFor(value = "name")
    String value() default "";

    @AliasFor(value = "value")
    String name() default "";

    // 是否冻结表头所在行
    boolean freezeHeader() default false;

    // 水印
    Watermark watermark() default @Watermark();

    // 列宽
    int columnWidth() default 15;
}

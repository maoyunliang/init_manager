package com.yitai.annotation.excel;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: ExcelExport
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/24 14:22
 * @Version: 1.0
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    @AliasFor(value = "name")
    String value() default "";

    @AliasFor(value = "value")
    String name() default "";

    // 键值对集合
    KV[] kvs() default {};

    /**
     * 时间样式
     * 当该值无值时，则智能判断（智能判定可能会出错，建议指定格式）
     * 当该值为固定值 ts 时（TimestampSecond），则表示表格中数据为秒级时间戳
     * 当该值为固定值 tms 时（TimestampMillisecond），则表示表格中数据为毫秒级时间戳
     * 当该值为其他值时，则表示表格中数据格式
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    // 列索引（如果有该值大于0，则以该值解析为准，优先级高于value字段）
    int columnIndex() default -1;

    // 批注，如果要换行请用 \r\n 代替
    String comment() default "";

    // 行宽
    int columnWidth() default 0;

}

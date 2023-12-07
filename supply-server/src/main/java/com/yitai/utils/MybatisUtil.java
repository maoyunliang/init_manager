package com.yitai.utils;

import org.apache.ibatis.mapping.MappedStatement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * ClassName: MybatisUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/7 20:35
 * @Version 1.0
 */
public class MybatisUtil {
    public static <T extends Annotation> T getAnnotation(MappedStatement mappedStatement,
                                                         Class<T> annotationClass) throws ClassNotFoundException {
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        final Class<?> cls = Class.forName(className);
        final Method[] methods = cls.getMethods();
        T annotation = null;

        for (Method method : methods) {
            if ((method.getName().equals(methodName) || method.getName().equals(methodName.split("_")[0]))
                    && method.isAnnotationPresent(annotationClass)) {
                annotation = method.getAnnotation(annotationClass);
                break;
            }
        }

        return annotation;
    }
}

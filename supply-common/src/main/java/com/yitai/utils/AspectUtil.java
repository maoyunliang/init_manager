package com.yitai.utils;

import com.yitai.exception.ServiceException;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ClassName: AspectUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/28 10:11
 * @Version: 1.0
 */
public class AspectUtil {
    public static Long getTenantId(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long tenantId;
        if (args[0] instanceof Long) {
            tenantId = (Long) args[0];
        } else {
            tenantId = null;
            Class<?> clazz = args[0].getClass();
            List<Method> methods = new ArrayList<>();
            while (clazz != null) {
                methods.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods())));
                clazz = clazz.getSuperclass();
            }
            Optional<Method> optionalMethod = methods.stream()
                    .filter(method -> method.getName().equals("getTenantId"))
                    .findFirst();
            if (optionalMethod.isPresent()) {
                Method getTenantId = optionalMethod.get();
                if (getTenantId.getReturnType().equals(Long.class)) {
                    try {
                        tenantId = (Long) getTenantId.invoke(args[0]);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ServiceException("传输实体不存在租户ID"); // Handle the exception according to your application's needs
                    }
                }
            }
        }
        return tenantId;
    }
}

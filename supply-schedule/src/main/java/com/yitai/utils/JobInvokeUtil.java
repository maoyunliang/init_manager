package com.yitai.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yitai.quartz.entity.SysJob;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * ClassName: JobInvokeUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/3 17:55
 * @Version: 1.0
 */
public class JobInvokeUtil {
    public static void invokeMethod(SysJob sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();
        //获取调用模板字符串的beanName
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        if(isValidClassName(beanName)){
            Class<?> clazz = Class.forName(beanName);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // 如果构造方法是私有的，设置访问权限为可访问
            Object bean = constructor.newInstance();
            invokeMethod(bean, methodName, methodParams);
        }else {
            Object bean = SpringUtils.getBean(beanName);
            invokeMethod(bean, methodName, methodParams);
        }
    }

    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams) throws Exception {
        if(CollectionUtil.isNotEmpty(methodParams) && methodParams.size()>0){
            Method method = bean.getClass().getMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        }else {
            Method method = bean.getClass().getMethod(methodName);
            method.invoke(method);
        }
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    private static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classes = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams){
            classes[index] = (Class<?>) os[1];
            index++;
        }
        return classes;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classes = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams)
        {
            classes[index] = (Object) os[0];
            index++;
        }
        return classes;
    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget)
    {
        return StrUtil.count(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    private static String getBeanName(String invokeTarget) {
        String beanName = StrUtil.subBefore(invokeTarget,"(", false);
        return StrUtil.subBefore(beanName, ".", true);
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    private static String getMethodName(String invokeTarget) {
        String beanName = StrUtil.subBefore(invokeTarget, "(", false);
        return StrUtil.subAfter(beanName, ".", true);
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    private static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StrUtil.subBetween(invokeTarget,"(",")");
        if(StrUtil.isEmpty(methodStr)){
            return null;
        }
        //这个正则表达式的作用是匹配逗号，并确保逗号后面的引号内的逗号不被作为分割符处理
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> classes = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StrUtil.trimToEmpty(methodParam);
            //如果 以' 或 " 开头，表示参数是字符串类型
            if (StrUtil.startWithAny(str, "'", "\"")) {
                classes.add(new Object[]{
                StrUtil.sub(str, 1, str.length()-1), String.class});
            }else if("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)){
                classes.add(new Object[]{
                        Boolean.valueOf(str), Boolean.class
                });
            }else if (StringUtils.endsWith(str, "L"))
            {
                classes.add(new Object[] {
                        Long.valueOf(StrUtil.sub(str, 0, str.length() - 1)), Long.class });
            }
            // double浮点类型，以D结尾
            else if (StringUtils.endsWith(str, "D"))
            {
                classes.add(new Object[] {
                        Double.valueOf(StrUtil.sub(str, 0, str.length() - 1)), Double.class });
            }
            // 其他类型归类为整形
            else
            {
                classes.add(new Object[] {
                        Integer.valueOf(str), Integer.class });
            }
        }
        return classes;
    }

}

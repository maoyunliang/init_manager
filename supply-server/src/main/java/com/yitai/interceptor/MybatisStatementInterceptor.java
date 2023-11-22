package com.yitai.interceptor;

import com.yitai.annotation.TableShard;
import com.yitai.enumeration.ShardType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * ClassName: MybatisStatementInterceptor
 * Package: com.yitai.interceptor
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 14:36
 * @Version: 1.0
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
@Slf4j
@Component
//@ConditionalOnProperty(value = "table.shard.enabled", havingValue = "true") //加上了table.shard.enabled 该配置才会生效
public class MybatisStatementInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Exception {
        //获取执行参数
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
//        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 获取分表注解
        TableShard tableShard = getTableShard(mappedStatement);
        log.info("=======执行sql语句=======\n{}", boundSql.getSql());
        if(tableShard != null){
            Object parameterObject = boundSql.getParameterObject();
            Long tenantId;
            //改装sql进行分表，直接传递给下一个拦截器处理
            if (parameterObject instanceof HashMap<?,?>){
                tenantId = (Long) ((HashMap<?, ?>) parameterObject).get("tenantId");
            }else {
                tenantId = null;
                Class<?> clazz = parameterObject.getClass();
                List<Method> methods = new ArrayList<>();
                while (clazz != null){
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
                            tenantId = (Long) getTenantId.invoke(parameterObject);
                            // Use the retrieved tenantId...
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace(); // Handle the exception according to your application's needs
                        }
                    } else {
                        // Handle the case where getTenantId method does not return Long
                        throw new Exception("getTenantId method does not return Long");
                    }
                } else {
                    // Handle the case where getTenantId method is not found
                    throw new Exception("getTenantId method not found");
                }
            }
            if(tableShard.type() == ShardType.TABLE){
                rewriteTableSql(boundSql, tenantId);
            }else if(tableShard.type() == ShardType.ID){
                rewriteTableSql(boundSql, tenantId);
            }
        }
        return invocation.proceed();
    }

    /**
     * 添加TenantId字段
     */
    private void rewriteFieldSql(BoundSql boundSql, Long tenantId) throws Exception {
        //TODO 修改sql（添加条件字段）
    }

    /*
     *  重写表结构
     */
    public void rewriteTableSql(BoundSql boundSql, Long tenantId) throws Exception {
        String newSql = boundSql.getSql().replace("_*", "_"+tenantId.toString());
        log.info("=======sql检测到更新=======\n{}", newSql);
        //对 BoundSql 对象通过反射修改 SQL 语句。
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, newSql);
    }

    private TableShard getTableShard(MappedStatement mappedStatement) throws ClassNotFoundException{
        String id = mappedStatement.getId();
        // 获取Class
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf("." )+ 1);
        final Class<?> cls = Class.forName(className);
        final Method[] method = cls.getMethods();
        // 分表注解
        TableShard tableShard = null;
        for (Method me : method) {
            if ((me.getName().equals(methodName) || me.getName().equals(methodName.split("_")[0]))  && me.isAnnotationPresent(TableShard.class)) {
                tableShard = me.getAnnotation(TableShard.class);
            }
        }
        return tableShard;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}




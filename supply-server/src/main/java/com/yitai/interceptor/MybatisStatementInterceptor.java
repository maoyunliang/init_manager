package com.yitai.interceptor;

import com.yitai.annotation.TableShard;
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
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

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
    public Object intercept(Invocation invocation) throws Throwable {
        //获取执行参数
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
//        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 获取分表注解
        TableShard tableShard = getTableShard(mappedStatement);
        // 获取执行的参数
        Object parameterObject = boundSql.getParameterObject();
        log.info("=======执行sql语句=======\n{}", boundSql.getSql());
        if(tableShard != null){
            Long tenantId;
            //改装sql进行分表，直接传递给下一个拦截器处理
            //TODO 反射判断
            if (parameterObject instanceof HashMap<?,?>){
                tenantId = (Long) ((HashMap<?, ?>) parameterObject).get("tenantId");
            }else {
                Method getTenantId = parameterObject.getClass().getDeclaredMethod("getTenantId");
                tenantId = (Long) getTenantId.invoke(parameterObject);
            }
            String newSql = boundSql.getSql().replace(tableShard.tableName(),
                    tableShard.tableName() + "_" + tenantId);
            log.info("=======sql检测到更新=======\n{}", newSql);
            // 8. 对 BoundSql 对象通过反射修改 SQL 语句。
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, newSql);
        }
        //组装sql

        return invocation.proceed();
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




package com.yitai.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.yitai.admin.entity.User;
import com.yitai.annotation.admin.DataScope;
import com.yitai.annotation.admin.TableShard;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.enumeration.ShardType;
import com.yitai.properties.MangerProperties;
import com.yitai.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * ClassName: MybatisStatementInterceptor
 * Package: com.yitai.interceptor
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/2 14:36
 * @Version: 1.0
 */
@Component
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
@Slf4j
public class MybatisStatementInterceptor implements Interceptor {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Object intercept(Invocation invocation) throws Exception {

        //获取执行参数
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 获取分表注解
        TableShard tableShard = getTableShard(mappedStatement);
        DataScope dataScope = getDateScope(mappedStatement);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = getSql(configuration, boundSql, mappedStatement.getId());
        if(tableShard != null && tableShard.type() == ShardType.TABLE){
            Long tenantId = getTenantId(boundSql);
            if(tenantId!= null){
                if(dataScope != null){
                    getDataRange(boundSql,tenantId,dataScope);
                }
                rewriteTableSql(boundSql, tenantId, mappedStatement);
            }
        }else {
            log.info("=======执行sql语句=======\n{}", sql);
        }
        return invocation.proceed();
    }

    /*
     *  重写表结构
     */
    public void rewriteTableSql(BoundSql boundSql, Long tenantId,
                                MappedStatement mappedStatement) throws Exception {
        String newSql = boundSql.getSql().replace("_*", "_"+tenantId.toString());
        //对 BoundSql 对象通过反射修改 SQL 语句。
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, newSql);

        String sqlId = mappedStatement.getId(); // 获取到节点的id,即sql语句的id
        Configuration configuration = mappedStatement.getConfiguration(); // 获取节点的配置
        String sql = getSql(configuration, boundSql, sqlId); // 获取到最终的sql语句
        log.info("=======sql检测到更新=======\n{}", sql);
    }

    private void getDataRange(BoundSql boundSql, Long tenantId, DataScope dataScope) {
        MangerProperties mangerProperties = SpringUtils.getBean(MangerProperties.class);
        User user = BaseContext.getCurrentUser();
        if (!mangerProperties.getUserId().contains(user.getId())){
            log.info("======需要数据权限======");
            String key = RedisConstant.DATASCOPE.concat(user.getId().toString());
            List<Long> deptIds = (List<Long>) redisTemplate.opsForHash().get(key, tenantId.toString());
            if(!CollectionUtil.isEmpty(deptIds)){
                // 使用 String.join() 将 List<Long> 转换为逗号分隔的字符串
                String deptIdsString = deptIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                String params = " " + dataScope.deptAlias()+ ".id in ("+ deptIdsString +") and \n";
                String originSql = boundSql.getSql();
                //统一转换小写，作对比
                int post = originSql.toLowerCase().indexOf("where");
                if(post != -1){
                    StringBuilder modifySql = new StringBuilder(originSql);
                    modifySql.insert(post+ "where".length(), params);
                    String newSql = modifySql.toString();
                    ReflectUtil.setFieldValue(boundSql, "sql", newSql);
                }
            }else {
                throw new MyBatisSystemException(new Throwable("数据权限异常"));
            }
        }else {
            log.info("======超级管理员权限======");
        }
    }

    // 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId)
    {
        String sql = showSql(configuration, boundSql);
        return sqlId +
                ":\n" +
                sql;
    }

    // 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        }
        else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
                    DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        }
        else {
            if (obj != null) {
                value = obj.toString();
            }
            else {
                value = "";
            }

        }
        return value;
    }

    // 进行？的替换
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
//        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        String sql = boundSql.getSql();
        if (CollectionUtil.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));

            }
            else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                    else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                    else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    public static Long getTenantId(BoundSql boundSql){
        Long tenantId = null;
        Object parameterObject = boundSql.getParameterObject();
        //改装sql进行分表，直接传递给下一个拦截器处理
        if (parameterObject instanceof HashMap<?,?>){
            tenantId = (Long) ((HashMap<?, ?>) parameterObject).get("tenantId");
        }else {
            Class<?> clazz = parameterObject.getClass();
            List<Method> methods = new ArrayList<>();
            while (clazz != null){
                methods.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods())));
                clazz = clazz.getSuperclass();
            }
            Optional<Method> optionalMethod = methods.stream()
                    .filter(method -> "getTenantId".equals(method.getName()))
                    .findFirst();
            if (optionalMethod.isPresent()) {
                Method getTenantId = optionalMethod.get();
                if (getTenantId.getReturnType().equals(Long.class)) {
                    try {
                        tenantId = (Long) getTenantId.invoke(parameterObject);
                        // Use the retrieved tenantId...
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception according to your application's needs
                    }
                }
            }
        }
        return tenantId;
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

    private DataScope getDateScope(MappedStatement mappedStatement) throws ClassNotFoundException {
        String id = mappedStatement.getId();
        // 获取Class
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf("." )+ 1);
        final Class<?> cls = Class.forName(className);
        final Method[] method = cls.getMethods();
        // 数据权限注解
        DataScope dataScope = null;
        for (Method me : method) {
            if ((me.getName().equals(methodName) || me.getName().equals(methodName.split("_")[0]))  && me.isAnnotationPresent(TableShard.class)) {
                dataScope = me.getAnnotation(DataScope.class);
            }
        }
        return dataScope;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}




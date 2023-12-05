package com.yitai.config;

import com.yitai.interceptor.MybatisStatementInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: CustomerInterceptorRegister
 * Package: com.yitai.config
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/5 22:17
 * @Version 1.0
 */

/**
 * InterceptorChain 类加载了执行器
 * @author 毛云亮
 */
@Component
public class CustomerInterceptorRegister implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            Configuration configuration = sqlSessionFactory.getConfiguration();
            MybatisStatementInterceptor mybatisStatementInterceptor = new MybatisStatementInterceptor();
//            if (!containsInterceptor(configuration, mybatisStatementInterceptor)) {
//                    configuration.addInterceptor(mybatisStatementInterceptor);
//            }
        }

    }

    /**
     * 是否已经存在相同的拦截器
     *
     * @param configuration 配置类
     * @param interceptor   拦截器
     * @return 是否存在
     */
    private boolean containsInterceptor(Configuration configuration, Interceptor interceptor) {
        try {
            // getInterceptors since 3.2.2
            return configuration.getInterceptors().stream().anyMatch(config
                    -> interceptor.getClass().isAssignableFrom(config.getClass()));
        } catch (Exception e) {
            return false;
        }
    }

}

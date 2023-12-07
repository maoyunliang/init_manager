package com.yitai.config;

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
 * InterceptorChain 类加载执行器 mybatis 拦截器链源码
 * @author 毛云亮
 */
// ioc容器加载处理完相应的bean之后 使用ApplicationListener<ContextRefreshedEvent>监听器注册自定义的Bean
//@Component
//public class CustomerInterceptorRegister implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    private List<SqlSessionFactory> sqlSessionFactoryList;
//
//    @Override
//    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
//        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
//            Configuration configuration = sqlSessionFactory.getConfiguration();
//            MybatisStatementInterceptor mybatisStatementInterceptor = new MybatisStatementInterceptor();
//            if (!containsInterceptor(configuration, mybatisStatementInterceptor)) {
//                    configuration.addInterceptor(mybatisStatementInterceptor);
//            }
//        }
//
//    }
//
//    /**
//     * 是否已经存在相同的拦截器
//     *
//     * @param configuration 配置类
//     * @param interceptor   拦截器
//     * @return 是否存在
//     */
//    private boolean containsInterceptor(Configuration configuration, Interceptor interceptor) {
//        try {
//            // getInterceptors since 3.2.2
//            return configuration.getInterceptors().stream().anyMatch(config
//                    -> interceptor.getClass().isAssignableFrom(config.getClass()));
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}

package com.yitai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
//开启注解方式的事务管理
@EnableTransactionManagement
public class SupplyServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SupplyServerApplication.class, args);
    }

}

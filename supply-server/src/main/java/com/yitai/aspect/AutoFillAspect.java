package com.yitai.aspect;

/*
 * ClassName: AutoFillAspect
 * Package: com.yitai.aspect
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/26 16:17
 * @Version: 1.0
 */

import cn.hutool.core.util.ObjectUtil;
import com.yitai.annotation.AutoFill;
import com.yitai.constant.AutoFillConstant;
import com.yitai.constant.MessageConstant;
import com.yitai.context.BaseContext;
import com.yitai.entity.User;
import com.yitai.enumeration.OperationType;
import com.yitai.exception.NotAuthException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 自定义切面类，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点, 拦截指定的包和文件
     */
    @Pointcut("execution(* com.yitai.mapper.*.*(..)) && @annotation(com.yitai.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充..");
        //获取当前被拦截方法的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        //获得数据库操作类型
        OperationType operationType = autoFill.value();
        //获得被拦截的连接点方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];
        if(entity instanceof Collection<?> collection){
            // 强制转换为 Collection 类型
            for(Object item : collection){
                startFill(item, operationType);
            }
        }else {
            startFill(entity,operationType);
        }

    }
    void startFill(Object entity, OperationType operationType){
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        User user = BaseContext.getCurrentUser();
        if (ObjectUtil.isNull(user)){
            throw new NotAuthException(MessageConstant.TOKEN_NOT_FIND);
        }
        //根据不同的操作类型, 为对应的属性通过反射赋值
        if (operationType == OperationType.INSERT) {
            //为4个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, String.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, String.class);
                Method setIsDel = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_IS_DEL, Integer.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, user.getUsername());
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, user.getUsername());
                setIsDel.invoke(entity, 0);
                log.info("插入公共字段自动填充完成..");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().
                        getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().
                        getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, String.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, user.getUsername());
                log.info("更新公共字段自动填充完成..");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
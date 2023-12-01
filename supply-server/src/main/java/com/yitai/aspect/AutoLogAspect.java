package com.yitai.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.yitai.admin.entity.OperationLog;
import com.yitai.admin.entity.User;
import com.yitai.annotation.admin.AutoLog;
import com.yitai.constant.MessageConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.ServiceException;
import com.yitai.result.Result;
import com.yitai.service.LogService;
import com.yitai.utils.AspectUtil;
import com.yitai.utils.IpUtils;
import com.yitai.utils.SpringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ClassName: AutoLogAspect
 * Package: com.yitai.aspect
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/7 15:28
 * @Version: 1.0
 */

@Aspect
@Component
@Slf4j
public class AutoLogAspect {
    /**
     * 计算操作耗时
     */
    private static final ThreadLocal<StopWatch> TIME_THREADLOCAL = new TransmittableThreadLocal<>();

    @Pointcut("@annotation(com.yitai.annotation.admin.AutoLog)")
    public void autoLogPointCut() {
    }
    /**
     *  日志请求前
     */
    @Before("autoLogPointCut()")
    public void before(JoinPoint joinPoint) {
        StopWatch stopWatch = new StopWatch();
        TIME_THREADLOCAL.set(stopWatch);
        stopWatch.start();
        log.info("----------开始处理日志---------");
    }

    /**
     *  日志请求后
     */
    @AfterReturning(value = "autoLogPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Result<?> jsonResult){
        StopWatch stopWatch = TIME_THREADLOCAL.get();
        stopWatch.stop();
        //获取当前被拦截方法的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoLog autoLog = methodSignature.getMethod().getAnnotation(AutoLog.class);
        //获得数据库操作类型
        //LogType logType = autoLog.type();
        log.info("日志类型:"+ autoLog.type()+ "-> 接口请求结束 -> 本次请求耗时"+ stopWatch.getTotalTimeSeconds());
        User user = BaseContext.getCurrentUser();
        if (user == null){
            log.error("-----------日志处理异常结束---------");
            throw new ServiceException(MessageConstant.LOG_ERROR);
        }
        //获取IP
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }
        String ipAddr = null;
        if (request != null) {
            ipAddr = IpUtils.getIpAddr(request);
        }
        Long tenantId = AspectUtil.getTenantId(joinPoint);
//        String username = (!StrUtil.isBlank(user.getUsername())) ? user.getUsername() : user.getPhone();
        //组装日志的实体对象
        OperationLog logs = OperationLog.builder().user(user.getUsername()).
                operation(autoLog.operation()).
                type(autoLog.type().getValue()).
                ip(ipAddr).duration(stopWatch.getTotalTimeSeconds()).
                time(DateUtil.now()).tenantId(tenantId).build();
        ThreadUtil.execAsync(()-> {
            LogService logService = SpringUtils.getBean(LogService.class);
            logService.save2(logs);
        });
        TIME_THREADLOCAL.remove();
        // 异步的方式擦入数据到数据库
        log.info("-----------日志处理完成---------");
    }

    // around模式
//    @Around("autoLogPointCut()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        log.info("-----------操作日志处理---------");
//        //获取当前被拦截方法的操作类型
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        AutoLog autoLog = methodSignature.getMethod().getAnnotation(AutoLog.class);
//        //获得数据库操作类型
//        LogType logType = autoLog.type();
//        //运行接口
//        Result<?> result = (Result<?>) joinPoint.proceed();
//        Employee employee = BaseContext.getCurrentUser();
//        if (employee == null){
//            //当前用户没有信息的话，就需要从参数里面获取操作人信息
//            Object[] args = joinPoint.getArgs();
//            if (ArrayUtil.isNotEmpty(args)){
//                if(args[0] instanceof Employee){
//                    employee = (Employee) args[0];
//                }
//            }
//        }
//        if (employee == null){
//            log.error("记录日志信息报错");
//            return null;
//        }
//        //获取IP
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)
//                RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = null;
//        if (servletRequestAttributes != null) {
//            request = servletRequestAttributes.getRequest();
//        }
//        String ipAddr = null;
//        if (request != null) {
//            ipAddr = IpUtils.getIpAddr(request);
//        }
//        //组装日志的实体对象
//        Logs logs = Logs.builder().user(employee.getName()).
//                operation(autoLog.operation()).
//                type(autoLog.type().getValue()).
//                ip(ipAddr).time(DateUtil.now()).build();
//        // 异步的方式擦入数据到数据库
//        ThreadUtil.execAsync(()->{
//            logService.save(logs);
//        });
//        log.info("-----------日志处理完成---------");
//        return result;
//    }
}

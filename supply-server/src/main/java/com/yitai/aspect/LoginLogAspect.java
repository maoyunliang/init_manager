package com.yitai.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.yitai.admin.dto.user.LoginMessageDTO;
import com.yitai.admin.dto.user.UserLoginDTO;
import com.yitai.admin.entity.LoginLogs;
import com.yitai.admin.entity.User;
import com.yitai.annotation.admin.LoginLog;
import com.yitai.result.Result;
import com.yitai.service.LogService;
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
 * ClassName: LoginLogAspect
 * Package: com.yitai.aspect
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 11:19
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class LoginLogAspect {

    /**
     * 计算操作耗时
     */
    private static final ThreadLocal<StopWatch> TIME_THREADLOCAL = new TransmittableThreadLocal<>();

    @Pointcut("@annotation(com.yitai.annotation.admin.LoginLog)")
    public void loginLogPointCut() {
    }
    /**
     *  日志请求前
     */
    @Before("loginLogPointCut()")
    public void before(JoinPoint joinPoint) {
        StopWatch stopWatch = new StopWatch();
        TIME_THREADLOCAL.set(stopWatch);
        stopWatch.start();
        log.info("----------开始处理日志---------");
    }

    /**
     *  日志请求后
     */
    @AfterReturning(value = "loginLogPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Result<?> jsonResult){
        StopWatch stopWatch = TIME_THREADLOCAL.get();
        stopWatch.stop();
        //获取当前被拦截方法的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LoginLog loginLog = methodSignature.getMethod().getAnnotation(LoginLog.class);
        log.info("日志类型:"+ loginLog.type()+ "-> 接口请求结束 -> 本次请求耗时"+ stopWatch.getTotalTimeSeconds());
        Object[] args = joinPoint.getArgs();
        User user = new User();
        //当前用户没有信息的话，就需要从参数里面获取操作人信息
        if (ArrayUtil.isNotEmpty(args)){
            if(args[0] instanceof UserLoginDTO userLoginDTO){
                user = User.builder().username(userLoginDTO.getUsername()).build();
            } else if (args[0] instanceof LoginMessageDTO loginMessageDTO){
                user = User.builder().phone(loginMessageDTO.getPhoneNumber()).build();
            }
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
        String username = (!StrUtil.isBlank(user.getUsername())) ? user.getUsername() : user.getPhone();
        //组装日志的实体对象
        LoginLogs logs = LoginLogs.builder().user(username).
                operation(loginLog.operation()).
                type(loginLog.type().getValue()).
                ip(ipAddr).duration(stopWatch.getTotalTimeSeconds()).
                time(DateUtil.now()).build();
        // 异步的方式擦入数据到数据库
        ThreadUtil.execAsync(()->{
            LogService logService = SpringUtils.getBean(LogService.class);
            logService.save1(logs);
        });
        log.info("-----------日志处理完成---------");
    }
}

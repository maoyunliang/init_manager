package com.yitai.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.yitai.annotation.HasPermit;
import com.yitai.constant.MessageConstant;
import com.yitai.context.BaseContext;
import com.yitai.entity.User;
import com.yitai.exception.NotAuthException;
import com.yitai.exception.NotPermissionException;
import com.yitai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: HasPermit
 * Package: com.yitai.aspect
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 10:29
 * @Version: 1.0
 */
@Aspect
@Component
@Slf4j
public class HasPermitAspect {

    @Resource
    UserService userService;

    @Resource
    private RedisTemplate<String, List<String>> redisTemplate;

    @Pointcut("@annotation(com.yitai.annotation.HasPermit)")
    public void hasPermitPointCut() {
    }
    /**
     *  日志请求前
     */
    @Before("hasPermitPointCut()")
    public void before(JoinPoint joinPoint) {
        log.info("----------查看用户权限---------");
        //获取当前被拦截方法的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        HasPermit hasPermit = methodSignature.getMethod().getAnnotation(HasPermit.class);
        String permission = hasPermit.permission();
        if(!hasPermitSession(permission)){
            throw new NotPermissionException(MessageConstant.NOT_PERMISSION);
        }
    }

    private boolean hasPermitSession(String permission) {
        User user = BaseContext.getCurrentUser();
        if(ObjectUtil.isNull(user)){
            throw new NotAuthException(MessageConstant.TOKEN_NOT_FIND);
        }
        permission = mapPermissionToAuthority(permission);
        List<String> permits = redisTemplate.opsForValue().get(user.getId().toString());
        log.info("权限列表：{}", permits);
        return permits != null && permits.contains(permission);
    }

    //admin路径 权限注解转成菜单路由
    private String mapPermissionToAuthority(String permission) {
        // 这里可以编写逻辑将权限字符串映射为权限名称，例如将"sys/user/add"映射为"system:user:add"
        // 实际逻辑根据你的需求和数据结构来实现
        // 这里简单地将"/"替换为":"，然后在前面加上"system:user:add"
        return "/"+permission.replace(":", "/");
    }
}

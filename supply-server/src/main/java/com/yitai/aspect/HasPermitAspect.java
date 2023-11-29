package com.yitai.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yitai.admin.entity.User;
import com.yitai.annotation.admin.HasPermit;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.NotAuthException;
import com.yitai.exception.NotPermissionException;
import com.yitai.properties.MangerProperties;
import com.yitai.service.UserService;
import com.yitai.utils.AspectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MangerProperties mangerProperties;
    @Pointcut("@annotation(com.yitai.annotation.admin.HasPermit)")
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
        User user = BaseContext.getCurrentUser();
        if(ObjectUtil.isNull(user)){
            throw new NotAuthException(MessageConstant.TOKEN_NOT_FIND);
        }
        if (!mangerProperties.getUserId().contains(user.getId())){
            log.info("----------企业用户权限---------");
            String permission = hasPermit.permission();
            Long tenantId = AspectUtil.getTenantId(joinPoint);
            if(!hasPermitSession(permission, tenantId, user.getId())){
                throw new NotPermissionException(MessageConstant.NOT_PERMISSION);
            }
        }else {
            log.info("----------超级用户权限---------");
        }

    }

    private boolean hasPermitSession(String permission, Long tenantId, Long userId) {
//        permission = mapPermissionToAuthority(permission);
        String key = RedisConstant.USER_PERMISSION.concat(userId.toString());
        if (tenantId == null){
            return false;
        }
        List<String> permits = (List<String>) redisTemplate.opsForHash().get(key, tenantId.toString());
        if(CollUtil.isEmpty(permits)){
            permits = userService.getPermiList(userId,tenantId);
        }
        log.info("权限列表：{}", permits);
        return permits != null && permits.contains(permission);
    }

}

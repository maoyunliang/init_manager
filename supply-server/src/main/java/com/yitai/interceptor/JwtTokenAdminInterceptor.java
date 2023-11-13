package com.yitai.interceptor;

import cn.hutool.core.date.DateUtil;
import com.yitai.constant.JwtClaimsConstant;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.entity.User;
import com.yitai.exception.NotAuthException;
import com.yitai.properties.JwtProperties;
import com.yitai.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;


/**
 * ClassName: JwtTokenAdminInterceptor
 * Package: com.yitai.interceptor
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 14:27
 * @Version: 1.0
 */

@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisTemplate redisTemplate;
    /*
      校验jwt
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, HttpServletResponse response, Object handler) {

//        System.out.println("当前线程的id:"+ Thread.currentThread().getId());
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        Claims claims;
        try {
            log.info("----------jwt开始校验----------");
            log.info("{}", token);
            claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            log.info("----------有效期至----------");
            log.info("{}", DateUtil.format(claims.getExpiration(), "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            throw new NotAuthException(MessageConstant.TOKEN_NOT_FIND);
            //response.setStatus(HttpStatusConstant.HTTP_FORBIDDEN);
        }
        log.info("----------生成用户信息上下文----------");
        String userId = claims.get(JwtClaimsConstant.USER_ID).toString();
        String username = (String) claims.get(JwtClaimsConstant.USERNAME);
        String phone = (String) claims.get(JwtClaimsConstant.PHONE);
        String redisToken = (String) redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN.concat(userId));
        if (redisToken == null || !Objects.equals(redisToken, token)) {
            throw new NotAuthException(MessageConstant.ACCOUNT_ELSE);
        }
        User user = User.builder().id(Long.valueOf(userId)).username(username).phone(phone).build();
        BaseContext.setCurrentUser(user);
        //3、通过，放行
        return true;

    }
}

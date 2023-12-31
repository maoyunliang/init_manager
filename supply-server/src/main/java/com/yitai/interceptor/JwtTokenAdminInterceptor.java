package com.yitai.interceptor;

import com.yitai.constant.JwtClaimsConstant;
import com.yitai.constant.MessageConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.properties.JwtProperties;
import com.yitai.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;



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
    private UserMapper userMapper;

    /*
      校验jwt
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        System.out.println("当前线程的id:"+ Thread.currentThread().getId());
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        try {
            log.info("jwt开始校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            long userId = Long.parseLong(claims.get(JwtClaimsConstant.USER_ID).toString());
//            String name = (String) claims.get(JwtClaimsConstant.USERNAME);
            BaseContext.setCurrentUser(userMapper.getById(userId));
//            System.out.println(router);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            log.info("用户认证失败");
            throw new ServiceException(MessageConstant.TOKEN_NOT_FIND);
            //response.setStatus(HttpStatusConstant.HTTP_FORBIDDEN);
        }
    }
}

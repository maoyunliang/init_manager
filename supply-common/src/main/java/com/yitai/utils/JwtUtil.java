package com.yitai.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * ClassName: JwtUtils
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 10:39
 * @Version: 1.0
 */
public class JwtUtil {
//    @PostConstruct
//    public void setUserService(){
//        staticEmployeeMapper = employeeMapper;
//    }

    //生成token
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims){
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    //token解析获取用户名
    public static Claims parseJWT(String secretKey, String token){
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
        return claims;
    }
}

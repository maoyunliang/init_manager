package com.yitai.controller.admin;

import com.yitai.admin.dto.user.LoginMessageDTO;
import com.yitai.admin.dto.user.UserLoginDTO;
import com.yitai.admin.dto.user.UserPasswordDTO;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.admin.vo.*;
import com.yitai.annotation.admin.LoginLog;
import com.yitai.base.BaseBody;
import com.yitai.constant.JwtClaimsConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.context.BaseContext;
import com.yitai.enumeration.LogType;
import com.yitai.properties.JwtProperties;
import com.yitai.result.Result;
import com.yitai.service.UserService;
import com.yitai.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClassName: LoginController
 * Package: com.yitai.controller
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/6 10:53
 * @Version: 1.0
 */
@Tag(name = "用户登录相关接口")
@RestController
@RequestMapping("admin/user")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    @LoginLog(operation = "用户登录", type = LogType.LOGIN)
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        //登录成功后，生成jwt令牌
        UserLoginVO userLoginVO = loginSuccess(user);
        return Result.success(userLoginVO);
    }

    @Operation(summary = "用户短信登录接口")
    @PostMapping ("/loginMessage")
    @LoginLog(operation = "短信登录", type = LogType.LOGIN)
    public Result<UserLoginVO> login(@RequestBody LoginMessageDTO loginMessageDTO){
        log.info("用户短信登录：{}", loginMessageDTO);
        User user = userService.login(loginMessageDTO);
        //登录成功后，生成jwt令牌
        UserLoginVO userLoginVO = loginSuccess(user);
        return Result.success(userLoginVO);
    }

    @Operation(summary = "发送短信验证码接口")
    @PostMapping ("/sendMsg/{phoneNumber}")
    public Result<?> sendMsg(@PathVariable String phoneNumber){
        log.info("发送短信验证码：{}", phoneNumber);
        if(userService.sendMsg(phoneNumber)){
            return Result.success();
        }else{
            return Result.error("发送失败");
        }

    }

    @Operation(summary = "获取关联租户")
    @PostMapping ("/getTenant")
    public Result<?> getTenant(){
        log.info("获取关联租户");
        List<Tenant> tenantList = userService.getTenant();
        List<TenantVO> list = tenantList.stream().map(tenant -> {
            TenantVO tenantVO = new TenantVO();
            BeanUtils.copyProperties(tenant, tenantVO);
            return tenantVO;
        }).collect(Collectors.toList());
        return Result.success(list);
    }

    @Operation(summary = "获取用户信息")
    @PostMapping("/getInfo")
    public Result<UserVO> getInfo(@RequestBody BaseBody baseBody){
        log.info("获取用户信息：" );
        UserVO userVO = userService.getInfo(baseBody.getTenantId());
        return Result.success(userVO);
    }
    @Operation(summary = "获取关联菜单和权限相关信息")
    @PostMapping ("/getRouter")
    public Result<?> getRouter(@RequestBody BaseBody baseBody){
        log.info("获取关联菜单和权限相关信息");
        User user = BaseContext.getCurrentUser();
        Long tenantId = baseBody.getTenantId();
        List<MenuVO> menuVOS = userService.getRouter(user.getId(), tenantId);
        List<String> permiList = userService.getPermiList(user.getId(), tenantId);
        // 获取数据权限至缓存
        userService.hasScopeRange(user.getId(), tenantId);
        return Result.success(RouterVO.builder().id(user.getId())
                .tenantId(tenantId)
                .menuVOS(menuVOS)
                .permiList(permiList)
                .build());
    }

    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public Result<String> logout(){
        log.info("用户退出");
        userService.logOut();
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PostMapping ("/modify")
    public Result<?> modify(@RequestBody UserPasswordDTO userPasswordDTO){
        log.info("修改密码{}", userPasswordDTO);
        userService.modifyPassword(userPasswordDTO);
        return Result.success();
    }

    public UserLoginVO loginSuccess(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        claims.put(JwtClaimsConstant.PHONE, user.getPhone());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        String key = RedisConstant.USER_LOGIN.concat(user.getId().toString());
        redisTemplate.opsForValue().set(key, token, 7, TimeUnit.DAYS);
        return UserLoginVO.builder().id(user.getId())
                .username(user.getUsername())
                .realname(user.getRealname())
                .token(token)
                .build();
    }
}

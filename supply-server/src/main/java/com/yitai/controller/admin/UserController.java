package com.yitai.controller.admin;

import com.yitai.annotation.AutoLog;
import com.yitai.annotation.HasPermit;
import com.yitai.constant.JwtClaimsConstant;
import com.yitai.dto.sys.UserDTO;
import com.yitai.dto.sys.UserLoginDTO;
import com.yitai.dto.sys.UserPageQueryDTO;
import com.yitai.dto.sys.UserRoleDTO;
import com.yitai.entity.User;
import com.yitai.enumeration.LogType;
import com.yitai.properties.JwtProperties;
import com.yitai.result.PageResult;
import com.yitai.result.Result;
import com.yitai.service.UserService;
import com.yitai.utils.JwtUtil;
import com.yitai.vo.MenuVO;
import com.yitai.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: EmployeeController
 * Package: com.yitai.controller.admin
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:00
 * @Version: 1.0
 */


@Tag(name = "用户管理相关接口")
@RestController
@RequestMapping("admin/sys/user")
@Slf4j
public class UserController<userId> {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @Operation(summary = "用户登录接口")
    @PostMapping ("/login")
    @AutoLog(operation = "用户登录", type = LogType.LOGIN)
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USERNAME, user.getUsername());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        // 获取路由时，将菜单存在缓存中
        ArrayList<MenuVO> menuVOS = userService.getRouter(user.getId());
        List<String> permiList = userService.getPermiList(user.getId());
        //TODO 单点登录用redis实现（redis中的哈希map数据类型隔离生产测试环境） 相同情况下，商家营业状态通过redis存储效率更高！
        UserLoginVO userLoginVO = UserLoginVO.builder().id(user.getId())
                .username(user.getUsername())
                .realname(user.getRealname())
                .token(token).menuVOS(menuVOS)
                .permiList(permiList)
                .build();
        return Result.success(userLoginVO);
    }


    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public Result<String> logout(){
        log.info("用户退出");
        userService.logOut();
        return Result.success();
    }

    @Operation(summary = "新增用户接口")
    @PostMapping("/add")
    @HasPermit(permission = "sys:user:add")
    @AutoLog(operation = "新增用户接口", type = LogType.ADD)
    public Result<String> save(@RequestBody UserDTO userDTO){
        log.info("新增用户:{}", userDTO);
        userService.save(userDTO);
        return Result.success();
    }

    @Operation(summary = "用户分页查询")
    @PostMapping("/page")
    public Result<PageResult> page(@ModelAttribute UserPageQueryDTO userPageQueryDTO){
        log.info("分页查询:{}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "账号启用或停用")
    @PostMapping("/status/{status}")
    public Result<?> startOrStop(@PathVariable Integer status, Long id){
        log.info("启用或禁用账号：{}， {}", status,id);
        userService.startOrStop(status,id);
        return Result.success();
    }

    @Operation(summary = "根据id查询用户信息")
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id){
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Operation(summary = "编辑用户信息")
    @PostMapping("/update")
    @HasPermit(permission = "sys:user:update")
    @AutoLog(operation = "编辑用户信息", type = LogType.UPDATE)
    public Result<Void> update(@RequestBody UserDTO userDTO){
        log.info("编辑用户信息：{}", userDTO);
        userService.update(userDTO);
        return Result.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/getInfo")
    public Result<User> getInfo(){
        log.info("获取用户信息：" );
        User user = userService.getInfo();
        return Result.success(user);
    }

    @Operation(summary = "分配角色")
    @PostMapping ("/assRole")
    public Result<?> assRole(@RequestBody UserRoleDTO userRoleDTO){
        log.info("分配角色：{}", userRoleDTO);
        userService.assRole(userRoleDTO);
        return Result.success();
    }
}

package com.yitai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.PasswordConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.constant.StatusConstant;
import com.yitai.context.BaseContext;
import com.yitai.dto.user.*;
import com.yitai.entity.Tenant;
import com.yitai.entity.User;
import com.yitai.entity.UserRole;
import com.yitai.entity.UserTenant;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.result.PageResult;
import com.yitai.service.UserService;
import com.yitai.utils.SendMsgUtil;
import com.yitai.utils.TreeUtil;
import com.yitai.utils.VerifyCodeUtil;
import com.yitai.vo.MenuVO;
import com.yitai.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClassName: EmployeeServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:24
 * @Version: 1.0
 */

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MangerProperties mangerProperties;
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        //1. 根据用户名查询数据库中的数据
        User user = userMapper.getByUsername(username);

        if (user == null){
            throw new ServiceException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())){
            throw new ServiceException(MessageConstant.PASSWORD_ERROR);
        }

        if (user.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new ServiceException(MessageConstant.ACCOUNT_LOCKED);
        }
        return user;
    }

    @Override
    public User login(LoginMessageDTO loginMessageDTO) {
        String phoneNumber = loginMessageDTO.getPhoneNumber();
        String key = RedisConstant.VERIFY_CODE.concat(phoneNumber);
        //查询数据库是否有手机号
        User user = userMapper.getByPhone(phoneNumber);
        if (ObjectUtil.isNull(user)){
            throw new ServiceException("该手机号不存在，请确认!");
        }
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
            throw new ServiceException("短信验证码失效，请重新获取!");
        }
        String real_verifyCode = (String) redisTemplate.opsForValue().get(phoneNumber+"-MSG");
        String verifyCode = loginMessageDTO.getVerifyCode();
        if(!verifyCode.equals(real_verifyCode)){
            throw new ServiceException("短信验证码错误!");
        }
        return user;
    }

    @Override
    public boolean sendMsg(String phoneNumber) {
        User user = userMapper.getByPhone(phoneNumber);
        if (ObjectUtil.isNull(user)){
            throw new ServiceException("该手机号不存在，请确认!");
        }
        String verifyCode = VerifyCodeUtil.generateCode();
        // 发送短信
        if(SendMsgUtil.sendMsg(phoneNumber, verifyCode)){
            String key = RedisConstant.VERIFY_CODE.concat(phoneNumber);
            redisTemplate.opsForValue().set(key, verifyCode, 300, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public List<Tenant> getTenant() {
        User user = BaseContext.getCurrentUser();
        List<Tenant> list = mangerProperties.getUserId().contains(user.getId()) ? userMapper.
                getAllTenant() : userMapper.getTenant(user.getId());
        if(CollUtil.isEmpty(list)){
            throw new ServiceException("没有找到关联租户");
        }
        return list;
    }

    @Override
    public void save(UserDTO userDTO) {
        if(StrUtil.isBlank(userDTO.getUsername())) {
            throw new ServiceException("请输入正确的账号");
        }
        User user = new User();
        //对象属性拷贝
        BeanUtils.copyProperties(userDTO, user);
        //设置账号的状态
        user.setStatus(StatusConstant.ENABLE);
        //设置默认密码
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        int records = userMapper.insert(user);
        //关联相关租户
        userMapper.insertUserTenant(UserTenant.builder().userId(user.getId()).
                tenantId(userDTO.getTenantId()).build());
        //TODO 关联相关角色
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<UserVO> page = userMapper.pageQuery(userPageQueryDTO);
        long total = page.getTotal();
        List<UserVO> records = page.getResult();
//        for (User record : records) {
//            record.setPassword("**********");
//        }
        return new PageResult(total,records);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        User user = User.builder().status(status).id(id).build();
//        System.out.println(employee);
        //update
        userMapper.update(user);
    }

    @Override
    public User getById(Long id) {
        User user = userMapper.getById(id);
        if(ObjectUtil.isEmpty(user)){
            throw new ServiceException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
//        user.setPassword("******");
        return user;
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        //属性拷贝
        BeanUtils.copyProperties(userDTO,user);
        //update
        userMapper.update(user);
    }

    @Override
    public User getInfo() {
        User user = BaseContext.getCurrentUser();
        user = userMapper.getById(user.getId());
        if (ObjectUtil.isNull(user)){
            throw  new ServiceException(MessageConstant.TOKEN_NOT_FIND);
        }
//        user.setPassword("******");
        return user;
    }

    @Override
    public ArrayList<MenuVO> getRouter(Long id){
        List<String> typeList = new ArrayList<>();
        typeList.add("M");
        typeList.add("C");
        List<MenuVO> menuList = mangerProperties.getUserId().contains(id) ? userMapper.
                pageAllMenu(typeList) : userMapper.pageMenu(id, typeList);
        //自定义方法的建立树结构 (state 表示顶层父ID的设定标准 只支持int类型)
//        return TreeUtil.buildTree(menuList, 0, MenuVO::getMenuPid);
        return TreeUtil.buildTree(menuList, MenuVO::getMenuPid);
    }

    @Override
    public List<String> getPermiList(Long id) {
        List<MenuVO> menuList = mangerProperties.getUserId().contains(id) ? userMapper.
                pageAllMenu(Collections.singletonList("B")) : userMapper.pageMenu(id, Collections.singletonList("B"));
        List<String> permissionList = menuList.stream().map(MenuVO::getIdentify).collect(Collectors.toList());
        permissionList = permissionList.stream().filter(permission -> permission.contains(":")).collect(Collectors.toList());
        String key = RedisConstant.USER_PERMISSION.concat(id.toString());
        redisTemplate.opsForValue().set(key, permissionList);
        return permissionList;
    }

    @Override
    public void logOut() {
        User user = BaseContext.getCurrentUser();
        String userId = user.getId().toString();
        redisTemplate.delete(RedisConstant.USER_LOGIN.concat(userId));
        redisTemplate.delete(RedisConstant.USER_PERMISSION.concat(userId));
    }

    @Override
    public void assRole(UserRoleDTO userRoleDTO) {
//        UserRole userRole = new UserRole();
        //1、for 循环批量插入
//        for (Long roleId : userRoleDTO.getRoleIds()) {
//            userRole.setUserId(userRoleDTO.getUserId());
//            userRole.setRoleId(roleId);
//            userMapper.assRole(userRole);
//        }
        //2、 mybatis批量擦入
        List<UserRole> userRoles = new ArrayList<>();
        Long userId = userRoleDTO.getUserId();
        for (Long roleId : userRoleDTO.getRoleIds()) {
            userRoles.add(UserRole.builder().roleId(roleId).userId(userId).build());
        }
//        //Lambda 表达式写法
//        userRoleDTO.getRoleIds().forEach(roleId ->{
//            userRoles.add(UserRole.builder().roleId(roleId).userId(userId).build());
//        });
        userMapper.assRole(userRoles, userRoleDTO.getTenantId());
        //2、原生批量插入分片实现（解决sql拼接造成语句过大）
    }



}

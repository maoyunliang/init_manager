package com.yitai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.admin.dto.user.*;
import com.yitai.admin.entity.*;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.PasswordConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.constant.StatusConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.NotScopeRangeException;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.result.PageResult;
import com.yitai.service.UserService;
import com.yitai.utils.SendMsgUtil;
import com.yitai.utils.TreeUtil;
import com.yitai.utils.VerifyCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
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
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MangerProperties mangerProperties;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        //1. 根据用户名查询数据库中的数据
        User user =  userMapper.getByUsername(username);

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
        String real_verifyCode = (String) redisTemplate.opsForValue().get(key);
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
    public void logOut() {
        User user = BaseContext.getCurrentUser();
        String userId = user.getId().toString();
        redisTemplate.delete(RedisConstant.USER_LOGIN.concat(userId));
        redisTemplate.delete(RedisConstant.USER_PERMISSION.concat(userId));
        redisTemplate.delete(RedisConstant.DATASCOPE.concat(userId));
    }

    @Override
    public void modifyPassword(UserPasswordDTO userPasswordDTO) {
        User user = BaseContext.getCurrentUser();
        User origin = userMapper.getById(user.getId());
        String originPassword = origin.getPassword();
        String inputPassword = DigestUtils.md5DigestAsHex(userPasswordDTO.getOldPassword().getBytes());
        //校验密码
        if(!inputPassword.equals(originPassword)){
            throw new ServiceException("请输入正确的旧密码");
        }
        origin.setPassword(DigestUtils.md5DigestAsHex(userPasswordDTO.getNewPassword().getBytes()));
        userMapper.update(origin);
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
    public ArrayList<MenuVO> getRouter(Long id, Long tenantId){
        List<String> typeList = Arrays.asList("M", "C");
        List<MenuVO> menuList = mangerProperties.getUserId().contains(id) ? userMapper.
                pageAllMenu(typeList) : userMapper.pageMenu(id, typeList, tenantId).stream()
                .filter(e-> e.getVisible() == 1 ).collect(Collectors.toList());
        return TreeUtil.buildTree(menuList, MenuVO::getMenuPid, MenuVO::getSortNo);
    }

    @Override
    public UserVO getInfo(Long tenantId) {
        User user = BaseContext.getCurrentUser();
        if (ObjectUtil.isNull(user)){
            throw new ServiceException(MessageConstant.TOKEN_NOT_FIND);
        }
        UserVO userVO = new UserVO();
        if(!mangerProperties.getUserId().contains(user.getId())) {
            userVO = userMapper.getInfo(user.getId(), tenantId);
        }else {
            User user1 = userMapper.getById(user.getId());
            BeanUtils.copyProperties(user1, userVO);
            userVO.setDepartments(List.of("所有部门"));
            userVO.setRoles(List.of("超级管理员"));
        }
        return userVO;
    }

    @Override
    public List<String> getPermiList(Long id, Long tenantId) {
        List<MenuVO> menuList = mangerProperties.getUserId().contains(id) ? userMapper.
                pageAllMenu(Collections.singletonList("B")) : userMapper.
                pageMenu(id, Collections.singletonList("B"), tenantId)
                .stream().filter(e -> e.getVisible() == 1).toList();
        List<String> permissionList = menuList.stream().map(MenuVO::getIdentify)
                .filter(permission -> permission.contains(":")).collect(Collectors.toList());
        String key = RedisConstant.USER_PERMISSION.concat(id.toString());
//        map.put(tenantId, permissionList);
        redisTemplate.opsForHash().put(key, tenantId.toString(), permissionList);
        return permissionList;
    }

    /*
     * 查看用户的数据权限
     */
    public void hasScopeRange(Long userId, Long tenantId){
        List<Long> deptIds = mangerProperties.getUserId().contains(userId) ?
                null : userMapper.hasScopeRange(userId, tenantId);
        String key = RedisConstant.DATASCOPE.concat(userId.toString());
        if(deptIds != null){
            if(CollectionUtil.isEmpty(deptIds)){
                throw new NotScopeRangeException("没有开通数据权限");
            }
            redisTemplate.opsForHash().put(key, tenantId.toString(), deptIds);
        }
    }



    @Override
    public List<UserVO> list(Long tenantId, List<Long> idList) {
        return userMapper.list(tenantId, idList);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<UserVO> page = userMapper.pageQuery(userPageQueryDTO);
        long total = page.getTotal();
        List<UserVO> records = page.getResult();
        return new PageResult(total,records);
    }


    @Override
    public void save(UserDTO userDTO) {
        if(StrUtil.isBlank(userDTO.getUsername())) {
            throw new ServiceException("请输入正确的账号");
        }
        if(!ObjectUtil.isNull(userMapper.getByUsername(userDTO.getUsername()))){
            throw new ServiceException("账号已存在");
        }
        if(!ObjectUtil.isNull(userMapper.getByPhone(userDTO.getPhone()))){
            throw new ServiceException("手机号已绑定");
        }
        //账号、手机号不能重名
        User user = new User();
        //对象属性拷贝
        BeanUtils.copyProperties(userDTO, user);
//        设置账号的状态
//        user.setStatus(StatusConstant.ENABLE);
        //设置默认头像
        user.setAvatar("http://172.30.117.161:8855/assets/c6d0bcb248708a2577369d6ccb5a410b.jpg");
        //设置默认密码
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        int records = userMapper.insert(user);
        if(records > 0){
            //关联相关租户
            userMapper.insertUserTenant(UserTenant.builder().userId(user.getId()).
                    tenantId(userDTO.getTenantId()).build());
        }
        //关联相关部门
        if(!CollectionUtil.isEmpty(userDTO.getDeptIds())){
            List<UserDepartment> userDepartments = userDTO.getDeptIds().stream()
                    .map(e -> UserDepartment.builder()
                            .deptId(e).userId(user.getId())
                            .build()).collect(Collectors.toList());
            userMapper.insertUserDept(userDepartments, userDTO.getTenantId());
        }
        //关联相关角色
        if(!CollectionUtil.isEmpty(userDTO.getRoleIds())){
            List<UserRole> userRoles = userDTO.getRoleIds().stream()
                    .map(roleId -> UserRole.builder()
                            .roleId(roleId).userId(user.getId())
                            .build()).collect(Collectors.toList());
            userMapper.insertUserRole(userRoles, userDTO.getTenantId());
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        User user = User.builder().status(status).id(id).build();
        //update
        userMapper.update(user);
    }

    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        //属性拷贝
        BeanUtils.copyProperties(userDTO, user);
        //校验手机号是否已存在
        User checkUser = userMapper.getByPhone(userDTO.getPhone());
        if(checkUser != null && !Objects.equals(checkUser.getId(), user.getId())){
            throw new ServiceException("手机号以被注册");
        }
        //关联相关部门
        if(!CollectionUtil.isEmpty(userDTO.getDeptIds())){
            userMapper.emptyDept(userDTO.getId(), userDTO.getTenantId());
            List<UserDepartment> userDepartments = userDTO.getDeptIds().stream()
                    .map(e -> UserDepartment.builder()
                            .deptId(e).userId(user.getId())
                            .build()).collect(Collectors.toList());
            userMapper.insertUserDept(userDepartments, userDTO.getTenantId());
        }
        //关联相关角色
        if(!CollectionUtil.isEmpty(userDTO.getRoleIds())){
            userMapper.emptyRole(userDTO.getId(), userDTO.getTenantId());
            List<UserRole> userRoles = userDTO.getRoleIds().stream()
                    .map(roleId -> UserRole.builder()
                            .roleId(roleId).userId(user.getId())
                            .build()).collect(Collectors.toList());
            userMapper.insertUserRole(userRoles, userDTO.getTenantId());
        }
        //update
        userMapper.update(user);
        //用户修改之后，删除相关缓存权限
        redisTemplate.delete(RedisConstant.DATASCOPE.concat(user.getId().toString()));
        redisTemplate.delete(RedisConstant.USER_PERMISSION.concat(user.getId().toString()));
    }

    @Override
    public void assRole(UserRoleDTO userRoleDTO) {
        //1、 mybatis批量擦入
        //Lambda 表达式写法
        if(!CollectionUtil.isEmpty(userRoleDTO.getRoleIds())) {
            Long userId = userRoleDTO.getUserId();
            List<UserRole> userRoleList = userRoleDTO.getRoleIds()
                    .stream()
                    .map(roleId -> UserRole.builder().roleId(roleId).userId(userId).build()).toList();
            userMapper.assRole(userRoleList, userRoleDTO.getTenantId());
        }
    }

    @Override
    public void saveBatch(List<UserDTO> userDTOS) {

    }

    @Override
    public void delete(UserDTO userDTO) {
        //1.删除用户
        userMapper.deleteById(userDTO.getId());
        //2.删除关联部门、删除关联角色
        userMapper.emptyDept(userDTO.getId(), userDTO.getTenantId());
        userMapper.emptyRole(userDTO.getId(), userDTO.getTenantId());
        redisTemplate.delete(RedisConstant.USER_LOGIN.concat(userDTO.getId().toString()));
        redisTemplate.delete(RedisConstant.DATASCOPE.concat(userDTO.getId().toString()));
        redisTemplate.delete(RedisConstant.USER_PERMISSION.concat(userDTO.getId().toString()));
    }
}

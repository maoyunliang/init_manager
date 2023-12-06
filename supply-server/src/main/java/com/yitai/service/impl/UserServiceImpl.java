package com.yitai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.admin.dto.user.*;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.admin.entity.UserRole;
import com.yitai.admin.entity.UserTenant;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    public List<UserVO> list(Long tenantId) {
        return userMapper.listAll(tenantId);
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
        //账号、手机号不能重名
        if(checkUser(userDTO)) {
            User user = new User();
            //对象属性拷贝
            BeanUtils.copyProperties(userDTO, user);
            //设置账号的状态
            user.setStatus(StatusConstant.ENABLE);
            //设置默认密码
            user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
            int records = userMapper.insert(user);
            if(records > 0){
                //关联相关租户
                userMapper.insertUserTenant(UserTenant.builder().userId(user.getId()).
                        tenantId(userDTO.getTenantId()).build());
            }
            //TODO 关联相关角色
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
        if(checkUser(userDTO)){
            User user = new User();
            //属性拷贝
            BeanUtils.copyProperties(userDTO,user);
            //update
            userMapper.update(user);
        }
    }

    @Override
    public UserVO getInfo(Long tenantId) {
        User user = BaseContext.getCurrentUser();
        user = userMapper.getById(user.getId());
        if (ObjectUtil.isNull(user)){
            throw  new ServiceException(MessageConstant.TOKEN_NOT_FIND);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        List<String> roleName = userMapper.getRole(user.getId(), tenantId);
        userVO.setRoles(roleName);
//        user.setPassword("******");
        return userVO;
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
    public boolean checkUser(UserDTO userDTO){
        if(!ObjectUtil.isNull(userMapper.getByUsername(userDTO.getUsername()))){
            throw new ServiceException("账号已存在");
        }
        if(!ObjectUtil.isNull(userMapper.getByPhone(userDTO.getPhone()))){
            throw new ServiceException("手机号已绑定");
        }
        return true;
    }

    @Override
    public void saveBatch(List<UserDTO> userDTOS) {

    }


}

package com.yitai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yitai.admin.dto.user.LoginMessageDTO;
import com.yitai.admin.dto.user.UserLoginDTO;
import com.yitai.admin.dto.user.UserPasswordDTO;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.constant.StatusConstant;
import com.yitai.context.BaseContext;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.service.LoginService;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ClassName: LoginServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/8 17:23
 * @Version: 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MangerProperties mangerProperties;
    @Autowired
    UserMapper userMapper;
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
}

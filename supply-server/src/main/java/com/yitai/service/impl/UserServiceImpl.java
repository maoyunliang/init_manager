package com.yitai.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.constant.MessageConstant;
import com.yitai.constant.PasswordConstant;
import com.yitai.constant.StatusConstant;
import com.yitai.context.BaseContext;
import com.yitai.dto.UserDTO;
import com.yitai.dto.UserLoginDTO;
import com.yitai.dto.UserPageQueryDTO;
import com.yitai.entity.User;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.result.PageResult;
import com.yitai.service.UserService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
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
    private RedisTemplate<String, List<String>> redisTemplate;
    @Autowired
    UserMapper userMapper;
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

        userMapper.insert(user);
    }

    @Override
    public PageResult pageQuery(UserPageQueryDTO userPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<User> page = userMapper.pageQuery(userPageQueryDTO);
        long total = page.getTotal();
        List<User> records = page.getResult();
        for (User record : records) {
            record.setPassword("**********");
        }
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
        user.setPassword("******");
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
        if (ObjectUtil.isNull(user)){
            throw  new ServiceException(MessageConstant.TOKEN_NOT_FIND);
        }
        user.setPassword("******");
        return user;
    }

    @Override
    public ArrayList<MenuVO> getRouter(Long id){
//        User user = BaseContext.getCurrentUser();
//        if (ObjectUtil.isNull(user)){
//            throw  new ServiceException(MessageConstant.TOKEN_NOT_FIND);
//        }
        List<MenuVO> menuList = userMapper.pageMenu(id);
        List<String> permessionList = menuList.stream().map(MenuVO::getMenuPath).collect(Collectors.toList());
        redisTemplate.opsForValue().set(id.toString(), permessionList);
        //自定义方法的建立树结构 (state 表示顶层父ID的设定标准 只支持int类型)
        return TreeUtil.buildTree(menuList, 0, MenuVO::getMenuPid);
    }

    @Override
    public void logOut() {
        User user = BaseContext.getCurrentUser();
        redisTemplate.delete(user.getId().toString());
    }
}

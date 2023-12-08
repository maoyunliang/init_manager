package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.admin.dto.user.UserDTO;
import com.yitai.admin.dto.user.UserPageQueryDTO;
import com.yitai.admin.dto.user.UserRoleDTO;
import com.yitai.admin.entity.User;
import com.yitai.admin.entity.UserDepartment;
import com.yitai.admin.entity.UserRole;
import com.yitai.admin.entity.UserTenant;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.constant.PasswordConstant;
import com.yitai.constant.RedisConstant;
import com.yitai.constant.StatusConstant;
import com.yitai.exception.NotScopeRangeException;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.UserMapper;
import com.yitai.properties.MangerProperties;
import com.yitai.result.PageResult;
import com.yitai.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        redisTemplate.opsForHash().delete(RedisConstant.DATASCOPE.concat(user.getId().toString()));
        redisTemplate.opsForHash().delete(RedisConstant.USER_PERMISSION.concat(user.getId().toString()));
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

    @Override
    public void saveBatch(List<UserDTO> userDTOS) {

    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}

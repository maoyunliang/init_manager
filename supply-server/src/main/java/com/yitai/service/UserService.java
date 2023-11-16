package com.yitai.service;

import com.yitai.dto.user.*;
import com.yitai.entity.Tenant;
import com.yitai.entity.User;
import com.yitai.result.PageResult;
import com.yitai.vo.MenuVO;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: EmployeeService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:09
 * @Version: 1.0
 */
public interface UserService {
    User login(UserLoginDTO userLoginDTO);

    User login(LoginMessageDTO loginMessageDTO);

    void save(UserDTO userDTO);

    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    void startOrStop(Integer status, Long id);

    User getById(Long id);

    void update(UserDTO userDTO);

    User getInfo();

    ArrayList<MenuVO> getRouter(Long id);

    void logOut();

    void assRole(UserRoleDTO userRoleDTO);

    List<String> getPermiList(Long id);

    boolean sendMsg(String phoneNumber);

    List<Tenant> getTenant();

    void modifyPassword(UserPasswordDTO userPasswordDTO);
}

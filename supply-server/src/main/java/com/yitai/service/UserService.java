package com.yitai.service;

import com.yitai.dto.UserDTO;
import com.yitai.dto.UserLoginDTO;
import com.yitai.dto.UserPageQueryDTO;
import com.yitai.entity.User;
import com.yitai.result.PageResult;
import com.yitai.vo.MenuVO;

import java.util.ArrayList;

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

    void save(UserDTO userDTO);

    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    void startOrStop(Integer status, Long id);

    User getById(Long id);

    void update(UserDTO userDTO);

    User getInfo();

    ArrayList<MenuVO> getRouter(Long id);

    void logOut();
}

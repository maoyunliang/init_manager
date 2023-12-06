package com.yitai.service;

import com.yitai.admin.dto.user.*;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.result.PageResult;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;

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

    UserVO getInfo(Long tenantId);

    void logOut();

    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    List<UserVO> list(Long tenantId);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void startOrStop(Integer status, Long id);

    ArrayList<MenuVO> getRouter(Long id, Long tenantId);

    void assRole(UserRoleDTO userRoleDTO);

    List<String> getPermiList(Long id, Long tenantId);

    boolean sendMsg(String phoneNumber);

    List<Tenant> getTenant();

    void modifyPassword(UserPasswordDTO userPasswordDTO);


    void saveBatch(List<UserDTO> userDTOS);

    void hasScopeRange(Long id, Long tenantId);
}

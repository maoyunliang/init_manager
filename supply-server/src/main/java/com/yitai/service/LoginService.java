package com.yitai.service;

import com.yitai.admin.dto.user.LoginMessageDTO;
import com.yitai.admin.dto.user.UserLoginDTO;
import com.yitai.admin.dto.user.UserPasswordDTO;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: LoginService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/8 17:23
 * @Version: 1.0
 */
public interface LoginService {
    User login(UserLoginDTO userLoginDTO);

    User login(LoginMessageDTO loginMessageDTO);

    UserVO getInfo(Long tenantId);

    void logOut();

    boolean sendMsg(String phoneNumber);

    void modifyPassword(UserPasswordDTO userPasswordDTO);

    ArrayList<MenuVO> getRouter(Long id, Long tenantId);

    List<Tenant> getTenant();

    List<String> getPermiList(Long id, Long tenantId);

    void hasScopeRange(Long id, Long tenantId);
}

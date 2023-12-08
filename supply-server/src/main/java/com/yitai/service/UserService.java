package com.yitai.service;

import com.yitai.admin.dto.user.UserDTO;
import com.yitai.admin.dto.user.UserPageQueryDTO;
import com.yitai.admin.dto.user.UserRoleDTO;
import com.yitai.admin.vo.UserVO;
import com.yitai.result.PageResult;

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

    PageResult pageQuery(UserPageQueryDTO userPageQueryDTO);

    List<UserVO> list(Long tenantId);

    void save(UserDTO userDTO);

    void update(UserDTO userDTO);

    void startOrStop(Integer status, Long id);

    void assRole(UserRoleDTO userRoleDTO);

    void saveBatch(List<UserDTO> userDTOS);

    void delete(Long id);
}

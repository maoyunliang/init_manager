package com.yitai.dto;

import lombok.Data;

/**
 * ClassName: EmployeePageQueryDTO
 * Package: com.yitai.dto
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/26 8:59
 * @Version: 1.0
 */
@Data
public class UserPageQueryDTO {
    private String username;
    private int page =1 ;
    private int pageSize =10;
}

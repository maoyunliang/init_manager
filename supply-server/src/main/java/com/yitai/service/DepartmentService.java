package com.yitai.service;

import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.vo.DepartmentVO;

import java.util.List;

/**
 * ClassName: DepartmentService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:32
 * @Version: 1.0
 */
public interface DepartmentService {
    List<DepartmentVO> list(DepartmentListDTO departmentListDTO);

    void save(DepartmentDTO departmentDTO);

    void update(DepartmentDTO departmentDTO);

    void delete(DepartmentDTO deleteDepartmentDTO);

    List<DepartmentVO> getUserByTree(Long tenantId);
}

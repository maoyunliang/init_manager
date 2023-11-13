package com.yitai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.yitai.dto.department.DeleteDepartmentDTO;
import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.entity.Department;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.DepartmentMapper;
import com.yitai.service.DepartmentService;
import com.yitai.utils.TreeUtil;
import com.yitai.vo.DepartmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: DepartmentServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:33
 * @Version: 1.0
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;
    @Override
    public List<DepartmentVO> list(DepartmentListDTO departmentListDTO) {
        List<DepartmentVO> departmentVOS = departmentMapper.list(departmentListDTO);
        return TreeUtil.buildTree(departmentVOS, DepartmentVO::getPid);
    }

    @Override
    public void save(DepartmentDTO departmentDTO) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        departmentMapper.save(department, departmentDTO.getTenantId());
    }

    @Override
    public void update(DepartmentDTO departmentDTO) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        departmentMapper.update(department, departmentDTO.getTenantId());
    }

    @Override
    public void delete(DeleteDepartmentDTO deleteDepartmentDTO) {
        //查询当前部门是否有子部门
        List<Department> departments = departmentMapper.containChildren(deleteDepartmentDTO);
        if(!CollectionUtil.isEmpty(departments)){
            throw new ServiceException("当前菜单存在子菜单，无法删除");
        }
        departmentMapper.deleteById(deleteDepartmentDTO);
    }
}

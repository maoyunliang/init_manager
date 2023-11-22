package com.yitai.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.dto.department.DepartmentUserDTO;
import com.yitai.entity.Department;
import com.yitai.exception.ServiceException;
import com.yitai.mapper.DepartmentMapper;
import com.yitai.service.DepartmentService;
import com.yitai.vo.DepartmentVO;
import com.yitai.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return departmentMapper.list(departmentListDTO);
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
    public void delete(DepartmentDTO deleteDepartmentDTO) {
        //查询当前部门是否有子部门
        List<Department> departments = departmentMapper.containChildren(deleteDepartmentDTO);
        if(!CollectionUtil.isEmpty(departments)){
            throw new ServiceException("当前菜单存在子菜单，无法删除");
        }
        departmentMapper.deleteById(deleteDepartmentDTO);
    }

    @Override
    public List<DepartmentVO> getUserByTree(Long tenantId) {
        List<DepartmentVO> departmentVOS = departmentMapper.deptList(tenantId);
        //查看部门下的人员和人员信息id
        List<DepartmentUserDTO> result =departmentMapper.getDeptUser(tenantId);
        Map<Long, List<DepartmentUserDTO>> usermap = result.stream().
                collect(Collectors.groupingBy(DepartmentUserDTO::getDeptId));
        Map<Long, List<UserVO>> newMap = new HashMap<>();
        usermap.forEach((key, value) -> {
            List<UserVO> users = BeanUtil.copyToList(value, UserVO.class);
            newMap.put(key, users);
        });
        departmentVOS.forEach(e -> {
            if (newMap.containsKey(e.getId())){
                e.setUsers(newMap.get(e.getId()));
            }}
        );
        return departmentVOS;
    }
}

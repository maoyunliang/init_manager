package com.yitai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yitai.dto.tenant.TenantDTO;
import com.yitai.dto.tenant.TenantListDTO;
import com.yitai.entity.Department;
import com.yitai.entity.Tenant;
import com.yitai.mapper.TenantMapper;
import com.yitai.result.PageResult;
import com.yitai.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: TenantServiceImpl
 * Package: com.yitai.service.impl
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/21 16:08
 * @Version: 1.0
 */
@Service
@Slf4j
public class TenantServiceImpl implements TenantService {
    @Autowired
    TenantMapper tenantMapper;

    @Override
    public PageResult page(TenantListDTO tenantListDTO) {
        PageHelper.startPage(tenantListDTO.getPage(), tenantListDTO.getPageSize());
        Page<Tenant> page = tenantMapper.pageQuery(tenantListDTO);
        long total = page.getTotal();
        List<Tenant> records = page.getResult();
        return new PageResult(total, records);
    }

    @Override
    public void save(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        int records = tenantMapper.save(tenant);
        Department department = Department.builder()
                .departmentName(tenant.getTenantName())
                .status(1)
                .build();
        //新建父部门
        tenantMapper.insertDept(department, tenant.getId());
    }

    @Override
    public void update(TenantDTO tenantDTO) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDTO, tenant);
        tenantMapper.update(tenant);
    }

    @Override
    public void delete(Long tenantId) {
        tenantMapper.delete(tenantId);
    }
}

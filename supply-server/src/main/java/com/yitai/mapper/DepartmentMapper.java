package com.yitai.mapper;

import com.yitai.annotation.AutoFill;
import com.yitai.annotation.TableShard;
import com.yitai.dto.department.DepartmentDTO;
import com.yitai.dto.department.DepartmentListDTO;
import com.yitai.dto.department.DepartmentUserDTO;
import com.yitai.entity.Department;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import com.yitai.vo.DepartmentVO;
import com.yitai.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: DepartMentMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/10 11:33
 * @Version: 1.0
 */
@Mapper
public interface DepartmentMapper {

    @TableShard(type = ShardType.TABLE)
    List<DepartmentVO> list(DepartmentListDTO departmentListDTO);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    int save(@Param("department") Department department, @Param("tenantId") Long tenantId);

    @AutoFill(value = OperationType.UPDATE)
    @TableShard(type = ShardType.TABLE)
    void update(@Param("department") Department department, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<Department> containChildren(DepartmentDTO deleteDepartmentDTO);

    @TableShard(type = ShardType.TABLE)
    void deleteById(DepartmentDTO deleteDepartmentDTO);

    @TableShard(type = ShardType.TABLE)
    List<DepartmentVO> deptList(@Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<DepartmentUserDTO> getDeptUser(@Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<UserVO> containsUser(DepartmentDTO deleteDepartmentDTO);
}

package com.yitai.mapper;


import com.github.pagehelper.Page;
import com.yitai.admin.dto.role.RolePageQueryDTO;
import com.yitai.admin.entity.MenuRole;
import com.yitai.admin.entity.Role;
import com.yitai.admin.entity.RoleDepartment;
import com.yitai.admin.entity.UserRole;
import com.yitai.admin.vo.DepartmentVO;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.annotation.admin.AutoFill;
import com.yitai.annotation.admin.DataScope;
import com.yitai.annotation.admin.TableShard;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: RoleMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 10:42
 * @Version: 1.0
 */
@Mapper
public interface RoleMapper {

    @TableShard(type = ShardType.TABLE)
    @DataScope(deptAlias = "rd", value = "dept_id")
    Page<Role> pageQuery(RolePageQueryDTO pageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    int save(@Param("role") Role role, @Param("tenantId") Long tenantId);


    @TableShard(type = ShardType.TABLE)
    void deleteById(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @AutoFill(value = OperationType.UPDATE)
    @TableShard(type = ShardType.TABLE)
    void update(Role role, @Param("tenantId") Long tenantId);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    void assMenu(@Param("list") List<MenuRole> menuRoles, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    Role getRoleById(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<MenuVO> selectByRoleId(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<DepartmentVO> selectDeptByRoleId(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<UserVO> selectUserByRoleId(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.INSERT)
    void assUser(@Param("list") List<UserRole> userRoles, @Param("tenantId") Long tenantId);

    List<MenuVO> listMenu();

    @TableShard(type = ShardType.TABLE)
    void emptyMenu(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    void emptyUser(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    void emptyDept(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    @DataScope(deptAlias = "d")
    List<DepartmentVO> listDepartment(@Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    @AutoFill(value = OperationType.INSERT)
    void assDept(@Param("list") List<RoleDepartment> roleDepartments, @Param("tenantId") Long tenantId);

}

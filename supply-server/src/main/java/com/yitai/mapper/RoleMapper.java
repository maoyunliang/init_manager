package com.yitai.mapper;


import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.annotation.TableShard;
import com.yitai.dto.role.DeleteRoleDTO;
import com.yitai.dto.role.RolePageQueryDTO;
import com.yitai.entity.MenuRole;
import com.yitai.entity.Role;
import com.yitai.entity.UserRole;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import com.yitai.vo.MenuVO;
import com.yitai.vo.UserVO;
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
    Page<Role> pageQuery(RolePageQueryDTO pageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    int save(@Param("role") Role role, @Param("tenantId") Long tenantId);


    @TableShard(type = ShardType.TABLE)
    void deleteById(DeleteRoleDTO deleteRoleDTO);

    @AutoFill(value = OperationType.UPDATE)
    @TableShard(type = ShardType.TABLE)
    void update(Role role, @Param("tenantId") Long tenantId);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    void assMenu(@Param("list") List<MenuRole> menuRoles, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<MenuVO> selectByRoleId(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    void deleteMenuById(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    Role getRoleById(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<UserVO> selectUserByRoleId(@Param("roleId") Long roleId, @Param("tenantId") Long tenantId);

    void assUser(@Param("list") List<UserRole> userRoles, @Param("tenantId") Long tenantId);

    List<MenuVO> listMenu();

    List<UserVO> list(Long tenantId);
}

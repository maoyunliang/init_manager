package com.yitai.mapper;


import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.annotation.TableShard;
import com.yitai.dto.sys.RolePageQueryDTO;
import com.yitai.entity.MenuRole;
import com.yitai.entity.Role;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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

    @TableShard(tableName = "role_*", type = ShardType.TABLE)
    Page<Role> pageQuery(RolePageQueryDTO pageQueryDTO);

//    @TableShard(tableName = "role", type = ShardType.TENANT_ID)
    List<Role> Query(RolePageQueryDTO pageQueryDTO);

    @Insert("insert into role_*(id, role_name, role_type, role_desc," +
            "create_time, update_time, create_user, update_user, is_del) values (#{role.id}," +
            "#{role.roleName},#{role.roleType}, #{role.roleDesc}, #{role.createTime}, #{role.updateTime}," +
            " #{role.createUser}, #{role.updateUser}, #{role.isDel})")
    @AutoFill(value = OperationType.INSERT)
    @TableShard(tableName = "role_*", type = ShardType.TABLE)
    void save(@Param("role") Role role, @Param("tenantId") Long tenantId);


    @Update("UPDATE role_1 set is_del = 1 where id = #{roleId}")
    void deleteById(Integer roleId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Role role);

    @AutoFill(value = OperationType.INSERT)
    void assMenu(@Param("list") List<MenuRole> menuRoles);
}

package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.admin.dto.user.UserPageQueryDTO;
import com.yitai.admin.entity.Tenant;
import com.yitai.admin.entity.User;
import com.yitai.admin.entity.UserRole;
import com.yitai.admin.entity.UserTenant;
import com.yitai.admin.vo.MenuVO;
import com.yitai.admin.vo.UserVO;
import com.yitai.annotation.admin.AutoFill;
import com.yitai.annotation.admin.DataScope;
import com.yitai.annotation.admin.TableShard;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: EmployeeMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:28
 * @Version: 1.0
 */

@Mapper
public interface UserMapper {
    @Select("select * from public_user where username = #{username} and is_del =0")
    User getByUsername(String username);

    @AutoFill(value = OperationType.INSERT)
    int insert(User user);

    @AutoFill(value = OperationType.INSERT)
    void insertUserTenant(UserTenant userTenant);
    /*
    分页查询
     */

    @TableShard(type = ShardType.TABLE)
    @DataScope(deptAlias = "d")
    Page<UserVO> pageQuery(UserPageQueryDTO userPageQueryDTO);

    /*
    更新employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(User user);

    @Select("select * from public_user where id = #{id} and is_del = 0")
    User getById(Long id);

    @TableShard(type = ShardType.TABLE)
    List<MenuVO> pageMenu(@Param("id") Long id,
                          @Param("typeList") List<String> typeList, @Param("tenantId") Long tenantId);

    List<MenuVO> pageAllMenu(List<String> typeList);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    void assRole(@Param("list") List<UserRole> userRoleList, @Param("tenantId") Long tenantId);

    @Select("select * from public_user where phone = #{phoneNumber} and is_del = 0")
    User getByPhone(String phoneNumber);

    List<Tenant> getTenant(Long id);

    @Select("select * from public_tenant where is_del = 0")
    List<Tenant> getAllTenant();

    @TableShard(type = ShardType.TABLE)
    List<String> getRole(@Param("id") Long id,  @Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<UserVO> listAll(@Param("tenantId") Long tenantId);

    @TableShard(type = ShardType.TABLE)
    List<Long> hasScopeRange(@Param("id") Long id,  @Param("tenantId") Long tenantId);
}

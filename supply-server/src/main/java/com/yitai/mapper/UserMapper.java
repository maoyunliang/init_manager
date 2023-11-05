package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.annotation.TableShard;
import com.yitai.dto.sys.UserPageQueryDTO;
import com.yitai.entity.Tenant;
import com.yitai.entity.User;
import com.yitai.entity.UserRole;
import com.yitai.entity.UserTenant;
import com.yitai.enumeration.OperationType;
import com.yitai.enumeration.ShardType;
import com.yitai.vo.MenuVO;
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

    //@TableShard(tableName = "role", type = ShardType.ID)
    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);


    /*
    更新employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(User user);

    @Select("select * from public_user where id = #{id} and is_del = 0")
    User getById(Long id);

    List<MenuVO> pageMenu(@Param("id") Long id, @Param("typeList") List<String> typeList);

    List<MenuVO> pageAllMenu(List<String> typeList);

    @AutoFill(value = OperationType.INSERT)
    @TableShard(type = ShardType.TABLE)
    void assRole(@Param("list") List<UserRole> userRoleList, @Param("tenantId") Long tenantId);

    @Select("select * from public_user where phone = #{phoneNumber} and is_del = 0")
    User getByPhone(String phoneNumber);

    List<Tenant> getTenant(Long id);

    @Select("select * from public_tenant where is_del = 0")
    List<Tenant> getAllTenant();
}

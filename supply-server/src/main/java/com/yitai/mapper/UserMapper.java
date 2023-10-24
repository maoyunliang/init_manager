package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.dto.sys.UserPageQueryDTO;
import com.yitai.entity.User;
import com.yitai.entity.UserRole;
import com.yitai.enumeration.OperationType;
import com.yitai.vo.MenuVO;
import org.apache.ibatis.annotations.Insert;
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
    @Select("select * from order_user where username = #{username} and is_del =0")
    User getByUsername(String username);

    @Insert("insert into order_user(id, username, openid, realname, password, phone, email, sex, id_number," +
            " status, avatar, create_time, update_time, create_user, update_user, is_del) values (#{id}," +
            "#{username},#{openid}, #{realname}, #{password}, #{phone}, #{email}, #{sex}, #{idNumber}, #{status}, #{avatar}," +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{isDel})")
    @AutoFill(value = OperationType.INSERT)
    void insert(User user);

    /*
    分页查询
     */

    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);


    /*
    更新employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(User user);

    @Select("select * from order_user where id = #{id} and is_del = 0")
    User getById(Long id);

    List<MenuVO> pageMenu(@Param("id") Long id, @Param("typeList") List<String> typeList);

    @AutoFill(value = OperationType.INSERT)
    void assRole(List<UserRole> userRoles);
}

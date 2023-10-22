package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.dto.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.entity.MenuRole;
import com.yitai.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * ClassName: MenuMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/19 17:33
 * @Version: 1.0
 */
@Mapper
public interface MenuMapper {
    Page<Menu> pageQuery(MenuPageQueryDTO pageQueryDTO);

    @Insert("insert into order_menu(id, menu_name, menu_path, menu_type, menu_pid, sort_no," +
            "create_time, update_time, create_user, update_user, is_del) values (#{id}," +
            "#{menuName},#{menuPath}, #{menuType}, #{menuPid}, #{sortNo},#{createTime}, #{updateTime}," +
            " #{createUser}, #{updateUser}, #{isDel})")
    @AutoFill(value = OperationType.INSERT)
    void save(Menu menu);


    @Update("UPDATE order_menu set is_del = 1 where id = #{menuId}")
    void deleteById(Integer menuId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Menu menu);

    @AutoFill(value = OperationType.INSERT)
    void givePermit(MenuRole menuRole);
}

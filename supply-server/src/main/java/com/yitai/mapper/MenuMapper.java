package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.dto.sys.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.entity.MenuRole;
import com.yitai.enumeration.OperationType;
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

    @AutoFill(value = OperationType.INSERT)
    int save(Menu menu);

    @Update("UPDATE order_menu set is_del = 1 where id = #{menuId}")
    void deleteById(Integer menuId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Menu menu);

    @AutoFill(value = OperationType.INSERT)
    void givePermit(MenuRole menuRole);
}

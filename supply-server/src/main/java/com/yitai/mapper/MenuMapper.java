package com.yitai.mapper;

import com.yitai.annotation.admin.AutoFill;
import com.yitai.admin.dto.menu.MenuDTO;
import com.yitai.admin.entity.Menu;
import com.yitai.enumeration.OperationType;
import com.yitai.admin.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
//    Page<Menu> pageQuery(MenuPageQueryDTO pageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    int save(Menu menu);

    @Update("UPDATE public_menu set is_del = 1 where id = #{menuId}")
    void deleteById(Long menuId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Menu menu);

    List<MenuVO> list(MenuDTO menuListDTO);

    List<Menu> containChildren(Long menuId);

    void delete();

//    @AutoFill(value = OperationType.INSERT)
//    void givePermit(MenuRole menuRole);
}

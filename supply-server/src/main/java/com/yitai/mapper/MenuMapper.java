package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.AutoFill;
import com.yitai.dto.menu.MenuListDTO;
import com.yitai.dto.menu.MenuPageQueryDTO;
import com.yitai.entity.Menu;
import com.yitai.enumeration.OperationType;
import com.yitai.vo.MenuVO;
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
    Page<Menu> pageQuery(MenuPageQueryDTO pageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    int save(Menu menu);

    @Update("UPDATE public_menu set is_del = 1 where id = #{menuId}")
    void deleteById(Long menuId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Menu menu);

    List<MenuVO> list(MenuListDTO menuListDTO);

    List<Menu> containChildren(Long menuId);

//    @AutoFill(value = OperationType.INSERT)
//    void givePermit(MenuRole menuRole);
}

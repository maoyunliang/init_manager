package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.dto.sys.LogPageQueryDTO;
import com.yitai.entity.Logs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: LogMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 14:46
 * @Version: 1.0
 */
@Mapper
public interface LogMapper {
    Page<Logs> pageQuery(LogPageQueryDTO logPageQueryDTO);
    @Delete("delete from order_logs where id = #{id}")
    void removeById(Integer id);
    void removeBatchIds(List<Integer> ids);
    void save(Logs logs);
}

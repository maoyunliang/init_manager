package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.dto.sys.LogPageQueryDTO;
import com.yitai.entity.LoginLogs;
import com.yitai.entity.OperationLog;
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
    /**
     * 操作日志相关处理
     */
    Page<OperationLog> pageQuery(LogPageQueryDTO logPageQueryDTO);
    @Delete("delete from public_logs where id = #{id}")
    void removeById(Integer id);
    void removeBatchIds(List<Integer> ids);
    void save2(OperationLog logs);

    /**
     * 登录日志相关处理
     */
    void save1(LoginLogs logs);
    Page<LoginLogs> pageQuery1(LogPageQueryDTO logPageQueryDTO);
    @Delete("delete from public_login_logs where id = #{id}")
    void removeById1(Integer id);
    void removeBatchIds1(List<Integer> ids);
}

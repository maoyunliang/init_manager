package com.yitai.mapper;

import com.yitai.quartz.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: JobLogMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 19:30
 * @Version 1.0
 */
@Mapper
public interface JobLogMapper {
    int save(@Param("sysJobLog") SysJobLog sysJobLog);
}

package com.yitai.mapper;

import com.yitai.quartz.dto.JobDTO;
import com.yitai.quartz.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: JobMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/29 14:31
 * @Version: 1.0
 */
@Mapper
public interface JobMapper {
    List<SysJob> page(JobDTO jobDTO);
}

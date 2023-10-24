package com.yitai.service;

import com.yitai.dto.sys.LogPageQueryDTO;
import com.yitai.entity.Logs;
import com.yitai.result.PageResult;

import java.util.List;

/**
 * ClassName: LogService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/8 13:22
 * @Version: 1.0
 */

public interface LogService {

    PageResult pageQuery(LogPageQueryDTO logPageQueryDTO);

    void removeById(Integer id);

    void removeBatchIds(List<Integer> ids);

    void save(Logs logs);
}

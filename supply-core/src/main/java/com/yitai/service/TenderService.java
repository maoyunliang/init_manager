package com.yitai.service;

import com.yitai.core.dto.TenderDTO;
import com.yitai.core.vo.TenderVO;
import com.yitai.result.PageResult;

import java.util.List;

/**
 * ClassName: TenderService
 * Package: com.yitai.service
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 11:45
 * @Version: 1.0
 */
public interface TenderService {
    PageResult page(TenderDTO tenantDTO);

    List<TenderVO> list(Long tenantId, List<Long> idList);
}

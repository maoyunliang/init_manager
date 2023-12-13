package com.yitai.mapper;

import com.github.pagehelper.Page;
import com.yitai.annotation.admin.TableShard;
import com.yitai.core.dto.TenderDTO;
import com.yitai.core.vo.TenderVO;
import com.yitai.enumeration.ShardType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: TenderMapper
 * Package: com.yitai.mapper
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/12/13 11:56
 * @Version: 1.0
 */

@Mapper
public interface TenderMapper {

    @TableShard(type = ShardType.TABLE)
    Page<TenderVO> page(TenderDTO tenderDTO);

    @TableShard(type = ShardType.TABLE)
    List<TenderVO> list(@Param("tenantId") Long tenantId, @Param("list") List<Long> idList);
}

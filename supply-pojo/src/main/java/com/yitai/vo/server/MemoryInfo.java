package com.yitai.vo.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: MemoryInfo 内存信息
 * Package: com.yitai.entity.server
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/7 11:31
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemoryInfo {
    /**
     * 内存总量
     */
    private String total;

    /**
     * 已用内存
     */
    private String used;

    /**
     * 剩余内存
     */
    private String free;
}

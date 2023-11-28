package com.yitai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: ServerVO
 * Package: com.yitai.vo.server
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/8 9:28
 * @Version: 1.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerVO {
    private CpuInfo cpuInfo;
    private JvmInfo jvmInfo;
    private MemoryInfo memoryInfo;
    private List<SysFile> sysFiles;
    private SystemDetails systemDetails;
}

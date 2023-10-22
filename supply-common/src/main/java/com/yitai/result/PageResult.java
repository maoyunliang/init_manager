package com.yitai.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: PageResult
 * Package: com.yitai.result
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/26 9:01
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {
    private long total;
    private List records;
}

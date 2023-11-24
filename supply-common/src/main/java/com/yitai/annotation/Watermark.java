package com.yitai.annotation;

import com.yitai.utils.ExcelUtils;

/**
 * ClassName: Watermark
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/24 14:18
 * @Version: 1.0
 */
public @interface Watermark {
    /**
     * 1-文本文字水印;
     * 2-本地图片水印;
     * 3-网络图片水印；
     * 更多查看 {@link ExcelUtils.WatermarkType#code}
     */
    int type() default 0;

    /**
     * 当type=1时：src为文字内容，如：xxx科技有效公司
     * 当type=2时：src为本地文件路径，如：D:\img\icon.png
     * 当type=3时：src为网络图片水印，如：https://profile-avatar.csdnimg.cn/624a0ef43e224cb2bf5ffbcca1211e51_sunnyzyq.jpg
     */
    String src() default "";
}

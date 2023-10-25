package com.yitai.utils;

import com.yitai.constant.HttpStatusConstant;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * ClassName: WebUtils
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/18 16:32
 * @Version: 1.0
 */
public class WebUtil {
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(HttpStatusConstant.SUCCESS);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

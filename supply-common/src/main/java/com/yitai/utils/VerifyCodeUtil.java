package com.yitai.utils;

import java.util.Random;

/**
 * ClassName: VerifyCodeUtil
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/30 14:25
 * @Version: 1.0
 */
public class VerifyCodeUtil {
    private static final Random random = new Random();

    public static String generateCode6(){
        return Integer.toString(100000 + random.nextInt(900000));
    }

    public static String generateCode(){
        return Integer.toString(1000 + random.nextInt(9000));
    }
}

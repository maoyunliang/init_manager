package com.yitai.exception;


/**
 * ClassName: NotPermissionException
 * Package: com.yitai.exception
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/16 16:55
 * @Version: 1.0
 */
public class NotPermissionException extends RuntimeException {

    public NotPermissionException(String msg){
        super(msg);
    }
}

package com.yitai.exception;

/**
 * ClassName: NotAuthException
 * Package: com.yitai.exception
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 17:31
 * @Version: 1.0
 */
public class NotAuthException extends RuntimeException{
    public NotAuthException(String msg){
        super(msg);
    }
}

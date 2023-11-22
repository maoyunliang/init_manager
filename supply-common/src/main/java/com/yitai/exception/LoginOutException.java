package com.yitai.exception;

/**
 * ClassName: LoginException
 * Package: com.yitai.exception
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/11/22 16:54
 * @Version: 1.0
 */
public class LoginOutException extends RuntimeException{
    public LoginOutException(String msg){
        super(msg);
    }
}

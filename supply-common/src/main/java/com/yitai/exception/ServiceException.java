package com.yitai.exception;

/**
 * ClassName: AccountLockedException
 * Package: com.yitai.exception
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 9:53
 * @Version: 1.0
 */
public class ServiceException extends RuntimeException{
    public ServiceException() {
    }

    public ServiceException(String msg){
        super(msg);
    }
}

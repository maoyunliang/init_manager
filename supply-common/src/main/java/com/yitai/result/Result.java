package com.yitai.result;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: result
 * Package: com.yitai
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 10:32
 * @Version: 1.0
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;


    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        return result;
    }

    public static <T> Result<T> success(T object){
        Result<T> result = new Result<>();
        result.code = 200;
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 500;
        return result;
    }

    public static <T> Result<T> error(String msg, Integer code){
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        return result;
    }
}

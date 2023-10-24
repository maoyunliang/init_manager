package com.yitai.handler;

import com.yitai.constant.HttpStatusConstant;
import com.yitai.exception.NotAuthException;
import com.yitai.exception.NotPermissionException;
import com.yitai.exception.ServiceException;
import com.yitai.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * ClassName: GlobalExceptionHandler
 * Package: com.yitai.handler
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 16:31
 * @Version: 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
     * 认证异常
     */
    @ExceptionHandler(NotAuthException.class)
    public Result<?> exceptionHandler(NotAuthException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.NOT_AUTH);
    }

    /*
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<?> exceptionHandler(NotPermissionException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.HTTP_FORBIDDEN);
    }

    /*
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> exceptionHandler(ServiceException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 业务异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.SERVICE_ERROR);
    }

    /*
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 发生系统异常：",requestURI, ex);
        return Result.error(ex.getMessage(), HttpStatusConstant.SYS_ERROR);
    }
}

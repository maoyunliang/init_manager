package com.yitai.handler;

import com.yitai.constant.HttpStatusConstant;
import com.yitai.exception.*;
import com.yitai.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;


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
     * 强制退出异常
     */
    @ExceptionHandler(LoginOutException.class)
    public Result<?> exceptionHandler(LoginOutException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 认证异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.AUTH_EXIT);
    }

    /*
     * 认证异常
     */
    @ExceptionHandler(NotAuthException.class)
    public Result<?> exceptionHandler(NotAuthException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 认证异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.NOT_AUTH);
    }

    /*
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<?> exceptionHandler(NotPermissionException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 权限异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error(ex.getMessage(), HttpStatusConstant.HTTP_FORBIDDEN);
    }

    /*
     * 权限码异常
     */
    @ExceptionHandler(NotScopeRangeException.class)
    public Result<?> exceptionHandler(NotScopeRangeException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 数据权限异常：'{}'",requestURI, ex.getMessage());
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
     * 文件上传异常
     */
    @ExceptionHandler(MultipartException.class)
    public Result<?> exceptionHandler(MultipartException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 文件上传异常信息：'{}'",requestURI, ex.getMessage());
        return Result.error("文件上传不得大于2MB", HttpStatusConstant.SERVICE_ERROR);
    }
    /*
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 发生系统异常：",requestURI, ex);
        return Result.error("未知异常", HttpStatusConstant.SYS_ERROR);
    }

    @ExceptionHandler(MyBatisSystemException.class)
    public Result<?> exceptionHandler(MyBatisSystemException ex, HttpServletRequest request){
        String requestURI = request.getRequestURI();
        log.error("请求地址:'{}', 发生Mybatis异常：",requestURI, ex);
        return Result.error("Mybatis异常", HttpStatusConstant.SYS_ERROR);
    }
}

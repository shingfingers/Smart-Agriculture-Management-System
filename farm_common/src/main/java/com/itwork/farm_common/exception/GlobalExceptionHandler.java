package com.itwork.farm_common.exception;

import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_common.result.CodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//统一异常处理器

//全局统一处理异常的注解,集中处理所有 Controller 层抛出的异常
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理系统异常
    @ExceptionHandler(Exception.class)
    public BaseResult defaultExceptionHandle(HttpServletRequest req, HttpServletResponse resp, Exception e){
        e.printStackTrace();
        return BaseResult.error(CodeEnum.SYSTEM_ERROR,e.getMessage());
    }
    //处理业务异常
    @ExceptionHandler(BusException.class)
    public BaseResult handleBusException(BusException e){
        e.printStackTrace();
        return BaseResult.error(e.getCodeEnum(),e.getMessage());
    }

    //权限不足处理异常,抓到异常后再抛出
    @ExceptionHandler(AccessDeniedException.class)
    public void defaultExceptionHandle(AccessDeniedException e)throws AccessDeniedException{
        throw e;

    }
}

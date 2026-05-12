package com.itwork.farm_system.security;

import com.alibaba.fastjson2.JSON;
import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_common.result.CodeEnum;
import com.itwork.farm_system.service.LoginInfoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 登录失败处理器
@Component
public class MyLoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private LoginInfoService loginInfoService;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        BaseResult result = null;
        // 验证码异常
        if (exception instanceof InvalidCaptchaException){
            BusException exception1 = (BusException) request.getAttribute("captchaError");
            CodeEnum codeEnum = exception1.getCodeEnum();
            result = new BaseResult(codeEnum.getCode(), codeEnum.getMessage(), null);
        }else {
            result = new BaseResult(402, "用户名或密码错误", null);
        }

        // 记录登录失败日志
        loginInfoService.recordLoginInfo("unknown", "1", result.getMessage(), request);

        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }


}

package com.itwork.farm_system.security;

import com.alibaba.fastjson2.JSON;
import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_system.service.LoginInfoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
//登录成功
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private LoginInfoService loginInfoService;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 记录登录成功日志
        String userName = authentication.getName();
        loginInfoService.recordLoginInfo(userName, "0", "登录成功", request);
        // 获取用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 生成JWT令牌
        String token = jwtUtil.generateToken(userDetails);
        //给前端返回JSON字符串
    BaseResult result=new BaseResult(200,"登录成功",token);
    response.setContentType("text/json;charset=utf-8");
    response.getWriter().write(JSON.toJSONString(result));
    }
}

package com.itwork.farm_system.security;

import com.itwork.farm_common.exception.BusException;
import com.itwork.farm_common.result.CodeEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 登录验证码过滤器
 * OncePerRequestFilter是 Spring 框架提供的一个过滤器类，旨在确保在一次完整的 HTTP 请求中，
 * 无论请求经过多少次内部转发（如服务器内部转发或异步请求），过滤器的逻辑仅执行一次
 */
@Component
@EnableWebSecurity
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MyLoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 只拦截登录请求
        if ("/user/login".equals(request.getRequestURI())) {
            try {
                // 获取请求参数
                String sessionId = request.getSession().getId();
                String captchaCode = request.getParameter("captchaCode");
                if (StringUtils.isEmpty(captchaCode)) {
                    throw new BusException(CodeEnum.CAPTCHA_ISNULL);
                }
                // 从Redis获取验证码
                String redisKey = "captcha:" + sessionId;
                String correctCode = redisTemplate.opsForValue().get(redisKey);
                // 验证码错误
                if (!captchaCode.equalsIgnoreCase(correctCode)) {
                    throw new BusException(CodeEnum.CAPTCHA_ERROR);
                }
            }catch (BusException e){
                /**
                 *  由于security的登录失败处理器先于SpringMVC的统一异常处理器执行，
                 *  所以此时直接抛异常，统一异常处理器是不能执行的，
                 *  只能将异常交给security的登录失败处理器进行处理
                 */
                request.setAttribute("captchaError",e);
                // 手动Spring Security的登录失败处理器
                loginFailureHandler.onAuthenticationFailure(
                        request,
                        response,
                        new InvalidCaptchaException(e.getMessage())
                );
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}


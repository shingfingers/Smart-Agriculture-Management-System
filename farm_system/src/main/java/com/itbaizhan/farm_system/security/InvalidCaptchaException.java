package com.itbaizhan.farm_system.security;

import org.springframework.security.core.AuthenticationException;  // ✅ 正确的包

/**
 * 自定义验证码错误异常
 */
public class InvalidCaptchaException extends AuthenticationException {
    public InvalidCaptchaException(String msg) {
        super(msg);
    }
}

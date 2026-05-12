package com.itbaizhan.farm_common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security工具
 */
public class SecurityUtil {
    /**
     * 获取登录用户名
     * @return 登录用户名
     */
    public static String getUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return authentication.getName();
        }else {
            return "unknown";
        }
    }
}


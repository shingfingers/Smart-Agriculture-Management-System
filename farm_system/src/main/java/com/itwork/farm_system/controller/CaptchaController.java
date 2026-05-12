package com.itwork.farm_system.controller;

import com.itwork.farm_common.result.BaseResult;
import com.itwork.farm_common.util.CaptchaUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成验证码
     * @param session 会话对象
     * @return Base64编码的图片
     */
    @GetMapping("/generate")
    public BaseResult<String> generateCaptcha(HttpSession session) {
        // 生成二维码
        Map<String, Object> captcha = CaptchaUtils.generateCaptcha();
        // 将验证码存入redis
        // 生成唯一key（使用sessionId作为用户id）
        String key = "captcha:" + session.getId();
        String value = (String) captcha.get("code");
        // 存储到Redis，5分钟过期
        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
        // 设置验证码过期时间（5分钟）
        String image = (String) captcha.get("image");
        return BaseResult.ok(image);
    }
}


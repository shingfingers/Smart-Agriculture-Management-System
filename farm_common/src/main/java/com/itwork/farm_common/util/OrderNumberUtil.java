package com.itwork.farm_common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 订单号生成工具类
 * 格式: 日期 + 编号（每天的编号从1开始），比如20270105-00001
 */
@Component
public class OrderNumberUtil {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // redis key前缀
    private final String REDIS_KEY_PREFIX = "order:counter:";

    // 日期格式
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 生成订单号
     * @return 订单号
     */
    public String generateOrderNumber() {
        // 获取今天的日期字符串
        LocalDate today = LocalDate.now();
        String dateString = today.format(DATE_FORMATTER);

        // 设置Redis key
        String redisKey = REDIS_KEY_PREFIX + dateString;

        // 如果key存在，计数器+1，如果key不存在，则初始化为1
        Long increment = redisTemplate.opsForValue().increment(redisKey);

        // 生成订单号
        return dateString + "-" + String.format("%05d", increment);
    }
}

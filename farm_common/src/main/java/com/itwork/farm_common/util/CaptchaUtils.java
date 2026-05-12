package com.itwork.farm_common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证码工具类
 */
public class CaptchaUtils {
    // 验证码字符集
    private static final char[] chars = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k','m',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int IMAGE_WIDTH = 100;
    // 图片高度
    private static final int IMAGE_HEIGHT = 40;
    // 干扰线数量
    private static final int LINE_COUNT = 20;

    /**
     * 生成验证码图片
     * @return 包含验证码图片和验证码的Map
     */
    public static Map<String, Object> generateCaptcha() {
        // 生成随机验证码
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        String code = sb.toString();

        // 创建验证码图片
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 绘制验证码
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < CODE_LENGTH; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 30);
        }

        // 绘制干扰线
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(IMAGE_WIDTH);
            int y1 = random.nextInt(IMAGE_HEIGHT);
            int x2 = random.nextInt(IMAGE_WIDTH);
            int y2 = random.nextInt(IMAGE_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 释放资源
        g.dispose();

        // 将图片转换为Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码失败");
        }
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("image", "data:image/png;base64," + base64Image);
        return result;
    }
}


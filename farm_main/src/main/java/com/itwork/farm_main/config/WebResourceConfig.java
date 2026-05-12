package com.itwork.farm_main.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/**
 * Web 静态资源映射配置
 */
@Configuration
public class WebResourceConfig implements WebMvcConfigurer {

    /**
     * 将本地上传目录映射为可访问的静态资源路径
     *
     * <p>例如：/avatar/xxx.png -> {user.dir}/upload/avatar/xxx.png</p>
     *
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path avatarDir = Path.of(System.getProperty("user.dir"), "upload", "avatar");
        String location = avatarDir.toUri().toString();
        registry.addResourceHandler("/avatar/**").addResourceLocations(location);
    }
}


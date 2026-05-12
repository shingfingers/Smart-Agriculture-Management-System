package com.itwork.farm_main;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

//启动这个模块的时候读取哪些包
@SpringBootApplication(scanBasePackages = {
        "com.itwork.farm_common",
        "com.itwork.farm_main",
        "com.itwork.farm_system",
        "com.itwork.farm_warehousing",
       "com.itwork.farm_plant",
})
@MapperScan(basePackages = {
        "com.itwork.farm_system.mapper",
        "com.itwork.farm_warehousing.mapper",
        "com.itwork.farm_plant.mapper",
})
@EnableMethodSecurity(prePostEnabled = true)
//启用aop
@EnableAspectJAutoProxy
public class FarmMainApplication {



    public static void main(String[] args) {
        SpringApplication.run(FarmMainApplication.class, args);
    }
    // 分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}

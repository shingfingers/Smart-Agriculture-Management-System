package com.itbaizhan.farm_system.log;


import java.lang.annotation.*;

/**
 * 操作日志记录注解
 */
//代表该注解标在方法上
@Target({ElementType.METHOD})
//注解在运行时可用
@Retention(RetentionPolicy.RUNTIME)
//该注解会被javadoc记录
@Documented
public @interface Log {

    //模块标题
    String title() default "";
    //业务类型:0其它 1新增 2修改 3删除
    BusinessType businessType() default BusinessType.OTHER;
}

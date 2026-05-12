package com.itbaizhan.farm_system.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.itbaizhan.farm_system.security.MyEntryPoint;
import com.itbaizhan.farm_system.security.MyAccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// security配置类
@Configuration
// 开启鉴权配置注解
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CaptchaFilter captchaFilter;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MyLoginSuccessHandler myLoginSuccessHandler;
    @Autowired
    private  MyLoginFailureHandler myLoginFailureHandler;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyEntryPoint myEntryPoint;


    // Spring Security配置
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 自定义表单登录
        http.formLogin(
                form -> {
                    form.usernameParameter("username") // 用户名项
                            .passwordParameter("password") // 密码项
                            .loginProcessingUrl("/user/login") // 登录提交路径
                            .successHandler(myLoginSuccessHandler) // 登录成功处理器
                            .failureHandler(myLoginFailureHandler); // 登录失败处理器
                }
        );

        // 权限拦截配置
        http.authorizeHttpRequests(
                resp -> {
          //resp.requestMatchers("/login", "/user/login").permitAll(); // 登录请求不需要认证
                    //resp.anyRequest().authenticated();// 其余请求都需要认证
                    resp.requestMatchers("/**").permitAll(); // 允许所有路径
                }
        );

        // 退出登录配置
        http.logout(
                logout -> {
                    logout.logoutUrl("/user/logout") // 注销的路径
                            .logoutSuccessHandler(myLogoutSuccessHandler); // 登出成功处理器
                           // .clearAuthentication(true) // 清除认证数据
                           // .invalidateHttpSession(true); // 清除session
                }
        );

        // 设置session管理策略为无状态
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // 异常处理
        http.exceptionHandling(
                exception -> {
                    exception.authenticationEntryPoint(myEntryPoint)// 未登录处理器
                            .accessDeniedHandler(myAccessDeniedHandler); // 权限不足处理器
                }
        );

        // 跨域访问
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 关闭csrf防护
        http.csrf(csrf ->csrf.disable());

        /**
         * 配置验证码过滤器先于用户名密码验证执行
         * 否则可能先验证用户名密码，再校验验证码
         */
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        /**
         *  添加验证码过滤器
         *  添加JWT过滤器
         */
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 允许所有来源
        configuration.addAllowedMethod("*"); // 允许所有 HTTP 方法
        configuration.addAllowedHeader("*"); // 允许所有请求头
        configuration.setAllowCredentials(false); // 不允许携带凭证（与 * 兼容）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 应用于所有路径
        return source;
    }
}

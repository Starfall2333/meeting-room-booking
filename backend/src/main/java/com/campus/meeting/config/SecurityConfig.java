package com.campus.meeting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // 放行 Swagger UI 和相关资源
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 放行注册登录请求
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // 会议室相关请求需要认证
                        .requestMatchers("/api/v1/rooms/**").authenticated()
                        // 其他任何请求都需要认证（可后续调整）
                        .anyRequest().authenticated()
                )
                // 禁用 CSRF（开发阶段方便测试 POST 等请求，后续可再开启）
                .csrf(csrf -> csrf.disable())
                // 使用默认的表单登录（可选，保留以便后续测试）
                .formLogin(form -> form
                        .loginPage("/login")  // 默认
                        .permitAll()
                )
                .httpBasic(httpBasic -> {}); // 支持 Basic Auth（可选）

        return http.build();
    }

    /**
     * 密码编码器 Bean，使用 BCrypt 强哈希
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

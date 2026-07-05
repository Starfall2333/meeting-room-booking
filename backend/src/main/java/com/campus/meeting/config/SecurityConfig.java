package com.campus.meeting.config;

import com.campus.meeting.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（JWT 无状态，不需要）
                .csrf(csrf -> csrf.disable())

                // 2. 配置路径权限
                .authorizeHttpRequests(authz -> authz
                        // 放行 OPTIONS 请求（跨域预检）
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 放行 Swagger 和 认证接口
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/**").permitAll()
                        // 业务接口需要认证（后续可细化角色）
                        .requestMatchers("/api/v1/rooms/**").authenticated()
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )

                // 3. 关键：设置会话为无状态（STATELESS）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 禁用默认的表单登录和 Basic Auth（因为我们要用 JWT）
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())

                // 5. 将自定义 JWT 过滤器放在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 未认证请求 全局异常处理
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(unauthorizedHandler));

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

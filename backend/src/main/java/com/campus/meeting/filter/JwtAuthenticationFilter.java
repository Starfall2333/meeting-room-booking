package com.campus.meeting.filter;

import com.campus.meeting.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中获取 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 判断是否存在且以 Bearer 开头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            try {
                // 3. 验证 Token 是否有效（未过期且格式正确）
                if (jwtUtil.isTokenValid(token)) {
                    // 4. 从 Token 中提取用户名和角色
                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token); // 从 Claims 中提取 role

                    // 5. 构建权限列表（Spring Security 默认需要 ROLE_ 前缀，但若使用 hasAuthority 则可省略）
                    //    这里我们直接使用 "ADMIN" 或 "USER" 作为权限字符串
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    // 6. 创建认证令牌（此时密码为 null，因为 JWT 已代表认证通过）
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    // 7. 设置请求详情（便于后续获取 IP 等）
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. 将认证信息放入 SecurityContextHolder（关键步骤）
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token 解析失败（过期、签名错误等），不设置认证，让后续拦截器处理 401
                System.out.println("JWT 解析失败: " + e.getMessage());
            }
        }

        // 9. 继续执行后续过滤器链
        filterChain.doFilter(request, response);
    }
}

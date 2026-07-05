package com.campus.meeting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    /**
     * 临时测试接口：用于验证 JWT 认证是否生效
     * 后续 Sprint 2 Day 2 完成后可删除此 Controller
     */
    @GetMapping("/ping")
    public Map<String, String> ping() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "JWT 认证成功，接口访问通过！");
        result.put("status", "ok");
        return result;
    }
}

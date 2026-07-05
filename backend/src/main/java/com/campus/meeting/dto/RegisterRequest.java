package com.campus.meeting.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    // 可添加 role 字段，默认 USER
}

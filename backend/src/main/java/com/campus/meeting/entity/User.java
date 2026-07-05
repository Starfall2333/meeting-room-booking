package com.campus.meeting.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;      // 'ADMIN' 或 'USER'
    private LocalDateTime createTime;
}

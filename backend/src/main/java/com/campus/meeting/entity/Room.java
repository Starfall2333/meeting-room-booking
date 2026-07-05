package com.campus.meeting.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Room {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
    private String status; // available / maintenance
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

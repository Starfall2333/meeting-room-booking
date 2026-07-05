package com.campus.meeting.dto;

import lombok.Data;

@Data
public class RoomRequest {
    private String name;
    private String location;
    private Integer capacity;
    private String description;
    private String status; // 前端可选传，不传则默认为 available
}

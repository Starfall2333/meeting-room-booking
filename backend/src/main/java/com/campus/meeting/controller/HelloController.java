package com.campus.meeting.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @RequestMapping("/hello")
    public String hello(String name) {
        System.out.println("HelloController …… hello : " + name);
        return "hello" + name;
    }
}

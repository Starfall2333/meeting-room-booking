package com.campus.meeting.service;


import com.campus.meeting.entity.User;
import com.campus.meeting.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册新用户（密码自动加密）
     */
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            return false; // 用户名重复
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 默认角色
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        return userMapper.insert(user) > 0;
    }

    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 验证用户登录（校验密码）
     * @return 验证通过返回 User 对象，否则返回 null
     */
    public User authenticate(String username, String rawPassword) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            return user;
        }
        return null;
    }
}

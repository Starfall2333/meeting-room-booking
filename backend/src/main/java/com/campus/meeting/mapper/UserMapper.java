package com.campus.meeting.mapper;

import com.campus.meeting.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(username, password, email, role, create_time) " +
            "VALUES(#{username}, #{password}, #{email}, #{role}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);
}

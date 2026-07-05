package com.campus.meeting.mapper;

import com.campus.meeting.entity.Room;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoomMapper {

    // 1. 新增会议室（主键自动回填到 id 字段）
    @Insert("INSERT INTO room(name, location, capacity, description, status) " +
            "VALUES(#{name}, #{location}, #{capacity}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);

    // 2. 根据ID删除
    @Delete("DELETE FROM room WHERE id = #{id}")
    int deleteById(Long id);

    // 3. 更新会议室（动态 SQL，只更新非空字段）
    @Update("<script>" +
            "UPDATE room SET " +
            "<if test='name != null'> name=#{name}, </if>" +
            "<if test='location != null'> location=#{location}, </if>" +
            "<if test='capacity != null'> capacity=#{capacity}, </if>" +
            "<if test='description != null'> description=#{description}, </if>" +
            "<if test='status != null'> status=#{status}, </if>" +
            " update_time = NOW() " +
            "WHERE id = #{id}" +
            "</script>")
    int update(Room room);

    // 4. 根据ID查询
    @Select("SELECT * FROM room WHERE id = #{id}")
    Room selectById(Long id);

    // 5. 查询所有（支持按名称模糊搜索）
    @Select("<script>" +
            "SELECT * FROM room " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            " name LIKE CONCAT('%', #{keyword}, '%') " +
            "</if>" +
            "</where>" +
            " ORDER BY id DESC" +
            "</script>")
    List<Room> selectAll(@Param("keyword") String keyword);
}

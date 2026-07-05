package com.campus.meeting.service;

import com.campus.meeting.entity.Room;
import com.campus.meeting.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    // 新增（默认状态为 available）
    public int addRoom(Room room) {
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("available");
        }
        return roomMapper.insert(room);
    }

    // 删除
    public int deleteRoom(Long id) {
        return roomMapper.deleteById(id);
    }

    // 更新
    public int updateRoom(Room room) {
        return roomMapper.update(room);
    }

    // 查询详情
    public Room getRoomById(Long id) {
        return roomMapper.selectById(id);
    }

    // 查询列表（支持搜索）
    public List<Room> listRooms(String keyword) {
        return roomMapper.selectAll(keyword);
    }
}

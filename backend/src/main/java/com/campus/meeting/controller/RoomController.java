package com.campus.meeting.controller;

import com.campus.meeting.dto.RoomRequest;
import com.campus.meeting.entity.Room;
import com.campus.meeting.service.RoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // 1. 获取列表（GET /api/v1/rooms?keyword=xxx）
    @GetMapping
    public ResponseEntity<List<Room>> list(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(roomService.listRooms(keyword));
    }

    // 2. 获取详情（GET /api/v1/rooms/{id}）
    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }

    // 3. 新增（POST /api/v1/rooms）
    @PostMapping
    public ResponseEntity<Map<String, Object>> add(@RequestBody RoomRequest request) {
        Room room = new Room();
        // 将前端传参拷贝到实体类
        BeanUtils.copyProperties(request, room);

        int rows = roomService.addRoom(room);
        Map<String, Object> response = new HashMap<>();
        if (rows > 0) {
            response.put("success", true);
            response.put("message", "新增成功");
            response.put("id", room.getId()); // 返回自动生成的ID
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "新增失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 4. 更新（PUT /api/v1/rooms/{id}）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody RoomRequest request) {
        // 先检查会议室是否存在
        Room existing = roomService.getRoomById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        // 将前端传参拷贝到 existing（保留 id 和 createTime）
        BeanUtils.copyProperties(request, existing);
        existing.setId(id); // 确保 ID 正确

        int rows = roomService.updateRoom(existing);
        Map<String, Object> response = new HashMap<>();
        if (rows > 0) {
            response.put("success", true);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "更新失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 5. 删除（DELETE /api/v1/rooms/{id}）
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Room existing = roomService.getRoomById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        int rows = roomService.deleteRoom(id);
        Map<String, Object> response = new HashMap<>();
        if (rows > 0) {
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "删除失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

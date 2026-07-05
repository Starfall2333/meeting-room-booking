import request from '@/utils/request';

// ---------- 类型定义 ----------
// 会议室实体（与后端对应）
export interface Room {
  id?: number;
  name: string;
  location: string;
  capacity: number;
  description?: string;
  status?: 'available' | 'maintenance';
  createTime?: string;
  updateTime?: string;
}

// 新增/更新时的请求体（不含 id 和自动生成字段）
export interface RoomRequest {
  name: string;
  location: string;
  capacity: number;
  description?: string;
  status?: 'available' | 'maintenance';
}

// ---------- API 方法 ----------
// 获取会议室列表（支持关键词搜索）
export const getRooms = (keyword?: string) => {
  return request.get<Room[]>('/rooms', { params: { keyword } });
};

// 新增会议室
export const addRoom = (data: RoomRequest) => {
  return request.post<{ success: boolean; message: string; id: number }>('/rooms', data);
};

// 更新会议室
export const updateRoom = (id: number, data: RoomRequest) => {
  return request.put<{ success: boolean; message: string }>(`/rooms/${id}`, data);
};

// 删除会议室
export const deleteRoom = (id: number) => {
  return request.delete<{ success: boolean; message: string }>(`/rooms/${id}`);
};
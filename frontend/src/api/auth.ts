import request from '@/utils/request';

// 请求参数类型
export interface LoginParams {
  username: string;
  password: string;
}

export interface RegisterParams {
  username: string;
  password: string;
  email: string;
}

// 登录响应类型（根据后端实际返回调整）
export interface LoginResponse {
  token: string;
  username: string;
  role: string;
}

// 注册响应类型（通常返回字符串或简单对象）
export type RegisterResponse = string; // 或 { message: string }

// API 方法，指定泛型
export const loginApi = (data: LoginParams) => {
  return request.post<LoginResponse>('/auth/login', data);
};

export const registerApi = (data: RegisterParams) => {
  return request.post<RegisterResponse>('/auth/register', data);
};
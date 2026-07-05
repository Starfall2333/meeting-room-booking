import axios, { type AxiosRequestConfig } from 'axios';
import { ElMessage } from 'element-plus';

const instance = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 5000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器：直接返回 response.data
instance.interceptors.response.use(
  (response) => {
    return response.data; // 这里返回的是业务数据
  },
  (error) => {
    if (error.response) {
      const status = error.response.status;
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录');
        localStorage.removeItem('token');
        window.location.href = '/login';
      } else {
        ElMessage.error(error.response.data?.message || '请求失败');
      }
    } else {
      ElMessage.error('网络连接异常');
    }
    return Promise.reject(error);
  }
);

// 重新封装，让 TypeScript 知道返回的是数据（而不是 AxiosResponse）
const request = {
  get: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return instance.get(url, config) as Promise<T>;
  },
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return instance.post(url, data, config) as Promise<T>;
  },
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
    return instance.put(url, data, config) as Promise<T>;
  },
  delete: <T = any>(url: string, config?: AxiosRequestConfig): Promise<T> => {
    return instance.delete(url, config) as Promise<T>;
  },
};

export default request;
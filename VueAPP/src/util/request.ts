// src/services/request.ts
import axios from 'axios';
import { type AxiosInstance } from 'axios';
import { storeToRefs } from 'pinia';
import { useAuthStore } from '@/stores/auth';
//import {pinia} from 'main'
import { ElMessage } from 'element-plus'; // 或者使用其他UI库的消息提示组件


// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',//测试用
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
    //...(process.env.NODE_ENV === 'production' ? { 'X-Requested-With': 'XMLHttpRequest' } : {}),
    'X-Requested-With': 'XMLHttpRequest' 
  },
});



// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 使用Pinia store
const authStore = useAuthStore();
const { token } = storeToRefs(authStore);
    if (token.value) {
      config.headers.Authorization = `Bearer ${token.value}`;//这块是AI写的，我没学到这
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 如果后端返回新的token，这里可以更新store中的token
    // 假设新的token在response.data.newToken字段
    //暂不考虑该功能
    /* if (response.data.newToken) {
      token.value = response.data.newToken;
    } */
    return response//.data; // 可能需要这么写，不然类型报错。自学的不确定
  },
  async (error) => {
    const originalRequest = error.config;

    // 处理401错误，尝试刷新令牌
    if (error.response.status === 401 && !originalRequest._retry) {
      //originalRequest._retry = true;

      try {
        ElMessage.error('请先登录')
        // 这里假设有一个refreshToken的API，需要根据实际情况调整
        //暂不实现
      } catch (refreshError) {
        // 使用Pinia store
const authStore = useAuthStore();
        ElMessage.error('登录状态已失效，请重新登录');
        authStore.logout(); // 清理store中的认证信息
      }
    }

    // 其他错误处理
    ElMessage.error(error.message || '网络错误');
    return Promise.reject(error);
  }
);

export default service;
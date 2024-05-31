// src/services/request.ts
import axios from 'axios';
import { type AxiosInstance } from 'axios';
import { storeToRefs } from 'pinia';
import { useAuthStore } from '../stores/auth';
//import {pinia} from 'main'
import { ElMessage } from 'element-plus'; // 或者使用其他UI库的消息提示组件
import router from '../router/router'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://1.94.119.197:8080',
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
const token = authStore.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;//这块是AI写的，我没学到这
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: any) => {
    // 未登录回到登录页
    if (response.status === 401) {
      return router?.push('/login')
    }
    
       return response.data
     
  },
  (error) => {
    const { response } = error;
    // 如果有响应（即请求发出了但服务器返回了错误）
    if (response) {
      // 检查401未授权状态码
      if (response.status === 401) {
        console.log('未登录，重定向到登录页');
        
          router.push('/login');
        }
          if (response.status !== 200) {
            //ElMessage.error(response.data.msg)
             return Promise.reject(response.status)
           } 
      }
  }
)
export default service;
// src/services/request.ts
import axios from 'axios';
import { type AxiosInstance } from 'axios';
import { storeToRefs } from 'pinia';
import { useAuthStore } from '@/stores/auth';
//import {pinia} from 'main'
import { ElMessage } from 'element-plus'; // 或者使用其他UI库的消息提示组件
import router from '@/router/router'

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
    console.log(response.status)
    if (response.status === 401) {
      return router?.push('/login')
    }
    if (response.status !== 200) {
      console.log(response)
      console.log("status错误"+response.status)
      //ElMessage.error(response.data.msg)
       return Promise.reject(response.status)
     } else {
      console.log("成功返回")
       return response.data
     }
  },
  (error: any) => {
    console.log("有任何错误")
  }
)
export default service;
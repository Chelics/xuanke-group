// src/stores/auth.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/util/request';
import service from '@/util/request';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
export const useAuthStore = defineStore('auth', {
  state: () => ({
    // 初始化状态
    token: '', // 测试用的 token，实际应从 API 获取
    StoUsername: '', // 测试用户名，根据实际逻辑调整
    userRole: '', // 用户角色，根据实际逻辑调整
  }),

  // 可以在这里定义 getters、actions 等

  // 开启持久化，这里我们对所有状态进行持久化
  // 如果只需要持久化部分状态，可以使用 paths 选项指定
  persist: {
    // 自定义存储的键名，默认为 store 的 id，这里是 'auth'
    // key: 'authStorage',
    // 指定存储方式，默认为 localStorage，也可以是 sessionStorage
    // storage: localStorage,
    // 指定需要持久化的状态路径数组
    // paths: ['token', 'StoUsername', 'userRole'],
  },
});
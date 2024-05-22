// src/stores/auth.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/util/request';

export const useAuthStore = defineStore('auth', () => {
  const token = ref('1');//测试,非空数据。只要没有连接后端数据，不会触发更新令牌
  const username = ref('1');//测试，0学生，1老师，2教务
  const userRole=ref('teacher');//测试，实际调整角色在这里。

  async function login(username: string, password: string) {
    try {
      const response = await request.post('/login', { username, password });
      if (response.status === 200 && response.data.code===1&&response.data.msg==='success') {//应该是这么写，没调过，不确定
        token.value = response.data.data.token;//大概吧。response.data是整个后端返回。code，msg，data，找data的token属性
        username = response.data.username;
        //确定用户角色
        if(username.startsWith('0')){
          userRole.value='student';
        }
        if(username.startsWith('1')){
          userRole.value='teacher';
        }
        if(username.startsWith('2')){
          userRole.value='staff';
        }
      } else {
        throw new Error('登录失败，请检查您的用户名和密码！');
      }
    } catch (error) {
        console.error('登录请求出现其它错误:', error);
        //直接处理未知错误 error;
        alert('登录请求出现其它错误:'+ error)
      }
  }

  function logout() {
    token.value = '';
    username.value = '';
  }
  function isLoggedIn() {
    return token.value.length!==0;
  }

  return { token, username,userRole, login, logout ,isLoggedIn};
});
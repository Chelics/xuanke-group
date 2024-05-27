// src/stores/auth.ts
import { defineStore } from 'pinia';
import { ref } from 'vue';
import request from '@/util/request';
interface ResponseData {
  code: number;
  msg: string;
  data: any;
}
export const useAuthStore = defineStore('auth', () => {
  const token = ref('eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg5LiJIiwiaWQiOjEsInVzZXJuYW1lIjoiMDEyMzQ1Njc4IiwiZXhwIjoxNzE2ODIzMTk0fQ.pEMF8rPyMiWFk2AlDik1FSRVWwZZ9COyAREom6Y49fE');//测试,非空数据。只要没有连接后端数据，不会触发更新令牌
  const username = ref('0');//测试，0开头学生，1开头老师，2开头教务
  const userRole=ref('student');//测试，实际调整角色在这里。

  async function login(usernameInput: string, password: string) {
    try {
      const response :ResponseData= await request.post('/login', { usernameInput, password });
      //应该是这么写，没调过，基本是对的
        if (response.code === 1 && response.msg === 'success') { // 假设成功码为200，与拦截器逻辑匹配
          token.value = response.data.token; //此处response为直接后端code，msg，data，找data的token属性。原生response.data是整个后端返回，包装已经拆除。
        username.value = usernameInput;
        //确定用户角色
        if(username.value.startsWith('0')){
          userRole.value='student';
        }
        if(username.value.startsWith('1')){
          userRole.value='teacher';
        }
        if(username.value.startsWith('2')){
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
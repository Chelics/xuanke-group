//import request from '@/util/request.ts'

/* export const userLoginService =(loginData)=>{
    const params = new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key])
    }
    return request.post('/user/login',params)
} */
interface ResponseData {
    code: number;
    msg: string;
    data: any;
  }
  import  service from '@/util/request';
  import { useAuthStore } from '@/stores/auth';
export async function login(username: string, password: string) {
    const authStore = useAuthStore();
    try {
      const response :ResponseData= await service.post('/login', { username, password });
      //应该是这么写，没调过，基本是对的
        if (response.code === 1 && response.msg === 'success') { // 假设成功码为200，与拦截器逻辑匹配
            authStore.token = response.data; //此处response为直接后端code，msg，data，找data的token属性。原生response.data是整个后端返回，包装已经拆除。
          localStorage.setItem('token', authStore.token);
          authStore.StoUsername = username;
        //确定用户角色
        const usernameValue = String(authStore.StoUsername)
        if(usernameValue.startsWith('0')){
            authStore.userRole='student';
        }
        if(usernameValue.startsWith('1')){
            authStore.userRole='teacher';
        }
        if(usernameValue.startsWith('2')){
            authStore.userRole='staff';
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

 export function logout() {
    const authStore = useAuthStore();
    console.log("执行了logout")
    authStore.token = '';
    authStore.StoUsername = '';
  }
export  function isLoggedIn() {
    const authStore = useAuthStore();
    return authStore.token!=='';
  }
import { defineStore } from 'pinia'
import {ref} from 'vue'


export const useTokenStore = defineStore('token',()=>{
    //响应式变量
    const token = ref('')

    //修改token值
    const setToken = (newToken:any)=>{
        token.value = newToken;
    }

    //移出token值
    const removeToken = ()=>{
        token.value = ''
    }

    return{
        token,setToken,removeToken
    }
});
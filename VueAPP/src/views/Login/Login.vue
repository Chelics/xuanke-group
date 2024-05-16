<script setup lang="ts">
// src/views/Login.vue
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import { ElButton } from 'element-plus';



const formData = ref({ username: '', password: '' });
const authStore = useAuthStore();
const router = useRouter();

async function handleLogin() {//虚假报错（如果有黄线）
  try {
    await authStore.login(formData.value.username, formData.value.password);
    // 登录成功后，根据需要重定向
    //router.push('/');
    // 根据角色跳转
    switch (authStore.userRole) {
      case 'student':
        router.push('/student');
        break;
      case 'teacher':
        router.push('/teacher');
        break;
      case 'staff':
        router.push('/staff');
        break;
      default:
        console.error('Unknown user role');
    }
  }  catch (Error:Error) {
    // 直接处理已知的错误结构，这里强制 Error 是 Error对象，只有一个Error Message属性
    let errorMessage = Error.errorMessage;

    alert(errorMessage);
    
  }
}
</script>
<template>
  <h1>login</h1>
  <div class="mb-4">
    <el-button>测试使用，默认身份学生，需要修改前往stores</el-button>
    <el-button type="primary" ref="/student" @click="()=>router.push('/student')">我是学生</el-button>
    <el-button type="success" ref="/teacher" @click="()=>router.push('/teacher')">我是老师</el-button>
    <el-button type="info" ref="/admin" @click="()=>router.push('/staff')">我是教务</el-button>
  </div>
</template>
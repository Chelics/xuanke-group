<script setup lang="ts">
// src/views/Login.vue
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import { ElButton } from 'element-plus';

import { userLoginService } from '@/api/Login';

const login = () => {
  /////接口service已经定义，在login.ts，，还没有使用

}

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
  } catch (Error: Error) {
    // 直接处理已知的错误结构，这里强制 Error 是 Error对象，只有一个Error Message属性
    let errorMessage = Error.errorMessage;

    alert(errorMessage);

  }
}

import { reactive } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

const ruleFormRef = ref<FormInstance>()

const checkName = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('请输入用户名'))
  }
  setTimeout(() => {
    if (!Number.isInteger(value)) {
      callback(new Error('请输入正确的用户名'))
    } else {
      callback()
    }
  }, 100)
}

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (ruleForm.pass !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('pass')
    }
    callback()
  }
}


const ruleForm = reactive({
  pass: '',
  name: '',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  name: [{ validator: checkName, trigger: 'blur' }],
  pass: [{ validator: validatePass, trigger: 'blur' }]
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('已提交!')
    } else {
      console.log('提交错误!')
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

</script>




<template>

  <!-- <h1>login</h1>
  <div class="mb-4">
    <el-button>测试使用，默认身份学生，需要修改前往stores</el-button>
    <el-button type="primary" ref="/student" @click="() => router.push('/student')">我是学生</el-button>
    <el-button type="success" ref="/teacher" @click="() => router.push('/teacher')">我是老师</el-button>
    <el-button type="info" ref="/admin" @click="() => router.push('/staff')">我是教务</el-button>
  </div> -->


  <div class="container">
    <div class="pic"></div>
    <div class="title">选课管理系统</div>
    <el-form ref="ruleFormRef" class="form-container" :model="ruleForm" status-icon :rules="rules" label-width="auto">
      <el-form-item label="用户名" prop="name">
        <el-input v-model.number="ruleForm.name" />
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input v-model="ruleForm.pass" type="password" autocomplete="off" />
      </el-form-item>
  
      <el-form-item>
        <div style="display: flex; justify-content: right;">
          <el-button type="primary" @click="submitForm(ruleFormRef)">登录</el-button>
          <el-button @click="resetForm(ruleFormRef)">清空输入</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
  


</template>


<style>
.container {
  position: relative;
}

.pic {
  background-image: url(./sky.jpg);
  background-size: cover;
  background-position: center bottom;
  height: 900px;
}

.form-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-width: 400px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8); /* 添加半透明背景以突出显示表单 */
}

.title {
  position: absolute;
  top: 100px; /* 调整距离顶部的位置 */
  left: 50%;
  transform: translateX(-50%);
  font-size: 80px; /* 修改字体大小为36px */
  font-weight: bold;
  color: white; /* 修改字体颜色为白色 */
}

</style>



<template>
    <el-container style="height: 100vh;">
      <el-aside width="200px" style="background-color: #f5f7fa;">
        <el-menu
          background-color="#f5f7fa"
          text-color="#303133"
          active-text-color="#409eff"
          router
          :default-active="$route.path"
        >
        <el-submenu v-if="authStore.userRole === 'student'" :default-active="$route.path">
          <el-menu-item index="/student/student-schedule">
            <span>学生课程</span>
          </el-menu-item>
          <el-menu-item index="/student/course-selection">
            <span>学生选课</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu v-if="authStore.userRole === 'teacher'" :default-active="$route.path">
          <el-menu-item index="/teacher/teacher-schedule">
            <span>教师课表</span>
          </el-menu-item>
          <el-menu-item index="/teacher/teacher-courses">
            <span>教师课程管理</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu v-if="authStore.userRole === 'staff'" :default-active="$route.path">
          <el-menu-item index="/staff/state">
            <span>教务查看阶段</span>
          </el-menu-item>
          <el-menu-item index="/staff/classroom">
            <span>教室课表</span>
          </el-menu-item>
          <el-menu-item index="/staff/audit">
            <span>教务审核</span>
          </el-menu-item>
        </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>

  </template>
  
  <script setup lang="ts">
  //这个是主跳转路由，页面没有美化
  import { useAuthStore } from '../src/stores/auth';//大概不会报错了
  const authStore = useAuthStore();
  </script>
  
  <style scoped>
  /* 可根据需要自定义样式 */
  </style>
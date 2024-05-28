

<template>
    <el-container style="height: 100vh;">
      <navigation v-if="!$route.meta.showNav">
      <el-aside width="200px" style="background-color: #FFFFFF;">
        <div class="logo">
          <img src="./logo.jpg" alt="Logo" />
        </div>
        <el-menu
          background-color="#FFFFFF"
          text-color="#303133"
          active-text-color="#409eff"
          router
          :default-active="$route.path"
        >
        <el-submenu v-if="authStore.userRole === 'student'" :default-active="$route.path">
          <el-menu-item index="/student/student-schedule">
            <el-icon><icon-menu /></el-icon>
            <span>学生课程</span>
          </el-menu-item>
          <el-menu-item index="/student/course-selection">
            <el-icon><icon-menu /></el-icon>
            <span>学生选课</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu v-if="authStore.userRole === 'teacher'" :default-active="$route.path">
          <el-menu-item index="/teacher/teacher-schedule">
            <el-icon><icon-menu /></el-icon>
            <span>教师课表</span>
          </el-menu-item>
          <el-menu-item index="/teacher/teacher-courses">
            <el-icon><icon-menu /></el-icon>
            <span>教师课程管理</span>
          </el-menu-item>
        </el-submenu>
        <el-submenu v-if="authStore.userRole === 'staff'" :default-active="$route.path">
          <el-menu-item index="/staff/state">
            <el-icon><icon-menu /></el-icon>
            <span>教务查看阶段</span>
          </el-menu-item>
          <el-menu-item index="/staff/classroom">
            <el-icon><icon-menu /></el-icon>
            <span>教室课表</span>
          </el-menu-item>
          <el-menu-item index="/staff/audit">
            <el-icon><icon-menu /></el-icon>
            <span>教务审核</span>
          </el-menu-item>
        </el-submenu>
        </el-menu>
      </el-aside>
    </navigation>
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

  .el-menu {
    width: 100%; /* 使菜单项占满整个侧边栏宽度 */
  }
  
  .el-menu-item, .el-submenu__title {
    font-size: 16px; /* 设置菜单项字体大小 */
  }
  
  .logo {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    height: 100px; /* 设置logo容器的高度 */
  }
  
  .logo img {
    max-width: 100%; /* 让图片宽度自适应容器 */
    max-height: 100%; /* 让图片高度自适应容器 */
  }
  .el-aside {
    background-color: #FFFFFF;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影效果，可以根据需求调整阴影的颜色、模糊度和偏移量 */
  }
  
  </style>


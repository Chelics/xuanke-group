import { createRouter, createWebHistory } from "vue-router"
import Login from "@/views/Login/Login.vue"
import Register from "@/views/Login/Register.vue"
import ForgetPassword from "@/views/Login/ForgetPassword.vue"

import StudentSchedule from "@/views/Student/StudentSchedule.vue"
import StudentCourseSelection from "@/views/Student/StudentCourseSelection.vue"

import TeacherSchedule from "@/views/Teacher/TeacherSchedule.vue"
import TeacherCourses from "@/views/Teacher/TeacherCourses.vue"

import State from "@/views/Staff/State.vue"
import Classroom from "@/views/Staff/Classroom.vue"
import AdminAudit from "@/views/Staff/StaffAudit.vue"

import { useAuthStore } from '@/stores/auth';
import HtmlTable from "@/testview/htmlTable.vue"
import StaffAudit from "@/views/Staff/StaffAudit.vue"
const router = createRouter({
    history: createWebHistory(),
    routes: [
        //登录部分路由
        {
            path: '/',
            redirect: 'login'
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            meta:{
                showNav: true
              }
        },
        {
            path: '/register',
            component: Register,
            meta:{
                showNav: true
              }
        },
        {
            path: '/forget-password',
            component: ForgetPassword,
            meta:{
                showNav: true
              }
        },
        //学生部分路由
        {
            path: '/student',
            name: 'StudentHome',
            redirect: '/student/student-schedule',
            children: [
                {
                    path: '/student/student-schedule',
                    component: StudentSchedule,
                    props: true,
                    meta: { requiresAuth: true, role: 'student' },
                },
                {
                    path: '/student/course-selection',
                    component: StudentCourseSelection,
                    props: true,
                    meta: { requiresAuth: true, role: 'student' },
                }

            ]
        },
        //教师部分路由
        {
            path: '/teacher',
            redirect: '/teacher/teacher-schedule',
            name: 'TeacherHome',
            children: [
                {
                    path: '/teacher/teacher-schedule',
                    component: TeacherSchedule,
                    props: true,
                    meta: { requiresAuth: true, role: 'teacher' },
                },
                {
                    path: '/teacher/teacher-courses',
                    component: TeacherCourses,
                    props: true,
                    meta: { requiresAuth: true, role: 'teacher' },
                }

            ]
        },
        //教务（管理，staff）部分路由
        {
            path: '/staff',
            redirect: '/staff/state',
            name: 'StaffHome',
            children: [
                {
                    path: '/staff/state',
                    component: State,
                    props: true,
                    meta: { requiresAuth: true, role: 'staff' },
                },
                {
                    path: '/staff/classroom',
                    component: Classroom,
                    props: true,
                    meta: { requiresAuth: true, role: 'staff' },
                },
                {
                    path: '/staff/audit',
                    component: StaffAudit,
                    props: true,
                    meta: { requiresAuth: true, role: 'staff' },
                }
            ]
        },
        {
            path:'/test',
            component:HtmlTable,
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/login',
          },

    ],
})
router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();

    if (to.meta.requiresAuth && !authStore.isLoggedIn()) {
        next({ name: 'login' });
    } else if (to.meta.role && authStore.userRole !== to.meta.role) {
        // 如果角色不匹配，可以提示错误或重定向到相应的主页
        alert(`无权访问当前页面`);
        //next(authStore.userRole === 'student' ? { name: 'StudentHome' } : { name: 'TeacherHome' });
        if (authStore.userRole === 'student') next({ name: 'StudentHome' });
        else if (authStore.userRole === 'teacher') next({ name: 'TeacherHome' });
        else if (authStore.userRole === 'staff') next({ name: 'StaffHome' });
        else next({name:'login'});
    } else {
        next();
    }
});
export default router
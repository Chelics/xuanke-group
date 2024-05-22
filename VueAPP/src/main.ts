import { createApp } from 'vue'//
import App from './App.vue'
import ElementPlus from 'element-plus'//导入
import 'element-plus/dist/index.css'//导入样式
import router from './router/router'
import { createPinia } from 'pinia'
import zh from 'element-plus/dist/locale/zh-cn.js'
//import { useAuthStore } from './stores/auth'

const app=createApp(App)
const pinia=createPinia()
app.use(pinia)
app.use(ElementPlus)
app.use(router)
app.use(ElementPlus,{zh})
app.mount('#app')
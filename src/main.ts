import { createApp } from 'vue'//
import App from './App.vue'
import ElementPlus from 'element-plus'//导入
import 'element-plus/dist/index.css'//导入样式
import router from './router';

createApp(App).use(router).mount('#app')
App.use(ElementPlus)

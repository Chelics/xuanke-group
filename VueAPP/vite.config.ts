import { fileURLToPath, URL } from 'node:url';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: [{ 
      find: '@', 
      replacement: fileURLToPath(new URL('./src', import.meta.url)) 
    }],
  },
  base: './', // 移动到顶层与 export default defineConfig 同级，作为它的属性
});
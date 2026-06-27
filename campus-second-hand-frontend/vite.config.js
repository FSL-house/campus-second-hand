import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Vite 配置文件。
// 这里主要配置前端启动端口，方便和后端 8080 端口区分。
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173
  }
})

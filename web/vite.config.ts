import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
    base: './',
    plugins: [vue()],
    server: {
        proxy: {
            '/api': {
                target: 'http://192.168.5.131:8888',
                changeOrigin: true,
            },
        },
    },
})

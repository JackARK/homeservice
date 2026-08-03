import { createApp } from 'vue'
import App from './App.vue'
import { absorbTokenFromUrl } from './auth'
import { router } from './router'
import './style.css'

// 先吸收 URL 中的免密 token，再挂载应用
absorbTokenFromUrl()

createApp(App).use(router).mount('#app')

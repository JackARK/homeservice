import { createRouter, createWebHashHistory } from 'vue-router'
import { getToken } from '../auth'

export const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
        {
            path: '/',
            component: () => import('../layout/MainLayout.vue'),
            children: [
                { path: '', name: 'status', component: () => import('../views/StatusView.vue') },
                { path: 'rules', name: 'rules', component: () => import('../views/RulesView.vue') },
                { path: 'config', name: 'config', component: () => import('../views/ConfigView.vue') },
                { path: 'tts', name: 'tts', component: () => import('../views/TtsView.vue') },
                { path: 'logs', name: 'logs', component: () => import('../views/LogsView.vue') },
            ],
        },
    ],
})

router.beforeEach((to) => {
    if (to.path !== '/login' && !getToken()) return '/login'
    if (to.path === '/login' && getToken()) return '/'
    return true
})

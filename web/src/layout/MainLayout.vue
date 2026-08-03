<template>
    <n-layout has-sider style="height: 100vh">
        <n-layout-sider bordered :width="210" style="display: flex; flex-direction: column">
            <div class="logo">homeService 控制台</div>
            <n-menu :value="activeKey" :options="menuOptions" @update:value="onSelect" />
            <div class="logout">
                <n-button text type="error" @click="logout">退出登录</n-button>
            </div>
        </n-layout-sider>
        <n-layout-content content-style="padding: 16px; box-sizing: border-box" :native-scrollbar="false">
            <router-view />
        </n-layout-content>
    </n-layout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NButton, NLayout, NLayoutContent, NLayoutSider, NMenu } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { clearToken } from '../auth'

const route = useRoute()
const router = useRouter()

const menuOptions: MenuOption[] = [
    { label: '状态看板', key: '/' },
    { label: '规则管理', key: '/rules' },
    { label: '配置', key: '/config' },
    { label: '音色', key: '/tts' },
    { label: '日志', key: '/logs' },
]

const activeKey = computed(() => route.path)

function onSelect(key: string) {
    void router.push(key)
}

function logout() {
    clearToken()
    void router.push('/login')
}
</script>

<style scoped>
.logo {
    padding: 16px;
    font-size: 16px;
    font-weight: 600;
    text-align: center;
}

.logout {
    position: absolute;
    bottom: 16px;
    left: 0;
    right: 0;
    text-align: center;
}
</style>

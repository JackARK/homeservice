<template>
    <div class="login-wrap">
        <n-card title="homeService 控制台登录" style="width: 380px">
            <n-form @submit.prevent="onLogin">
                <n-form-item label="访问令牌（Token）">
                    <n-input
                        v-model:value="token"
                        type="password"
                        show-password-on="click"
                        placeholder="请输入服务端配置的 server.token"
                        @keyup.enter="onLogin"
                    />
                </n-form-item>
                <n-button type="primary" block :loading="loading" @click="onLogin">登录</n-button>
            </n-form>
        </n-card>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { NButton, NCard, NForm, NFormItem, NInput, useMessage } from 'naive-ui'
import { setToken } from '../auth'
import { verifyToken } from '../api/client'

const router = useRouter()
const message = useMessage()

const token = ref('')
const loading = ref(false)

async function onLogin() {
    const t = token.value.trim()
    if (!t) {
        message.warning('请输入 token')
        return
    }
    loading.value = true
    try {
        const ok = await verifyToken(t)
        if (ok) {
            setToken(t)
            message.success('登录成功')
            void router.push('/')
        } else {
            message.error('token 无效或服务器不可达')
        }
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.login-wrap {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>

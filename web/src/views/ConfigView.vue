<template>
    <div>
        <n-space align="center" style="margin-bottom: 12px">
            <h2 style="margin: 0">配置</h2>
            <n-button type="primary" size="small" :loading="saving" @click="save">保存配置</n-button>
            <n-button size="small" :loading="loading" @click="load">重新加载</n-button>
        </n-space>
        <n-alert type="warning" style="margin-bottom: 12px">
            server.port 修改后服务器自动零停机切换（约 0.5s），当前页面可能短暂失联；
            server.token 修改即时生效，需用新 token 重新登录。敏感字段显示为掩码，不修改请保持原样。
        </n-alert>

        <n-spin :show="loading">
            <template v-if="form">
                <n-card title="Bridge（音箱指令）" size="small" style="margin-bottom: 12px">
                    <n-form label-placement="left" label-width="140">
                        <n-form-item label="baseUrl">
                            <n-input v-model:value="form.bridge.baseUrl" />
                        </n-form-item>
                        <n-form-item label="token">
                            <n-input v-model:value="form.bridge.token" type="password" show-password-on="click" placeholder="显示为掩码，不修改请保持原样" />
                        </n-form-item>
                        <n-form-item label="timeoutMs">
                            <n-input-number v-model:value="form.bridge.timeoutMs" :min="0" />
                        </n-form-item>
                        <n-form-item label="retry">
                            <n-input-number v-model:value="form.bridge.retry" :min="0" />
                        </n-form-item>
                        <n-form-item label="ttsSpeaker">
                            <n-input v-model:value="form.bridge.ttsSpeaker" placeholder="豆包音色 ID，留空用小爱原生 TTS" />
                        </n-form-item>
                    </n-form>
                </n-card>

                <n-card title="Server（内嵌 HTTP 服务器）" size="small" style="margin-bottom: 12px">
                    <n-form label-placement="left" label-width="140">
                        <n-form-item label="port">
                            <n-input-number v-model:value="form.server.port" :min="1" :max="65535" />
                        </n-form-item>
                        <n-form-item label="token">
                            <n-input v-model:value="form.server.token" type="password" show-password-on="click" placeholder="显示为掩码，不修改请保持原样" />
                        </n-form-item>
                        <n-form-item label="allowLanOnly">
                            <n-switch v-model:value="form.server.allowLanOnly" />
                        </n-form-item>
                    </n-form>
                </n-card>

                <n-card title="Home Assistant" size="small" style="margin-bottom: 12px">
                    <n-form label-placement="left" label-width="140">
                        <n-form-item label="baseUrl">
                            <n-input v-model:value="form.ha.baseUrl" />
                        </n-form-item>
                        <n-form-item label="token">
                            <n-input v-model:value="form.ha.token" type="password" show-password-on="click" placeholder="显示为掩码，不修改请保持原样" />
                        </n-form-item>
                        <n-form-item label="timeoutMs">
                            <n-input-number v-model:value="form.ha.timeoutMs" :min="0" />
                        </n-form-item>
                        <n-form-item label="retry">
                            <n-input-number v-model:value="form.ha.retry" :min="0" />
                        </n-form-item>
                    </n-form>
                </n-card>

                <n-card title="LLM（DeepSeek）" size="small" style="margin-bottom: 12px">
                    <n-form label-placement="left" label-width="140">
                        <n-form-item label="baseUrl">
                            <n-input v-model:value="form.llm.baseUrl" />
                        </n-form-item>
                        <n-form-item label="apiKey">
                            <n-input v-model:value="form.llm.apiKey" type="password" show-password-on="click" placeholder="显示为掩码，不修改请保持原样" />
                        </n-form-item>
                        <n-form-item label="model">
                            <n-input v-model:value="form.llm.model" />
                        </n-form-item>
                        <n-form-item label="timeoutMs">
                            <n-input-number v-model:value="form.llm.timeoutMs" :min="0" />
                        </n-form-item>
                        <n-form-item label="maxTokens">
                            <n-input-number v-model:value="form.llm.maxTokens" :min="1" />
                        </n-form-item>
                    </n-form>
                </n-card>

                <n-card title="Smart Home（智能回家）" size="small" style="margin-bottom: 12px">
                    <n-form label-placement="left" label-width="140">
                        <n-form-item label="temperatureSensor">
                            <n-input v-model:value="form.smartHome.temperatureSensor" placeholder="室内温度传感器 entity_id" />
                        </n-form-item>
                        <n-form-item label="sunEntity">
                            <n-input v-model:value="form.smartHome.sunEntity" placeholder="如 sun.sun" />
                        </n-form-item>
                        <n-form-item label="acEntityId">
                            <n-input v-model:value="form.smartHome.acEntityId" placeholder="空调 entity_id" />
                        </n-form-item>
                        <n-form-item label="lightEntityId">
                            <n-input v-model:value="form.smartHome.lightEntityId" placeholder="客厅灯 entity_id" />
                        </n-form-item>
                        <n-form-item label="temperatureThreshold">
                            <n-input-number v-model:value="form.smartHome.temperatureThreshold" />
                        </n-form-item>
                        <n-form-item label="acTargetTemp">
                            <n-input-number v-model:value="form.smartHome.acTargetTemp" />
                        </n-form-item>
                        <n-form-item label="acHvacMode">
                            <n-input v-model:value="form.smartHome.acHvacMode" placeholder="如 cool" />
                        </n-form-item>
                        <n-form-item label="weatherEntity">
                            <n-input v-model:value="form.smartHome.weatherEntity" placeholder="天气 entity_id，可留空" clearable />
                        </n-form-item>
                    </n-form>
                </n-card>
            </template>
        </n-spin>
    </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import {
    NAlert,
    NButton,
    NCard,
    NForm,
    NFormItem,
    NInput,
    NInputNumber,
    NSpace,
    NSpin,
    NSwitch,
    useMessage,
} from 'naive-ui'
import { api, type AppConfig, type Rule } from '../api/client'

const message = useMessage()

const form = ref<AppConfig | null>(null)
// 加载时的 rules 原值，提交 config 时原样带回（不在本页编辑）
let originalRules: Rule[] = []
const loading = ref(false)
const saving = ref(false)

async function load() {
    loading.value = true
    try {
        const config = await api.getConfig()
        originalRules = config.rules ?? []
        form.value = config
    } catch (e) {
        message.error(`加载配置失败：${(e as Error).message}`)
    } finally {
        loading.value = false
    }
}

async function save() {
    if (!form.value) return
    saving.value = true
    try {
        await api.saveConfig({ ...form.value, rules: originalRules })
        message.success('配置已保存')
    } catch (e) {
        message.error(`保存失败：${(e as Error).message}`)
    } finally {
        saving.value = false
    }
}

onMounted(() => void load())
</script>

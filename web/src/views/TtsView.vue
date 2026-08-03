<template>
    <div>
        <n-space align="center" style="margin-bottom: 12px">
            <h2 style="margin: 0">音色设置</h2>
            <n-button size="small" :loading="loading" @click="load">刷新</n-button>
        </n-space>

        <n-card size="small" style="max-width: 560px">
            <n-spin :show="loading">
                <n-space vertical style="width: 100%">
                    <n-form-item label="当前音色" label-placement="left">
                        <n-select
                            v-model:value="selected"
                            :options="voiceOptions"
                            filterable
                            placeholder="选择音色"
                            style="width: 380px"
                        />
                    </n-form-item>
                    <n-space>
                        <n-button type="primary" :loading="saving" :disabled="!selected" @click="save">保存音色</n-button>
                        <n-button :loading="previewing" :disabled="!selected" @click="preview">试听</n-button>
                    </n-space>
                    <n-alert v-if="voiceOptions.length === 0" type="info">
                        未获取到可用音色列表（voices 为空），可能是 bridge 未配置豆包 TTS。
                    </n-alert>
                </n-space>
            </n-spin>
        </n-card>
    </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { NAlert, NButton, NCard, NFormItem, NSelect, NSpace, NSpin, useMessage } from 'naive-ui'
import { api } from '../api/client'

const message = useMessage()

const current = ref('')
const voices = ref<Record<string, string>>({})
const selected = ref<string | null>(null)
const loading = ref(false)
const saving = ref(false)
const previewing = ref(false)

const voiceOptions = computed(() => {
    const opts = Object.entries(voices.value).map(([id, name]) => ({
        label: current.value === id ? `${name}（当前）` : name,
        value: id,
    }))
    // voices 为空时至少保证当前音色可选中显示
    if (opts.length === 0 && current.value) {
        opts.push({ label: `${current.value}（当前）`, value: current.value })
    }
    return opts
})

async function load() {
    loading.value = true
    try {
        const info = await api.getTts()
        current.value = info.current
        voices.value = info.voices ?? {}
        selected.value = info.current || null
    } catch (e) {
        message.error(`加载音色失败：${(e as Error).message}`)
    } finally {
        loading.value = false
    }
}

async function save() {
    if (!selected.value) return
    saving.value = true
    try {
        await api.saveTts(selected.value)
        current.value = selected.value
        message.success('音色已保存')
    } catch (e) {
        message.error(`保存失败：${(e as Error).message}`)
    } finally {
        saving.value = false
    }
}

async function preview() {
    previewing.value = true
    try {
        await api.dispatchActions([{ type: 'bridgePlayText', text: '你好，这是音色试听' }])
        message.success('已下发试听指令')
    } catch (e) {
        message.error(`试听失败：${(e as Error).message}`)
    } finally {
        previewing.value = false
    }
}

onMounted(() => void load())
</script>

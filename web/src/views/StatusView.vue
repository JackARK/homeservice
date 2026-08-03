<template>
    <div>
        <n-space align="center" style="margin-bottom: 12px">
            <h2 style="margin: 0">状态看板</h2>
            <n-button size="small" :loading="loading" @click="refresh">手动刷新</n-button>
            <span style="color: #888; font-size: 12px">每 10 秒自动刷新</span>
        </n-space>

        <n-grid :cols="4" :x-gap="12" :y-gap="12" responsive="screen">
            <n-gi>
                <n-card title="通知监听授权" size="small">
                    <n-tag :type="status?.listenerEnabled ? 'success' : 'error'">
                        {{ status?.listenerEnabled ? '已授权' : '未授权' }}
                    </n-tag>
                </n-card>
            </n-gi>
            <n-gi>
                <n-card title="Root 可用" size="small">
                    <n-tag :type="status?.rootAvailable ? 'success' : 'error'">
                        {{ status?.rootAvailable ? '可用' : '不可用' }}
                    </n-tag>
                </n-card>
            </n-gi>
            <n-gi>
                <n-card title="Bridge 连通" size="small">
                    <n-tag :type="status?.bridgeReachable ? 'success' : 'error'">
                        {{ status?.bridgeReachable ? '正常' : '不可达' }}
                    </n-tag>
                </n-card>
            </n-gi>
            <n-gi>
                <n-card title="HA 连通" size="small">
                    <n-tag :type="status?.haReachable ? 'success' : 'error'">
                        {{ status?.haReachable ? '正常' : '不可达' }}
                    </n-tag>
                </n-card>
            </n-gi>
            <n-gi>
                <n-card title="规则数" size="small">{{ status?.ruleCount ?? '-' }}</n-card>
            </n-gi>
            <n-gi>
                <n-card title="版本" size="small">{{ status?.version ?? '-' }}</n-card>
            </n-gi>
            <n-gi>
                <n-card title="运行时长" size="small">{{ formatUptime(status?.uptimeMs) }}</n-card>
            </n-gi>
            <n-gi>
                <n-card title="服务端口" size="small">{{ status?.serverPort ?? '-' }}</n-card>
            </n-gi>
        </n-grid>

        <n-card title="快捷操作" size="small" style="margin-top: 12px">
            <n-space>
                <n-button type="primary" @click="showPlayModal = true">测试播报</n-button>
                <n-button :loading="waking" @click="wakeup">唤醒音箱</n-button>
            </n-space>
        </n-card>

        <n-card title="最近通知事件" size="small" style="margin-top: 12px">
            <n-list v-if="events.length" bordered>
                <n-list-item v-for="(ev, i) in events" :key="ev.timeMillis + '-' + i">
                    <n-space vertical :size="4" style="width: 100%">
                        <n-space align="center">
                            <span style="color: #888">{{ formatTime(ev.timeMillis) }}</span>
                            <n-tag size="small" type="info" class="mono">{{ ev.packageName }}</n-tag>
                            <template v-if="ev.matchedRuleIds?.length">
                                <n-tag v-for="rid in ev.matchedRuleIds" :key="rid" size="small" type="success">
                                    {{ rid }}
                                </n-tag>
                            </template>
                            <span v-else style="color: #aaa">-</span>
                        </n-space>
                        <div>
                            <b>{{ ev.title }}</b>
                            <span v-if="ev.text"> — {{ ev.text }}</span>
                        </div>
                    </n-space>
                </n-list-item>
            </n-list>
            <n-empty v-else description="暂无通知事件" />
        </n-card>

        <n-modal v-model:show="showPlayModal" preset="dialog" title="测试播报" positive-text="播报" negative-text="取消" @positive-click="playText">
            <n-input
                v-model:value="playContent"
                type="textarea"
                placeholder="请输入要播报的文字"
                :autosize="{ minRows: 2, maxRows: 5 }"
            />
        </n-modal>
    </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import {
    NButton,
    NCard,
    NEmpty,
    NGi,
    NGrid,
    NInput,
    NList,
    NListItem,
    NModal,
    NSpace,
    NTag,
    useMessage,
} from 'naive-ui'
import { api, type NotifyEvent, type StatusInfo } from '../api/client'

const message = useMessage()

const status = ref<StatusInfo | null>(null)
const loading = ref(false)
const showPlayModal = ref(false)
const playContent = ref('')
const waking = ref(false)

const events = computed<NotifyEvent[]>(() =>
    [...(status.value?.events ?? [])].sort((a, b) => b.timeMillis - a.timeMillis),
)

function formatUptime(ms?: number): string {
    if (ms === undefined) return '-'
    const h = Math.floor(ms / 3_600_000)
    const m = Math.floor((ms % 3_600_000) / 60_000)
    return `${h}h ${m}m`
}

function formatTime(ms: number): string {
    const d = new Date(ms)
    const p = (n: number) => String(n).padStart(2, '0')
    return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`
}

async function refresh() {
    loading.value = true
    try {
        status.value = await api.status()
    } catch (e) {
        message.error(`获取状态失败：${(e as Error).message}`)
    } finally {
        loading.value = false
    }
}

async function playText() {
    const text = playContent.value.trim()
    if (!text) {
        message.warning('请输入播报内容')
        return false
    }
    try {
        await api.dispatchActions([{ type: 'bridgePlayText', text }])
        message.success('已下发播报指令')
        playContent.value = ''
        return true
    } catch (e) {
        message.error(`播报失败：${(e as Error).message}`)
        return false
    }
}

async function wakeup() {
    waking.value = true
    try {
        await api.dispatchActions([{ type: 'bridgeWakeup' }])
        message.success('已下发唤醒指令')
    } catch (e) {
        message.error(`唤醒失败：${(e as Error).message}`)
    } finally {
        waking.value = false
    }
}

let timer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
    void refresh()
    timer = setInterval(() => void refresh(), 10_000)
})

onUnmounted(() => {
    if (timer !== undefined) clearInterval(timer)
})
</script>

<template>
    <div class="logs-page">
        <n-space align="center" style="margin-bottom: 12px">
            <h2 style="margin: 0">日志</h2>
            <n-select v-model:value="level" :options="levelOptions" style="width: 140px" />
            <n-input v-model:value="keyword" placeholder="关键字过滤" clearable style="width: 240px" />
            <n-switch v-model:value="autoScroll">
                <template #checked>自动滚动</template>
                <template #unchecked>不滚动</template>
            </n-switch>
            <n-button size="small" @click="resetAndLoad">重新加载</n-button>
        </n-space>

        <div ref="listRef" class="log-list">
            <div v-for="entry in entries" :key="entry.id" class="log-line">
                <span class="log-time mono">{{ formatTime(entry.timeMillis) }}</span>
                <n-tag size="small" :type="levelTagType(entry.level)" class="log-tag">{{ levelName(entry.level) }}</n-tag>
                <span class="log-tag-name mono">[{{ entry.tag }}]</span>
                <span class="log-msg mono">{{ entry.message }}</span>
            </div>
            <n-empty v-if="!entries.length" description="暂无日志" style="margin-top: 40px" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { NButton, NEmpty, NInput, NSelect, NSpace, NSwitch, NTag, useMessage } from 'naive-ui'
import { api, type LogEntry } from '../api/client'

const message = useMessage()

const PAGE_SIZE = 200

const level = ref(2)
const keyword = ref('')
const autoScroll = ref(true)
const entries = ref<LogEntry[]>([])
const listRef = ref<HTMLElement | null>(null)

let lastId: number | undefined
// 世代计数：筛选变化后丢弃旧的轮询响应，避免竞态
let generation = 0

const levelOptions = [
    { label: '全部', value: 2 },
    { label: 'INFO', value: 4 },
    { label: 'WARN', value: 5 },
    { label: 'ERROR', value: 6 },
]

function levelName(lv: number): string {
    return { 2: 'VERBOSE', 3: 'DEBUG', 4: 'INFO', 5: 'WARN', 6: 'ERROR' }[lv] ?? String(lv)
}

function levelTagType(lv: number): 'default' | 'info' | 'warning' | 'error' {
    if (lv >= 6) return 'error'
    if (lv === 5) return 'warning'
    if (lv === 4) return 'info'
    return 'default'
}

function formatTime(ms: number): string {
    const d = new Date(ms)
    const p = (n: number, len = 2) => String(n).padStart(len, '0')
    return `${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}.${p(d.getMilliseconds(), 3)}`
}

async function scrollToBottom() {
    if (!autoScroll.value) return
    await nextTick()
    const el = listRef.value
    if (el) el.scrollTop = el.scrollHeight
}

async function fetchLogs(full: boolean) {
    const gen = generation
    try {
        const result = await api.logs({
            level: level.value,
            keyword: keyword.value || undefined,
            afterId: full ? undefined : lastId,
            limit: PAGE_SIZE,
        })
        if (gen !== generation) return // 筛选已变化，丢弃本次响应
        if (full) {
            entries.value = result.entries
        } else if (result.entries.length) {
            entries.value = [...entries.value, ...result.entries]
        }
        lastId = result.lastId
        await scrollToBottom()
    } catch (e) {
        if (gen === generation) {
            message.error(`获取日志失败：${(e as Error).message}`)
        }
    }
}

function resetAndLoad() {
    generation++
    entries.value = []
    lastId = undefined
    void fetchLogs(true)
}

watch(level, resetAndLoad)

let keywordTimer: ReturnType<typeof setTimeout> | undefined
watch(keyword, () => {
    if (keywordTimer !== undefined) clearTimeout(keywordTimer)
    keywordTimer = setTimeout(resetAndLoad, 500)
})

let pollTimer: ReturnType<typeof setInterval> | undefined

onMounted(() => {
    void fetchLogs(true)
    pollTimer = setInterval(() => void fetchLogs(false), 3_000)
})

onUnmounted(() => {
    if (pollTimer !== undefined) clearInterval(pollTimer)
    if (keywordTimer !== undefined) clearTimeout(keywordTimer)
})
</script>

<style scoped>
.logs-page {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 32px);
}

.log-list {
    flex: 1;
    overflow-y: auto;
    border: 1px solid #e0e0e6;
    border-radius: 6px;
    padding: 8px;
    background: #fafafc;
}

.log-line {
    display: flex;
    align-items: baseline;
    gap: 8px;
    padding: 2px 0;
    border-bottom: 1px dashed #eee;
}

.log-time {
    color: #888;
    flex-shrink: 0;
}

.log-tag {
    flex-shrink: 0;
}

.log-tag-name {
    color: #666;
    flex-shrink: 0;
}

.log-msg {
    white-space: pre-wrap;
    word-break: break-all;
}
</style>

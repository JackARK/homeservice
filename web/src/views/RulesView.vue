<template>
    <div>
        <n-space align="center" style="margin-bottom: 12px">
            <h2 style="margin: 0">规则管理</h2>
            <n-button type="primary" size="small" @click="openEditor(null)">新增规则</n-button>
            <n-button size="small" :loading="loading" @click="loadRules">刷新</n-button>
        </n-space>

        <n-data-table :columns="columns" :data="rules" :loading="loading" :row-key="(r: Rule) => r.id" />

        <n-drawer v-model:show="drawerVisible" :width="640">
            <n-drawer-content :title="editingIndex === null ? '新增规则' : `编辑规则：${draft.id || '(未命名)'}`" closable>
                <n-form label-placement="top">
                    <n-form-item label="规则 ID（必填）" required>
                        <n-input v-model:value="draft.id" placeholder="如 doorbell-ring" />
                    </n-form-item>
                    <n-form-item label="包名列表（回车添加多个）">
                        <n-dynamic-tags v-model:value="draft.packageNames" />
                    </n-form-item>
                    <n-form-item label="标题正则（titleRegex，留空表示不限制）">
                        <n-input v-model:value="draft.titleRegex" placeholder="如 .*门铃.*" />
                    </n-form-item>
                    <n-form-item label="正文正则（textRegex，留空表示不限制）">
                        <n-input v-model:value="draft.textRegex" />
                    </n-form-item>
                    <n-form-item label="去重窗口（毫秒）">
                        <n-input-number v-model:value="draft.dedupWindowMs" :min="0" style="width: 200px" />
                    </n-form-item>

                    <n-form-item label="动作列表（按数组顺序执行）">
                        <div style="width: 100%">
                            <n-card
                                v-for="(act, i) in draft.actions"
                                :key="i"
                                size="small"
                                style="margin-bottom: 8px"
                                :title="`动作 ${i + 1}`"
                            >
                                <template #header-extra>
                                    <n-button text type="error" @click="draft.actions.splice(i, 1)">删除</n-button>
                                </template>
                                <n-space vertical style="width: 100%">
                                    <n-select v-model:value="act.type" :options="ACTION_TYPE_OPTIONS" placeholder="选择动作类型" />
                                    <template v-if="fieldsOf(act.type).includes('text')">
                                        <n-input v-model:value="act.text" type="textarea" placeholder="播报文字（text）" />
                                    </template>
                                    <template v-if="fieldsOf(act.type).includes('url')">
                                        <n-input v-model:value="act.url" placeholder="音频链接（url）" />
                                    </template>
                                    <template v-if="fieldsOf(act.type).includes('domain')">
                                        <n-input v-model:value="act.domain" placeholder="HA 域（domain），如 light" />
                                    </template>
                                    <template v-if="fieldsOf(act.type).includes('service')">
                                        <n-input v-model:value="act.service" placeholder="HA 服务（service），如 turn_on" />
                                    </template>
                                    <template v-if="fieldsOf(act.type).includes('entityId')">
                                        <n-input v-model:value="act.entityId" placeholder="实体 ID（entityId），如 light.living_room" />
                                    </template>
                                    <template v-if="fieldsOf(act.type).includes('data')">
                                        <n-input
                                            v-model:value="act.dataText"
                                            type="textarea"
                                            placeholder='附加数据（data，JSON 对象，可留空），如 {"field":"value"}'
                                        />
                                    </template>
                                    <span v-if="fieldsOf(act.type).length === 0" style="color: #888; font-size: 12px">
                                        该动作类型无参数
                                    </span>
                                </n-space>
                            </n-card>
                            <n-button dashed block @click="addAction">添加动作</n-button>
                        </div>
                    </n-form-item>
                </n-form>

                <template #footer>
                    <n-space>
                        <n-button type="primary" :loading="saving" @click="saveRule">保存</n-button>
                        <n-button @click="drawerVisible = false">取消</n-button>
                    </n-space>
                </template>
            </n-drawer-content>
        </n-drawer>
    </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue'
import {
    NButton,
    NCard,
    NDataTable,
    NDrawer,
    NDrawerContent,
    NDynamicTags,
    NForm,
    NFormItem,
    NInput,
    NInputNumber,
    NPopconfirm,
    NSelect,
    NSpace,
    NTag,
    useMessage,
} from 'naive-ui'
import type { DataTableColumns } from 'naive-ui'
import { ACTION_FIELDS, ACTION_TYPE_OPTIONS, api, type ActionDef, type Rule } from '../api/client'

const message = useMessage()

const rules = ref<Rule[]>([])
const loading = ref(false)
const saving = ref(false)
const drawerVisible = ref(false)
// null 表示新增，否则为正在编辑的规则在数组中的下标
const editingIndex = ref<number | null>(null)

interface DraftAction {
    type: string
    text: string
    url: string
    domain: string
    service: string
    entityId: string
    dataText: string
}

const draft = reactive({
    id: '',
    packageNames: [] as string[],
    titleRegex: '',
    textRegex: '',
    dedupWindowMs: 60_000,
    actions: [] as DraftAction[],
})

function fieldsOf(type: string): string[] {
    return ACTION_FIELDS[type] ?? []
}

function addAction() {
    draft.actions.push({ type: 'bridgePlayText', text: '', url: '', domain: '', service: '', entityId: '', dataText: '' })
}

function openEditor(index: number | null) {
    editingIndex.value = index
    if (index === null) {
        draft.id = ''
        draft.packageNames = []
        draft.titleRegex = ''
        draft.textRegex = ''
        draft.dedupWindowMs = 60_000
        draft.actions = []
        addAction()
    } else {
        const r = rules.value[index]
        draft.id = r.id
        draft.packageNames = [...r.packageNames]
        draft.titleRegex = r.titleRegex ?? ''
        draft.textRegex = r.textRegex ?? ''
        draft.dedupWindowMs = r.dedupWindowMs
        draft.actions = r.actions.map((a) => ({
            type: a.type,
            text: a.text ?? '',
            url: a.url ?? '',
            domain: a.domain ?? '',
            service: a.service ?? '',
            entityId: a.entityId ?? '',
            dataText: a.data ? JSON.stringify(a.data) : '',
        }))
    }
    drawerVisible.value = true
}

function serializeAction(a: DraftAction): ActionDef | null {
    const fields = fieldsOf(a.type)
    const out: ActionDef = { type: a.type }
    if (fields.includes('text') && a.text) out.text = a.text
    if (fields.includes('url') && a.url) out.url = a.url
    if (fields.includes('domain') && a.domain) out.domain = a.domain
    if (fields.includes('service') && a.service) out.service = a.service
    if (fields.includes('entityId') && a.entityId) out.entityId = a.entityId
    if (fields.includes('data') && a.dataText.trim()) {
        try {
            const parsed: unknown = JSON.parse(a.dataText)
            if (typeof parsed !== 'object' || parsed === null || Array.isArray(parsed)) {
                message.error(`动作 data 必须是 JSON 对象：${a.dataText}`)
                return null
            }
            out.data = parsed as Record<string, unknown>
        } catch {
            message.error(`动作 data 不是合法 JSON：${a.dataText}`)
            return null
        }
    }
    return out
}

async function saveRule() {
    const id = draft.id.trim()
    if (!id) {
        message.error('规则 ID 不能为空')
        return
    }
    const dup = rules.value.some((r, i) => r.id === id && i !== editingIndex.value)
    if (dup) {
        message.error(`规则 ID 重复：${id}`)
        return
    }
    // 正则试编译，编译不过仅警告不阻断（后端同样容忍）
    for (const [label, pattern] of [
        ['标题正则', draft.titleRegex],
        ['正文正则', draft.textRegex],
    ] as const) {
        if (pattern) {
            try {
                new RegExp(pattern)
            } catch (e) {
                message.warning(`${label}无法编译（后端也会匹配失败）：${(e as Error).message}`)
            }
        }
    }
    const actions: ActionDef[] = []
    for (const a of draft.actions) {
        if (!a.type) {
            message.error('存在未选择类型的动作')
            return
        }
        const serialized = serializeAction(a)
        if (!serialized) return
        actions.push(serialized)
    }
    const rule: Rule = {
        id,
        packageNames: draft.packageNames.filter(Boolean),
        titleRegex: draft.titleRegex || null,
        textRegex: draft.textRegex || null,
        actions,
        dedupWindowMs: draft.dedupWindowMs,
    }
    const next = [...rules.value]
    if (editingIndex.value === null) {
        next.push(rule)
    } else {
        next[editingIndex.value] = rule
    }
    saving.value = true
    try {
        await api.saveRules(next)
        rules.value = next
        drawerVisible.value = false
        message.success('规则已保存')
    } catch (e) {
        message.error(`保存失败：${(e as Error).message}`)
    } finally {
        saving.value = false
    }
}

async function deleteRule(index: number) {
    const next = rules.value.filter((_, i) => i !== index)
    try {
        await api.saveRules(next)
        rules.value = next
        message.success('规则已删除')
    } catch (e) {
        message.error(`删除失败：${(e as Error).message}`)
    }
}

const columns: DataTableColumns<Rule> = [
    { title: 'ID', key: 'id' },
    {
        title: '包名',
        key: 'packageNames',
        render(row) {
            return row.packageNames.map((p) =>
                h(NTag, { size: 'small', class: 'mono', style: 'margin-right:4px' }, { default: () => p }),
            )
        },
    },
    { title: '标题正则', key: 'titleRegex', render: (row) => row.titleRegex ?? '-' },
    { title: '正文正则', key: 'textRegex', render: (row) => row.textRegex ?? '-' },
    { title: '动作数', key: 'actionCount', render: (row) => row.actions.length },
    { title: '去重窗口(ms)', key: 'dedupWindowMs' },
    {
        title: '操作',
        key: 'ops',
        render(row, index) {
            return h(NSpace, null, {
                default: () => [
                    h(NButton, { size: 'small', onClick: () => openEditor(index) }, { default: () => '编辑' }),
                    h(
                        NPopconfirm,
                        { onPositiveClick: () => deleteRule(index) },
                        {
                            trigger: () => h(NButton, { size: 'small', type: 'error' }, { default: () => '删除' }),
                            default: () => `确认删除规则 ${row.id}？`,
                        },
                    ),
                ],
            })
        },
    },
]

async function loadRules() {
    loading.value = true
    try {
        rules.value = await api.getRules()
    } catch (e) {
        message.error(`加载规则失败：${(e as Error).message}`)
    } finally {
        loading.value = false
    }
}

onMounted(() => void loadRules())
</script>

import { clearToken, getToken } from '../auth'
import { router } from '../router'

export class ApiError extends Error {
    constructor(
        message: string,
        public status: number,
    ) {
        super(message)
    }
}

async function request<T>(path: string, options: { method?: string; body?: unknown } = {}): Promise<T> {
    const headers: Record<string, string> = {}
    const token = getToken()
    if (token) headers['Authorization'] = `Bearer ${token}`
    let body: string | undefined
    if (options.body !== undefined) {
        headers['Content-Type'] = 'application/json'
        body = JSON.stringify(options.body)
    }
    const resp = await fetch(path, { method: options.method ?? 'GET', headers, body })
    if (resp.status === 401) {
        clearToken()
        if (router.currentRoute.value.path !== '/login') {
            void router.push('/login')
        }
        throw new ApiError('未授权或 token 已失效，请重新登录', 401)
    }
    const data: unknown = await resp.json().catch(() => ({}))
    if (!resp.ok) {
        const msg = (data as { error?: string }).error ?? `HTTP ${resp.status}`
        throw new ApiError(msg, resp.status)
    }
    return data as T
}

// 用候选 token 试调一个需鉴权接口，验证其有效性（登录用，此时 token 尚未入库）
export async function verifyToken(token: string): Promise<boolean> {
    try {
        const resp = await fetch('/api/rules', {
            headers: { Authorization: `Bearer ${token}` },
        })
        return resp.ok
    } catch {
        return false
    }
}

// ---------- 类型定义 ----------

export interface ActionDef {
    type: string
    text?: string | null
    url?: string | null
    domain?: string | null
    service?: string | null
    entityId?: string | null
    data?: Record<string, unknown> | null
}

export interface Rule {
    id: string
    packageNames: string[]
    titleRegex: string | null
    textRegex: string | null
    actions: ActionDef[]
    dedupWindowMs: number
}

export interface NotifyEvent {
    timeMillis: number
    packageName: string
    title: string
    text: string
    matchedRuleIds: string[]
}

export interface StatusInfo {
    status: string
    uptimeMs: number
    version: string
    rootAvailable: boolean
    listenerEnabled: boolean
    ruleCount: number
    bridgeReachable: boolean
    haReachable: boolean
    serverPort: number
    events: NotifyEvent[]
}

export interface LogEntry {
    id: number
    timeMillis: number
    level: number
    tag: string
    message: string
}

export interface LogResult {
    entries: LogEntry[]
    lastId: number
}

export interface AppConfig {
    bridge: {
        baseUrl: string
        token: string
        timeoutMs: number
        retry: number
        ttsSpeaker: string
    }
    server: {
        port: number
        token: string
        allowLanOnly: boolean
    }
    ha: {
        baseUrl: string
        token: string
        timeoutMs: number
        retry: number
    }
    llm: {
        baseUrl: string
        apiKey: string
        model: string
        timeoutMs: number
        maxTokens: number
    }
    smartHome: {
        temperatureSensor: string
        sunEntity: string
        acEntityId: string
        lightEntityId: string
        temperatureThreshold: number
        acTargetTemp: number
        acHvacMode: string
        weatherEntity: string | null
    }
    rules: Rule[]
}

export interface TtsInfo {
    current: string
    voices: Record<string, string>
}

// 动作类型与所需字段（供规则编辑器动态渲染表单）
export const ACTION_TYPE_OPTIONS = [
    { label: 'bridge 播报文字（bridgePlayText）', value: 'bridgePlayText' },
    { label: 'bridge 播放音频链接（bridgePlayUrl）', value: 'bridgePlayUrl' },
    { label: 'bridge 唤醒音箱（bridgeWakeup）', value: 'bridgeWakeup' },
    { label: 'bridge 打断播放（bridgeInterrupt）', value: 'bridgeInterrupt' },
    { label: 'HA 打开（haTurnOn）', value: 'haTurnOn' },
    { label: 'HA 关闭（haTurnOff）', value: 'haTurnOff' },
    { label: 'HA 切换（haToggle）', value: 'haToggle' },
    { label: 'HA 调用服务（haCallService）', value: 'haCallService' },
    { label: '智能回家（welcomeHome）', value: 'welcomeHome' },
]

export const ACTION_FIELDS: Record<string, string[]> = {
    bridgePlayText: ['text'],
    bridgePlayUrl: ['url'],
    bridgeWakeup: [],
    bridgeInterrupt: [],
    haTurnOn: ['entityId'],
    haTurnOff: ['entityId'],
    haToggle: ['entityId'],
    haCallService: ['domain', 'service', 'entityId', 'data'],
    welcomeHome: [],
}

// ---------- API 封装 ----------

export const api = {
    health: () => request<{ status: string }>('/api/health'),
    status: () => request<StatusInfo>('/api/status'),
    logs: (params: { level?: number; keyword?: string; afterId?: number; limit?: number }) => {
        const q = new URLSearchParams()
        if (params.level !== undefined) q.set('level', String(params.level))
        if (params.keyword) q.set('keyword', params.keyword)
        if (params.afterId !== undefined) q.set('afterId', String(params.afterId))
        if (params.limit !== undefined) q.set('limit', String(params.limit))
        const qs = q.toString()
        return request<LogResult>(`/api/logs${qs ? `?${qs}` : ''}`)
    },
    getRules: () => request<Rule[]>('/api/rules'),
    saveRules: (rules: Rule[]) => request<unknown>('/api/rules', { method: 'POST', body: rules }),
    getConfig: () => request<AppConfig>('/api/config'),
    saveConfig: (config: AppConfig) => request<unknown>('/api/config', { method: 'POST', body: config }),
    getTts: () => request<TtsInfo>('/api/tts'),
    saveTts: (speaker: string) => request<unknown>('/api/tts', { method: 'POST', body: { speaker } }),
    dispatchActions: (actions: ActionDef[]) =>
        request<{ status: string; count: number }>('/api/action', { method: 'POST', body: actions }),
}

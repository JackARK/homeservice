const TOKEN_KEY = 'hs_token'

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
    localStorage.removeItem(TOKEN_KEY)
}

// App 内 WebView 免密注入场景：URL query 带 ?token=xxx，读取后清掉地址栏参数
export function absorbTokenFromUrl(): void {
    const params = new URLSearchParams(window.location.search)
    const token = params.get('token')
    if (!token) return
    setToken(token)
    params.delete('token')
    const query = params.toString()
    history.replaceState(null, '', location.pathname + (query ? `?${query}` : '') + location.hash)
}

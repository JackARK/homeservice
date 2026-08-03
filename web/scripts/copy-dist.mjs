// 把 web/dist 整体复制到 Android assets（先清空目标目录）
import { cpSync, existsSync, mkdirSync, rmSync } from 'node:fs'
import { dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'

const here = dirname(fileURLToPath(import.meta.url))
const src = join(here, '..', 'dist')
const dest = join(here, '..', '..', 'app', 'src', 'main', 'assets', 'web')

if (!existsSync(src)) {
    console.error('dist 目录不存在，请先执行 vite build')
    process.exit(1)
}

rmSync(dest, { recursive: true, force: true })
mkdirSync(dest, { recursive: true })
cpSync(src, dest, { recursive: true })
console.log(`已复制构建产物：${src} -> ${dest}`)

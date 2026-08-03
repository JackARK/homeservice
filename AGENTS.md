# AGENTS.md — serviceAPK 项目指南

> 本文件供 AI 编程助手（Kimi Code 等）阅读。修改本项目代码前请通读本文件。

## 1. 项目概述

在闲置的红米 K40 Gaming（已 root，长期插电）上运行的**服务型 Android 应用**，无人值守长期运行。

核心能力：

1. **通知监听**：捕获智能家居 APP 的通知，通过规则匹配触发动作。
2. **指令下发**：向小米音箱（刷有 open-xiaoai-bridge）发送 HTTP 指令，控制音箱播放/执行特定功能。
3. **内嵌服务器**：充当局域网 HTTP 服务器，接收外部请求触发动作或查询状态。

本应用**不上架商店**，通过 sideload 安装，仅供个人自用。

## 2. 硬件与软件环境

| 项目 | 说明 |
|------|------|
| 设备 | 红米 K40 Gaming（天玑 1200），型号 `M2012K10C`，ROM 代号 `ares`，序列号 `ssnfkz7hxggu9px4` |
| 系统 | **HyperOS 1.0 / MIUI `V816.0.4.0.TKJCNXM`（基于 Android 13，API 33）** |
| Root | **已刷 Magisk**，`su -c` 可直接拿 `uid=0(root)`，SELinux 域 `u:r:magisk:s0` |
| 电源 | 长期插电，无需考虑功耗优化 |
| 外设 | 小米音箱（已刷 open-xiaoai-client，见 §2.2） |
| 网络 | 局域网 `192.168.5.0/24`，所有设备同网段 |

### 2.1 首批监听目标（包名已确认）

| APP | 包名 | 备注 |
|-----|------|------|
| 360 云智联 | **`com.visualworld.ecology`** | 已在设备上用 `adb shell pm path` 验证，versionName `2.23.0`。注意：与旧候选 `com.qihoo.smart` / `com.visualworld.cloudsmart` 均不符，实际后缀是 `.ecology` |
| （参照）涂鸦智能 IoT | `com.tuya.smartiot` | 设备上亦存在，非首批目标 |
| （参照）米家 | `com.xiaomi.smarthome` | 设备上亦存在，非首批目标 |

> 复核命令：
> ```bash
> adb shell pm list packages | grep -iE "visualworld|ecology|tuya|smarthome"
> ```

### 2.2 网络拓扑（全部已实测）

| 角色 | IP | 说明 |
|------|-----|------|
| 目标手机（K40） | `192.168.5.131` | 跑本应用，wlan0 |
| 小米音箱 | `192.168.5.6` | dropbear SSH（`SSH-2.0-dropbear`），已刷 open-xiaoai-client |
| 电视盒子（bridge 宿主） | `192.168.5.50` | 见 §2.3，跑 open-xiaoai-bridge + Home Assistant |
| 开发机 | `192.168.5.71` | 开发调试用，与设备同网段 |

### 2.3 bridge 宿主：电视盒子（`192.168.5.50`）

open-xiaoai-bridge **部署在此盒子**（不在音箱、不在手机）。已实测规格：

| 项 | 规格 |
|----|------|
| SoC | 瑞芯微 RK35xx（约 RK3568 级），4 核 Cortex-A55 @ 2.0GHz |
| 内存 | 3.8 GiB（已跑 HA + wfwp，仍空闲约 2.0G） |
| 存储 | 115G eMMC，剩余约 107G |
| 系统 | Armbian 25.05 / Ubuntu 22.04 (Jammy)，aarch64，内核 `6.1.99-vendor-rk35xx` |
| 运行时 | **Docker 29.5.3 已装**，Python 3.10，已开机 27 天，负载 ~0.19 |
| 已有容器 | `home-assistant:stable`（8123）、`wfwp:latest`（8080→3000） |
| SSH | `jack@192.168.5.50` |
| 镜像支持 | 官方 `ghcr.io/coderzc/open-xiaoai-bridge:latest` manifest **含 `arm64/linux`**，可直接 docker compose 部署 |

**资源结论**：AI 全走云（见 §5.4 策略）时，盒子本地仅做 VAD/KWS 轻量推理 + 音频转发，4G RAM 的 RK3568 性能过剩，绰绰有余。

### 2.4 bridge 部署状态（已上线，端到端验证通过）

bridge 已用 docker compose 部署到盒子 `~/open-xiaoai-bridge/`，TTS 播放链路 `HTTP API → bridge → 音箱` 实测发声正常。

| 项 | 状态 / 位置 |
|----|------|
| 部署目录 | 盒子 `~/open-xiaoai-bridge/`（`docker-compose.yml` + `config.py` + `models/`） |
| 容器名 | `open-xiaoai-bridge-open-xiaoai-bridge-1`，`restart: unless-stopped` |
| 端口 | `9092`（HTTP API，监听 `0.0.0.0`）、`4399`（WebSocket，接音箱 client） |
| 鉴权 | **已启用 `OPEN_XIAOAI_TOKEN`**（WebSocket client 鉴权），值存放于盒子 `docker-compose.yml` 与音箱 `/data/open-xiaoai/token.txt`，两者必须一致 |
| 模型 | `models/` 来自 [vad-kws-asr-models release](https://github.com/coderzc/open-xiaoai-bridge/releases/tag/vad-kws-asr-models)（约 470MB） |
| 音箱 client | 二进制 `/data/open-xiaoai/client`，由开机脚本 `/data/init.sh` 启动，读 `server.txt`（=`ws://192.168.5.50:4399`）与 `token.txt` |
| 音箱 SSH | `root@192.168.5.6`，dropbear，需 `-o HostKeyAlgorithms=+ssh-rsa`，密码为刷机默认值 |

**运维命令**：
```bash
# 盒子：查 bridge 日志 / 重启
ssh jack@192.168.5.50 'cd ~/open-xiaoai-bridge && docker compose logs --tail=50'
ssh jack@192.168.5.50 'cd ~/open-xiaoai-bridge && docker compose restart'

# 音箱：重启 client（改完 server.txt/token.txt 后必须重启 client 才生效）
ssh -o HostKeyAlgorithms=+ssh-rsa root@192.168.5.6 \
  'kill -9 $(ps | grep "/data/open-xiaoai/client" | grep -v grep | awk "{print \$1}"); \
   cd /data/open-xiaoai && export OPEN_XIAOAI_TOKEN=$(cat token.txt) && ./client "$(cat server.txt)" >/dev/null 2>&1 &'

# 连通性自测（开发机执行，返回 success 且音箱出声 = 链路正常）
curl http://192.168.5.50:9092/api/health
curl -X POST http://192.168.5.50:9092/api/play/text -H "Content-Type: application/json" -d '{"text":"测试"}'
```

> ⚠️ 踩坑记录：
> - 盒子 Docker 原配了失效代理 `127.0.0.1:7890`（`/etc/systemd/system/docker.service.d/http-proxy.conf` + `~/.docker/config.json`），拉镜像会失败，已禁用。
> - 音箱 busybox **无 `nohup`/`setsid`/标准 `timeout`**，client 后台启动用 `./client ... &`（init.sh 原生方式）。
> - `/api/play/text` 返回 `{"success":true}` 只代表 bridge 接受请求，**不代表音箱发声**；判断链路是否真通的唯一标准是"音箱是否物理出声"。

### 2.5 开发机 adb（已配置）

- adb：`~/android-sdk/platform-tools/adb`，版本 1.0.41
- udev 规则：`/etc/udev/rules.d/51-android.rules`，内容 `SUBSYSTEM=="usb", ATTR{idVendor}=="2717", MODE="0666", GROUP="plugdev"`（Xiaomi vendor ID `2717`）
- 设备已信任本机（手机端勾选"始终允许 USB 调试"）

## 3. 技术栈

- **语言**：Kotlin（不使用 Java）
- **SDK**（以 `app/build.gradle.kts` 实际值为准）：`minSdk = 33`、`targetSdk = 37`、`compileSdk = 37`，**AGP 9.3.1**
- 无需向下兼容，本应用只在这一台手机上运行（minSdk 锁 33）
- **构建工具**：Gradle + Kotlin DSL（`build.gradle.kts`），版本目录 `gradle/libs.versions.toml`
- **命名空间 / applicationId**：`club.saltfish.homeservice`
- **异步**：Kotlin Coroutines + Flow
- **HTTP 客户端**：OkHttp（发指令到 bridge）
- **HTTP 服务器**：**NanoHTTPD 2.3.1**（内嵌 REST，默认端口 8888，见 §5.5）
- **JSON**：**Gson 2.14**
- **Root**：**libsu 6.0**（com.github.topjohnwu，见 §5.6）
- **日志**：Timber 5.0
- **序列化配置**：规则配置用 JSON

### 依赖确认原则

不要假设某个库可用。引入新依赖前，先检查 `app/build.gradle.kts` 的 `dependencies` 块与 `gradle/libs.versions.toml`，遵循已有选型。确实需要引入新库时，在 commit 中说明原因。

## 4. 项目结构（建议）

```
homeservice/
├── AGENTS.md                  # 本文件
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── kotlin/club/saltfish/homeservice/
│       │   │   ├── App.kt                    # Application 入口
│       │   │   ├── notification/             # 通知监听与解析
│       │   │   ├── rule/                     # 规则引擎（纯逻辑，可单测）
│       │   │   ├── action/                   # 动作执行（发指令、root命令等）
│       │   │   ├── bridge/                   # open-xiaoai-bridge 通信
│       │   │   ├── ha/                       # Home Assistant 通信（控制米家设备）
│       │   │   ├── llm/                      # LLM 客户端（DeepSeek，生成播报文本）
│       │   │   ├── smart/                    # 智能场景编排（welcomeHome）
│       │   │   ├── server/                   # 内嵌 HTTP 服务器
│       │   │   ├── root/                     # Root 操作封装（集中管理）
│       │   │   ├── keepalive/               # MIUI 保活策略
│       │   │   ├── config/                  # 配置加载与管理
│       │   │   └── log/                     # 日志配置
│       │   └── res/
│       └── test/
│           └── kotlin/club/saltfish/homeservice/   # 单元测试（至少覆盖 rule/）
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

## 5. 架构与模块职责

模块间通过**接口**通信，降低耦合，便于测试和替换。

### 5.1 notification — 通知监听
- 基于 `NotificationListenerService`（标准 Android API，**不需要 root 即可监听通知**）
- Root 的作用仅限：自动授权 listener 权限（`cmd notification allow_listener`）、保活
- 解析 `StatusBarNotification`，提取包名、标题、文本、时间，交给规则引擎

### 5.2 rule — 规则引擎（核心纯逻辑）
- 输入：解析后的通知结构体
- 输出：匹配的动作列表（或空）
- 匹配方式：包名过滤 + 标题/正文正则匹配
- **必须脱离 Android 框架可独立单测**（不依赖 Context、不依赖 Android 设备）

### 5.3 action — 动作执行
- 接收规则引擎输出的动作列表，由 `ActionDispatcher` 按 `ActionDef.type` 字符串分发
- 顺序执行，单个动作失败不影响后续动作
- 已实现动作类型（type 取值）：
  - bridge：`bridgePlayText`（TTS，需 `text`）、`bridgePlayUrl`（音频，需 `url`）、`bridgeWakeup`、`bridgeInterrupt`
  - ha：`haTurnOn`/`haTurnOff`/`haToggle`（需 `entityId`，域从前缀自动提取）、`haCallService`（需 `domain`+`service`，可选 `entityId`、`data`）
  - smart：`welcomeHome`（智能回家，按室温/日落动态决定开空调与客厅灯，DeepSeek 生成欢迎语播报）

### 5.4 bridge — open-xiaoai-bridge 通信

封装 bridge 的 HTTP API（默认端口 **9092**，需 `API_SERVER_ENABLE=1` 启用，可选 `OPEN_XIAOAI_TOKEN` 鉴权）。

**端点列表**（来源：[coderzc/open-xiaoai-bridge](https://github.com/coderzc/open-xiaoai-bridge)）：

| 方法 | 路径 | 说明 | Body |
|------|------|------|------|
| POST | `/api/play/text` | TTS 播放文字（**本模块最常用**） | `{"text":"..."}` |
| POST | `/api/play/url` | 播放音频链接 | `{"url":"..."}` |
| POST | `/api/play/file` | 上传并播放音频文件 | multipart `file=@...` |
| POST | `/api/tts/doubao` | 豆包 TTS 合成并播放 | `{"text":"...","speaker_id":"..."}` |
| GET | `/api/tts/doubao_voices` | 获取可用音色列表 | — |
| POST | `/api/wakeup` | 唤醒小爱音箱 | — |
| POST | `/api/interrupt` | 打断当前播放 | — |
| GET | `/api/status` | 获取播放状态 | — |
| GET | `/api/health` | 健康检查（连通性探测用） | — |

**连通性探测示例**：
```bash
curl http://192.168.5.50:9092/api/health
curl -X POST http://192.168.5.50:9092/api/play/text \
  -H "Content-Type: application/json" -d '{"text":"测试"}'
```

模块要求：含重试与超时机制；地址（`http://192.168.5.50:9092`）与 token 走配置文件（§6.4），**不硬编码**。

**音色可配置（已实现）**：`BridgeConfig.ttsSpeaker` 持有豆包音色 ID（默认 `zh_female_vv_uranus_bigtts`）。`playText` 在 `ttsSpeaker` 非空时走 `/api/tts/doubao`（带 `speaker_id`），留空回退 `/api/play/text`（小爱原生）。`listVoices()` 封装 `/api/tts/doubao_voices` 拉取火山音色库。盒子侧 `config.py` 的 `tts.doubao.app_id`/`access_key` 对应火山控制台的 **APP ID / Access Token**（注意：是 Access Token，不是 Secret Key；`access_key` 填错会报 `45000010 load grant not found`）。

**部署侧关键约束**：bridge 默认 `API_SERVER_HOST=127.0.0.1`（仅本机），部署到盒子后**必须设 `API_SERVER_HOST=0.0.0.0`**，否则手机/局域网无法访问 9092。

### 5.5 ha — Home Assistant 通信（控制米家设备）

封装 Home Assistant REST API（盒子默认端口 **8123**），用于控制接入 HA 的米家等设备。**bridge 只能驱动音箱，控制设备走本模块**。HA 部署见 §2.3。

**鉴权**：所有调用附带 `Authorization: Bearer <长期访问令牌>`。令牌在 `http://192.168.5.50:8123/profile` 生成后填入配置（§6.4），**不硬编码**。

**核心端点**：

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/services/<domain>/<service>` | 调用服务（控设备核心，如 `light.turn_on`、`switch.toggle`、`xiaomi_miot.set_property`） |
| GET | `/api/states/<entity_id>` | 查询实体状态（返回 state 字符串） |
| GET | `/api/` | 健康检查（需 token） |

**便捷封装**：`turnOn/turnOff/toggle(entityId)` 从 `entity_id` 前缀提取 domain（HA 约定 `<domain>.<object_id>`），调用 `<domain>.turn_on/turn_off/toggle`；复杂场景（带 field/value 的 `set_property`、`script.turn_on` 等）用通用 `callService(domain, service, data)`。

**连通性探测示例**：
```bash
curl -H "Authorization: Bearer TOKEN" http://192.168.5.50:8123/api/
curl -H "Authorization: Bearer TOKEN" "http://192.168.5.50:8123/api/services/light/turn_on" \
  -H "Content-Type: application/json" -d '{"entity_id":"light.xxx"}'
```

模块要求：含重试与超时机制（与 bridge 一致，默认 10s/3 次指数退避）；地址与 token 走配置（§6.4）。

### 5.6 server — 内嵌 HTTP 服务器
- 暴露 REST API：`GET/POST /rules`（规则）、`GET/POST /config`（整包配置）、`GET/POST /tts`（音色：GET 返回当前音色+可用音色列表，POST 单独改音色 `{"speaker":"<音色ID>"}`）、`POST /action`（触发动作）、`GET /health`
- **绑定局域网 IP，不暴露公网**
- 需要简单鉴权（Token 或局域网 IP 白名单）

### 5.7 root — Root 操作（集中管理）
- 所有需要 root 权限的操作统一在此模块
- 通过 libsu 库或 `Runtime.exec("su")` 执行（待定，见 §10）
- 包括：自动授权通知监听、电池优化白名单、冻结/解冻应用等

### 5.8 keepalive — MIUI 保活
- 前台服务（Foreground Service）+ 常驻通知
- 电池优化白名单（root 自动添加）
- 自启动权限（root 自动授权）
- 服务崩溃自动重启（通过 `START_STICKY` + JobScheduler 守护）

### 5.9 llm — LLM 客户端（DeepSeek）

封装 DeepSeek（OpenAI 兼容）文本生成接口，用于生成自然语言播报。端点 `POST {baseUrl}/chat/completions`，鉴权 `Authorization: Bearer {apiKey}`。

- 默认模型 `deepseek-v4-flash`（快速），请求带 `thinking:{type:disabled}` 关闭推理，追求低延迟
- 不重试（LLM 响应慢），失败由调用方降级
- apiKey 与 HA token 一样**只存运行时 config.json，不进仓库**

### 5.10 smart — 智能场景编排

`WelcomeHomeOrchestrator` 编排「智能回家」全流程（`welcomeHome` 动作）：

1. **并行查 HA** 环境状态：室内温（`temperatureSensor`）、天色（`sunEntity`）、天气（`weatherEntity`，可选）、空调模式、灯状态
2. **决策**：室温 ≥ `temperatureThreshold` 且空调未运行 → `climate.set_temperature` 开空调；`sun` 为 `below_horizon` 且灯 off → 开客厅灯
3. **执行** HA 动作
4. **组装上下文**（日期/时刻/天色/天气/室温/实际操作结果）调 DeepSeek 生成欢迎语
5. **bridge.playText** 播报

降级原则：任一状态读不到则跳过该项决策（温度读不到保守不开空调）；LLM 失败用兜底语「欢迎回家」；不阻断整体。entity/阈值/天气源全走 `SmartHomeConfig`（§6.4），配好 HA 天气集成后填 `weatherEntity` 即可纳入天气描述。

## 6. 硬性要求

### 6.1 可靠性（无人值守，最高优先级）
- **绝不吞异常**：所有 `catch` 块必须有日志输出，不允许空 catch
- **网络请求必须有超时和重试**：默认超时 10s，重试 3 次，指数退避
- **服务必须能自恢复**：崩溃后自动重启，依赖 `START_STICKY`
- **关键路径必须有日志**：通知接收、规则匹配、动作执行、指令发送，每一步都要记日志

### 6.2 安全性
- HTTP 服务器**只在局域网内监听**，不监听 `0.0.0.0` 除非有鉴权
- Root 命令**不允许拼接用户输入**，防止命令注入
- 配置文件中不硬编码敏感信息（Token、密码等），用环境变量或单独的 `.local` 文件

### 6.3 Root 使用原则
- **能用标准 Android API 实现的，不用 root**
- Root 仅用于：自动授权、保活、系统级操作
- 所有 root 操作集中在 `root/` 模块，其他模块不直接执行 `su`

### 6.4 配置外部化
- 规则、端口、包名列表、bridge 地址等全部走配置文件
- 配置文件放在应用内部存储，支持通过 HTTP API 热更新
- **不在代码中硬编码任何设备特定信息**（IP地址、包名、端口等）

## 7. 编码规范

### 7.1 语言与风格
- 全程使用 Kotlin，不引入 Java 文件
- 遵循 [Kotlin 官方编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 4 空格缩进，行宽上限 120 字符
- **注释用中文**，代码标识符用英文

### 7.2 命名
- 类名 PascalCase，函数/变量 camelCase，常量 UPPER_SNAKE_CASE
- 包名全小写，不包含下划线
- 语义清晰优先于简短：`bridgeCommandSender` 优于 `sender`

### 7.3 日志
- 使用 Timber，**不用** `Log.d` / `println`
- 日志要包含上下文：通知包名、规则ID、动作类型、执行结果
- 关键操作使用 `Log.i` 级别，调试信息用 `Log.d`
- 敏感信息（Token、密码）不打日志

### 7.4 异常处理
- 不允许 `catch (e: Exception)` 后空块
- 预期异常（网络超时、解析失败）捕获后记录日志并降级处理
- 非预期异常向上传播，由顶层异常处理器统一处理

### 7.5 协程
- IO 操作用 `withContext(Dispatchers.IO)`
- 不要在主线程做网络/文件操作
- 长生命周期协程用 `applicationScope`，避免泄漏

## 8. 测试要求

- **规则引擎**（`rule/`）必须有单元测试，这是核心纯逻辑
- **通知解析逻辑**必须有单元测试
- **动作执行器**通过接口 mock 依赖后可测
- 测试代码放在 `src/test/` 下，纯 JVM 测试，不依赖 Android 设备
- 修改核心逻辑后，先跑测试再提交

### 构建与测试命令

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 运行单元测试
./gradlew test

# 安装到设备
./gradlew installDebug
# 或 adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 9. 调试技巧

- **通知监听不工作**：检查 `设置 → 通知访问权限` 是否开启，可用 root 自动授权：
  ```bash
  adb shell su -c "cmd notification allow_listener club.saltfish.homeservice/<NotificationListenerService全路径>"
  ```
- **MIUI 杀后台**：需要在 `设置 → 应用 → 自启动` 开启，或 root 自动授权
- **日志查看**：`adb logcat -s <Tag>` 或 Timber 输出
- **服务存活检查**：`adb shell dumpsys activity services club.saltfish.homeservice`
- **bridge 连通性**：`curl http://192.168.5.50:9092/api/health`，不通先查 `API_SERVER_HOST=0.0.0.0`
- **包名复核**：`adb shell pm list packages | grep visualworld`

## 10. 待办 / 未决问题

环境调研项已全部落实（见下）。剩余为**设计决策**，给出推荐默认值，实施时若无异议则直接采用：

1. ~~手机 Android 版本~~ → **Android 13 / API 33，HyperOS V816.0.4.0.TKJCNXM** ✅
2. ~~open-xiaoai-bridge HTTP API 文档~~ → **已确认**（见 §5.4 端点表） ✅
3. ~~360 云智联包名~~ → **`com.visualworld.ecology`** ✅
4. ~~小米音箱局域网 IP~~ → **`192.168.5.6`**；bridge 宿主 **`192.168.5.50`** ✅
5. ~~bridge 能否在电视盒子运行~~ → **能**（RK3568/4G/arm64 镜像可用，AI 走云策略） ✅

**设计决策（均已实施，APK 已搭建并真机验证通过）**：

| # | 决策 | 推荐默认 | 理由 |
|---|------|---------|------|
| A | HTTP 服务器选型 | **NanoHTTPD** | 轻量、单依赖，内嵌够用；Ktor 引入成本高 |
| B | JSON 库 | **Gson** | 生态熟，配置热更新够用 |
| C | Root 执行方式 | **libsu** | 比裸 `Runtime.exec("su")` 稳，能拿 exit code / 输出 |
| D | Web UI 管理规则 | **先不做**，纯 REST API + 配置文件 | 按需后加 |
| E | 通知去重策略 | **默认 60s 内相同(包名+title+text)去重，可配置** | 避免重复触发，阈值外部化 |

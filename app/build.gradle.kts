plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "club.saltfish.homeservice"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "club.saltfish.homeservice"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // 启用 BuildConfig 生成（用于 BuildConfig.DEBUG 控制 Timber 日志树）
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    // 异步
    implementation(libs.kotlinx.coroutines.android)
    // 网络：发指令到 open-xiaoai-bridge
    implementation(libs.okhttp)
    // 日志
    implementation(libs.timber)
    // JSON：规则/配置序列化
    implementation(libs.gson)
    // 内嵌 HTTP 服务器
    implementation(libs.nanohttpd)
    // Root 操作封装（Magisk 作者出品）
    implementation(libs.libsu)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

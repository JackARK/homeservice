<template>
    <n-config-provider :locale="zhCN" :date-locale="dateZhCN" style="height: 100%">
        <n-message-provider>
            <n-dialog-provider>
                <router-view />
            </n-dialog-provider>
        </n-message-provider>
    </n-config-provider>
</template>

<script setup lang="ts">
import { dateZhCN, NConfigProvider, NDialogProvider, NMessageProvider, zhCN } from 'naive-ui'
</script>

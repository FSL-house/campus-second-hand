<template>
  <div class="app-shell">
    <AppHeader v-if="showHeader" />
    <main :class="showHeader ? 'page-main' : 'page-main no-header'">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from './components/AppHeader.vue'

const route = useRoute()

// 登录页和注册页不显示顶部导航。
const showHeader = computed(() => !['/login', '/register'].includes(route.path))
</script>

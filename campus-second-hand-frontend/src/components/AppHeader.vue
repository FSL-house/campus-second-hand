<template>
  <header class="header">
    <div class="page-container header-inner">
      <div class="brand" @click="goHome">校园二手交易平台</div>
      <nav class="nav-list">
        <el-button link @click="router.push('/')">首页</el-button>
        <el-button link @click="router.push('/products')">商品列表</el-button>
        <el-button v-if="token" link @click="router.push('/product/add')">发布商品</el-button>
        <el-button v-if="token" link @click="router.push('/orders')">我的订单</el-button>
        <el-button v-if="token" link @click="router.push('/favorites')">我的收藏</el-button>
        <el-button v-if="userInfo?.role === 'ADMIN'" link @click="router.push('/admin')">后台管理</el-button>
      </nav>
      <div class="user-box">
        <template v-if="token">
          <span class="nickname">{{ userInfo?.nickname || userInfo?.username }}</span>
          <el-button size="small" @click="logout">退出登录</el-button>
        </template>
        <template v-else>
          <el-button size="small" @click="router.push('/login')">登录</el-button>
          <el-button size="small" type="primary" @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { clearLoginInfo, getToken, getUserInfo } from '../utils/auth'

const router = useRouter()
const token = computed(() => getToken())
const userInfo = computed(() => getUserInfo())

function goHome() {
  router.push('/')
}

// 退出登录时清空本地登录信息，并跳回登录页。
function logout() {
  clearLoginInfo()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.header-inner {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.brand {
  font-size: 20px;
  font-weight: 700;
  cursor: pointer;
  white-space: nowrap;
}

.nav-list {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}

.user-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nickname {
  color: #606266;
  font-size: 14px;
}

@media (max-width: 900px) {
  .header {
    height: auto;
    padding: 12px 0;
  }

  .header-inner {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>

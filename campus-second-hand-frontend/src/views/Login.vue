<template>
  <div class="auth-wrapper">
    <el-card class="auth-card">
      <h2 class="page-title">用户登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button link style="margin: 0 auto" @click="router.push('/register')">没有账号？去注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginUser } from '../api/user'
import { setToken, setUserInfo } from '../utils/auth'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

// 登录成功后，把 token 和用户信息保存到本地。
async function handleLogin() {
  const res = await loginUser(form)
  setToken(res.data.token)
  setUserInfo(res.data.userInfo)
  ElMessage.success('登录成功')

  if (res.data.userInfo.role === 'ADMIN') {
    router.push('/admin')
    return
  }

  router.push('/')
}
</script>

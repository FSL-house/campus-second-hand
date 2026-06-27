<template>
  <div class="auth-wrapper">
    <el-card class="auth-card">
      <h2 class="page-title">用户注册</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleRegister">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button link style="margin: 0 auto" @click="router.push('/login')">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { registerUser } from '../api/user'

const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  nickname: '',
  phone: ''
})

async function handleRegister() {
  await registerUser(form)
  ElMessage.success('注册成功，请登录')
  router.push('/login')
}
</script>

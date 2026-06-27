<template>
  <div class="page-container" v-if="product">
    <div class="two-column">
      <el-card shadow="never">
        <img :src="getImageUrl(product.image)" alt="商品图片" style="width: 100%; border-radius: 8px;" @error="handleImageError" />
      </el-card>

      <el-card shadow="never">
        <h1 class="page-title">{{ product.title }}</h1>
        <div style="margin-bottom: 12px; color: #909399">分类：{{ product.categoryName }}</div>
        <div style="margin-bottom: 12px; color: #909399">卖家：{{ product.sellerNickname }}</div>
        <div style="margin-bottom: 18px; color: #f56c6c; font-size: 32px; font-weight: 700;">
          ￥{{ Number(product.price || 0).toFixed(2) }}
        </div>
        <div style="line-height: 1.8; margin-bottom: 20px; white-space: pre-wrap;">{{ product.description || '暂无商品描述' }}</div>
        <div style="display: flex; gap: 12px; flex-wrap: wrap;">
          <el-button type="primary" @click="handleBuy">立即购买</el-button>
          <el-button @click="handleFavorite">收藏商品</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { addFavorite } from '../api/favorite'
import { createOrder } from '../api/order'
import { getProductDetail } from '../api/product'
import { getToken } from '../utils/auth'
import { getFallbackImage, resolveProductImage } from '../utils/image'

const route = useRoute()
const router = useRouter()
const product = ref(null)

function getImageUrl(image) {
  return resolveProductImage(image, product.value?.title || '商品图片', 'detail')
}

function handleImageError(event) {
  event.target.src = getFallbackImage(product.value?.title || '商品图片', 'detail')
}

async function loadDetail() {
  const res = await getProductDetail(route.params.id)
  product.value = res.data
}

function checkLogin() {
  if (!getToken()) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return false
  }
  return true
}

async function handleBuy() {
  if (!checkLogin()) return
  await createOrder(product.value.id)
  ElMessage.success('下单成功')
  router.push('/orders')
}

async function handleFavorite() {
  if (!checkLogin()) return
  await addFavorite(product.value.id)
  ElMessage.success('收藏成功')
}

onMounted(loadDetail)
</script>

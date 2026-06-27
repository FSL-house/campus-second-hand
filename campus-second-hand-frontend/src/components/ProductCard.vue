<template>
  <el-card shadow="hover" class="product-card" @click="goDetail">
    <img :src="getImageUrl(product.image)" class="cover" alt="商品图片" @error="handleImageError" />
    <div class="content">
      <div class="title">{{ product.title }}</div>
      <div class="meta">{{ product.categoryName || '未分类' }} | {{ product.sellerNickname || '未知用户' }}</div>
      <div class="price-row">
        <span class="price">￥{{ Number(product.price || 0).toFixed(2) }}</span>
        <el-tag v-if="showStatus" size="small">{{ product.status }}</el-tag>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { getFallbackImage, resolveProductImage } from '../utils/image'

const props = defineProps({
  product: {
    type: Object,
    required: true
  },
  showStatus: {
    type: Boolean,
    default: false
  }
})

const router = useRouter()

// 兼容本地静态图片路径和网络图片地址。
function getImageUrl(image) {
  return resolveProductImage(image, props.product.title, 'card')
}

// 图片加载失败时，自动回退到默认占位图。
function handleImageError(event) {
  event.target.src = getFallbackImage(props.product.title, 'card')
}

function goDetail() {
  router.push(`/product/${props.product.id}`)
}
</script>

<style scoped>
.product-card {
  cursor: pointer;
  border-radius: 8px;
}

.cover {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 6px;
}

.content {
  padding-top: 12px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  line-height: 22px;
  min-height: 44px;
}

.meta {
  color: #909399;
  font-size: 13px;
  margin: 10px 0;
}

.price-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: 700;
}
</style>

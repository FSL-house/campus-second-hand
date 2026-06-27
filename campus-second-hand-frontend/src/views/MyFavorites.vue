<template>
  <div class="page-container">
    <h1 class="page-title">我的收藏</h1>
    <div v-if="favoriteList.length" class="grid-list">
      <div v-for="item in favoriteList" :key="item.id" class="favorite-item">
        <ProductCard :product="item" />
        <div class="favorite-actions">
          <el-button type="danger" plain size="small" @click="handleCancel(item.id)">取消收藏</el-button>
        </div>
      </div>
    </div>
    <el-empty v-else description="你还没有收藏商品" class="empty-box" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { cancelFavorite, getMyFavorites } from '../api/favorite'
import ProductCard from '../components/ProductCard.vue'

const favoriteList = ref([])

async function loadFavorites() {
  const res = await getMyFavorites()
  favoriteList.value = res.data
}

async function handleCancel(productId) {
  await cancelFavorite(productId)
  ElMessage.success('取消收藏成功')
  loadFavorites()
}

onMounted(loadFavorites)
</script>

<style scoped>
.favorite-item {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.favorite-actions {
  display: flex;
  justify-content: flex-end;
}
</style>

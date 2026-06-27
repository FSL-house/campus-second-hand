<template>
  <div class="page-container">
    <el-card shadow="never" style="margin-bottom: 20px">
      <div style="display: flex; justify-content: space-between; gap: 16px; flex-wrap: wrap; align-items: center;">
        <div>
          <h1 class="page-title" style="margin-bottom: 10px">校园二手交易平台</h1>
          <div style="color: #606266">适合校园场景的简洁二手交易小项目，支持商品发布、购买、收藏和后台审核。</div>
        </div>
        <div style="display: flex; gap: 12px; flex-wrap: wrap;">
          <el-button type="primary" @click="router.push('/products')">去逛商品</el-button>
          <el-button @click="router.push('/product/add')">发布商品</el-button>
        </div>
      </div>
    </el-card>

    <div class="toolbar">
      <el-select v-model="categoryId" placeholder="按分类筛选" clearable style="width: 220px" @change="loadProducts">
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-input v-model="keyword" placeholder="请输入商品关键词" style="width: 260px" clearable @keyup.enter="loadProducts" />
      <el-button type="primary" @click="loadProducts">搜索</el-button>
    </div>

    <div v-loading="loading" class="product-panel">
      <div v-if="productList.length" class="grid-list">
        <ProductCard v-for="item in productList" :key="item.id" :product="item" />
      </div>
      <el-empty v-else-if="!loading" description="暂无商品数据" class="empty-box" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getCategoryList, getProductList } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const router = useRouter()
const categoryList = ref([])
const productList = ref([])
const categoryId = ref('')
const keyword = ref('')
const loading = ref(false)

async function loadCategories() {
  const res = await getCategoryList()
  categoryList.value = res.data
}

// 首页直接展示公开商品，方便用户一进来就能看到内容。
async function loadProducts() {
  loading.value = true
  try {
    const res = await getProductList({
      categoryId: categoryId.value || undefined,
      keyword: keyword.value || undefined
    })
    productList.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.product-panel {
  min-height: 420px;
}
</style>

<template>
  <div class="page-container">
    <h1 class="page-title">商品列表</h1>
    <div class="toolbar">
      <el-select v-model="categoryId" placeholder="选择分类" clearable style="width: 220px">
        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-input v-model="keyword" placeholder="输入关键词搜索" clearable style="width: 280px" @keyup.enter="loadProducts" />
      <el-button type="primary" @click="loadProducts">查询</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <div v-loading="loading" class="product-panel">
      <div v-if="productList.length" class="grid-list">
        <ProductCard v-for="item in productList" :key="item.id" :product="item" />
      </div>
      <el-empty v-else-if="!loading" description="没有找到商品" class="empty-box" />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getCategoryList, getProductList } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const categoryList = ref([])
const productList = ref([])
const categoryId = ref('')
const keyword = ref('')
const loading = ref(false)

async function loadCategories() {
  const res = await getCategoryList()
  categoryList.value = res.data
}

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

function resetSearch() {
  categoryId.value = ''
  keyword.value = ''
  loadProducts()
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

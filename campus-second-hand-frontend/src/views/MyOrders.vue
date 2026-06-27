<template>
  <div class="page-container">
    <h1 class="page-title">我的订单</h1>
    <el-tabs v-model="activeName">
      <el-tab-pane label="我买到的" name="buy">
        <el-table :data="buyOrders" border style="width: 100%">
          <el-table-column prop="orderNo" label="订单编号" min-width="220" />
          <el-table-column prop="productTitle" label="商品标题" min-width="160" />
          <el-table-column prop="sellerNickname" label="卖家" width="120" />
          <el-table-column prop="price" label="价格" width="100" />
          <el-table-column prop="status" label="状态" width="130" />
          <el-table-column label="操作" width="220">
            <template #default="{ row }">
              <el-button size="small" type="success" :disabled="row.status !== 'PENDING_DEAL'" @click="changeStatus(row.id, 'FINISHED')">
                完成订单
              </el-button>
              <el-button size="small" :disabled="row.status !== 'PENDING_DEAL'" @click="changeStatus(row.id, 'CANCELLED')">
                取消订单
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="我卖出的" name="sell">
        <el-table :data="sellOrders" border style="width: 100%">
          <el-table-column prop="orderNo" label="订单编号" min-width="220" />
          <el-table-column prop="productTitle" label="商品标题" min-width="160" />
          <el-table-column prop="buyerNickname" label="买家" width="120" />
          <el-table-column prop="price" label="价格" width="100" />
          <el-table-column prop="status" label="状态" width="130" />
          <el-table-column label="操作" width="220">
            <template #default="{ row }">
              <el-button size="small" type="success" :disabled="row.status !== 'PENDING_DEAL'" @click="changeStatus(row.id, 'FINISHED')">
                完成订单
              </el-button>
              <el-button size="small" :disabled="row.status !== 'PENDING_DEAL'" @click="changeStatus(row.id, 'CANCELLED')">
                取消订单
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyBuyOrders, getMySellOrders, updateOrderStatus } from '../api/order'

const activeName = ref('buy')
const buyOrders = ref([])
const sellOrders = ref([])

async function loadBuyOrders() {
  const res = await getMyBuyOrders()
  buyOrders.value = res.data
}

async function loadSellOrders() {
  const res = await getMySellOrders()
  sellOrders.value = res.data
}

async function changeStatus(orderId, status) {
  await updateOrderStatus({ orderId, status })
  ElMessage.success('订单状态修改成功')
  loadBuyOrders()
  loadSellOrders()
}

onMounted(() => {
  loadBuyOrders()
  loadSellOrders()
})
</script>

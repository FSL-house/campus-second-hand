import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ProductList from '../views/ProductList.vue'
import ProductDetail from '../views/ProductDetail.vue'
import ProductAdd from '../views/ProductAdd.vue'
import MyOrders from '../views/MyOrders.vue'
import MyFavorites from '../views/MyFavorites.vue'
import Admin from '../views/Admin.vue'
import { getToken, getUserInfo } from '../utils/auth'

const routes = [
  { path: '/', name: 'home', component: Home },
  { path: '/login', name: 'login', component: Login },
  { path: '/register', name: 'register', component: Register },
  { path: '/products', name: 'products', component: ProductList },
  { path: '/product/:id', name: 'product-detail', component: ProductDetail },
  { path: '/product/add', name: 'product-add', component: ProductAdd, meta: { requiresAuth: true } },
  { path: '/orders', name: 'orders', component: MyOrders, meta: { requiresAuth: true } },
  { path: '/favorites', name: 'favorites', component: MyFavorites, meta: { requiresAuth: true } },
  { path: '/admin', name: 'admin', component: Admin, meta: { requiresAuth: true, requiresAdmin: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由前置守卫。
// 用于判断页面是否需要登录，以及是否需要管理员权限。
router.beforeEach((to, from, next) => {
  const token = getToken()
  const userInfo = getUserInfo()

  if (to.meta.requiresAuth && !token) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }

  if (to.meta.requiresAdmin && userInfo?.role !== 'ADMIN') {
    ElMessage.error('只有管理员可以访问该页面')
    next('/')
    return
  }

  next()
})

export default router

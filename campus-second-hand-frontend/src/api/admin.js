import request from '../utils/request'

export function getAdminUsers() {
  return request.get('/admin/users')
}

export function getAdminProducts() {
  return request.get('/admin/products')
}

export function updateAdminProductStatus(data) {
  return request.post('/admin/product/status', data)
}

export function getAdminOrders() {
  return request.get('/admin/orders')
}

export function getAdminStats() {
  return request.get('/admin/stats')
}

import request from '../utils/request'

export function createOrder(productId) {
  return request.post(`/order/create/${productId}`)
}

export function getMyBuyOrders() {
  return request.get('/order/my-buy')
}

export function getMySellOrders() {
  return request.get('/order/my-sell')
}

export function updateOrderStatus(data) {
  return request.post('/order/update-status', data)
}

import request from '../utils/request'

export function addFavorite(productId) {
  return request.post(`/favorite/add/${productId}`)
}

export function cancelFavorite(productId) {
  return request.delete(`/favorite/cancel/${productId}`)
}

export function getMyFavorites() {
  return request.get('/favorite/my')
}

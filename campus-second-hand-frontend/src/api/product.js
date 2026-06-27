import request from '../utils/request'

export function getCategoryList() {
  return request.get('/category/list')
}

export function addProduct(data) {
  return request.post('/product/add', data)
}

export function getProductList(params) {
  return request.get('/product/list', { params })
}

export function getProductDetail(id) {
  return request.get(`/product/detail/${id}`)
}

export function getMyProducts() {
  return request.get('/product/my')
}

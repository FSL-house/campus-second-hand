import request from '../utils/request'

export function registerUser(data) {
  return request.post('/user/register', data)
}

export function loginUser(data) {
  return request.post('/user/login', data)
}

export function getCurrentUser() {
  return request.get('/user/current')
}

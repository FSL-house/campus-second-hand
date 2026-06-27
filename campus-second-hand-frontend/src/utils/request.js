import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { clearLoginInfo, getToken } from './auth'

// Axios 实例。
// 统一设置后端地址、请求头和错误处理逻辑。
const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 请求拦截器：自动带上 token。
request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.token = token
  }
  return config
})

// 响应拦截器：统一处理后端返回结构。
request.interceptors.response.use(
  (response) => {
    const result = response.data
    if (result.code === 200) {
      return result
    }

    ElMessage.error(result.message || '请求失败')

    if (result.code === 401) {
      clearLoginInfo()
      router.push('/login')
    }

    return Promise.reject(new Error(result.message || '请求失败'))
  },
  (error) => {
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request

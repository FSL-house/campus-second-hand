const TOKEN_KEY = 'campus_second_hand_token'
const USER_KEY = 'campus_second_hand_user'

// 保存 token。
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token || '')
}

// 获取 token。
export function getToken() {
  return localStorage.getItem(TOKEN_KEY) || ''
}

// 保存当前登录用户信息。
export function setUserInfo(userInfo) {
  localStorage.setItem(USER_KEY, JSON.stringify(userInfo || null))
}

// 获取当前登录用户信息。
export function getUserInfo() {
  const value = localStorage.getItem(USER_KEY)
  return value ? JSON.parse(value) : null
}

// 清除登录信息。
export function clearLoginInfo() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

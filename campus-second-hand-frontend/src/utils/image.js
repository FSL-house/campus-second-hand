// 生成商品占位图。
// 使用本地 data URL，避免依赖外网占位图服务导致加载慢或加载失败。
function buildPlaceholderSvg(title = '商品图片') {
  const safeTitle = String(title).slice(0, 18)
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="600" height="400" viewBox="0 0 600 400">
      <rect width="600" height="400" fill="#eef2f7"/>
      <rect x="170" y="100" width="260" height="170" rx="18" fill="#d9e2ef"/>
      <circle cx="255" cy="170" r="26" fill="#b8c6db"/>
      <path d="M210 255l66-60 44 38 50-54 56 76H210z" fill="#97abc7"/>
      <text x="300" y="325" text-anchor="middle" font-size="28" fill="#667885" font-family="Arial,Microsoft YaHei,sans-serif">${safeTitle}</text>
    </svg>
  `
  return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`
}

// 生成详情页用的大图占位图。
function buildDetailPlaceholderSvg(title = '商品图片') {
  const safeTitle = String(title).slice(0, 18)
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="900" height="600" viewBox="0 0 900 600">
      <rect width="900" height="600" fill="#eef2f7"/>
      <rect x="255" y="145" width="390" height="240" rx="22" fill="#d9e2ef"/>
      <circle cx="360" cy="235" r="36" fill="#b8c6db"/>
      <path d="M305 365l94-86 64 54 72-78 70 110H305z" fill="#97abc7"/>
      <text x="450" y="470" text-anchor="middle" font-size="42" fill="#667885" font-family="Arial,Microsoft YaHei,sans-serif">${safeTitle}</text>
    </svg>
  `
  return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`
}

// 处理商品图片地址：
// 1. 兼容本地图片路径
// 2. 修正常见手误，如 .jps -> .jpg
// 3. 遇到外网占位图时直接换成本地 data 占位图，避免加载慢
const placeholderImageMap = {
  JavaBook: '/images/java-book.svg',
  PowerBank: '/images/power-bank.svg',
  Lamp: '/images/lamp.svg',
  Demo: '/images/demo.svg'
}

function getMappedPlaceholderImage(image) {
  const match = image.match(/[?&]text=([^&]+)/i)

  if (!match) {
    return ''
  }

  const text = decodeURIComponent(match[1])
  return placeholderImageMap[text] || ''
}

export function resolveProductImage(image, title, mode = 'card') {
  const fallback = mode === 'detail' ? buildDetailPlaceholderSvg(title) : buildPlaceholderSvg(title)

  if (!image) {
    return fallback
  }

  let finalImage = image.trim()

  if (finalImage.endsWith('.jps')) {
    finalImage = `${finalImage.slice(0, -4)}.jpg`
  }

  if (finalImage.includes('via.placeholder.com')) {
    return getMappedPlaceholderImage(finalImage) || fallback
  }

  if (finalImage.startsWith('data:') || finalImage.startsWith('http://') || finalImage.startsWith('https://') || finalImage.startsWith('/')) {
    return finalImage
  }

  return `/${finalImage}`
}

export function getFallbackImage(title, mode = 'card') {
  return mode === 'detail' ? buildDetailPlaceholderSvg(title) : buildPlaceholderSvg(title)
}

<template>
  <div class="page-container">
    <el-card shadow="never" style="max-width: 800px; margin: 0 auto;">
      <h1 class="page-title">发布商品</h1>

      <el-alert
        title="支持直接上传商品图片，上传成功后会自动回填图片地址"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      >
        <template #default>
          <div>如果你已经把图片放到 <b>public/images</b> 目录，也可以继续手动填写，例如 <b>/images/keyboard.jpg</b>。</div>
          <div style="margin-top: 6px">单张图片建议不超过 5MB，常见的 jpg、jpeg、png、gif、webp 都可以上传。</div>
        </template>
      </el-alert>

      <el-form :model="form" label-width="100px">
        <el-form-item label="商品标题">
          <el-input v-model="form.title" placeholder="请输入商品标题" />
        </el-form-item>

        <el-form-item label="商品价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="1" style="width: 220px" />
        </el-form-item>

        <el-form-item label="商品分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 220px">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="图片地址">
          <el-input v-model="form.image" placeholder="请输入图片地址，例如 /images/keyboard.jpg" />
        </el-form-item>

        <el-form-item label="上传图片">
          <div class="upload-wrapper">
            <el-upload
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              accept="image/*"
              @change="handleSelectImage"
            >
              <el-button>选择图片</el-button>
            </el-upload>
            <el-button type="primary" :loading="uploading" @click="handleUploadImage">上传到项目</el-button>
            <span class="upload-tip">{{ selectedFileName || '暂未选择图片' }}</span>
          </div>

          <div v-if="form.image" class="preview-box">
            <img :src="previewImage" alt="商品预览图" class="preview-image" />
            <div class="preview-path">{{ form.image }}</div>
          </div>
        </el-form-item>

        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="5" placeholder="请输入商品描述" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交发布</el-button>
          <el-button @click="router.push('/products')">返回商品列表</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { uploadProductImage } from '../api/file'
import { addProduct, getCategoryList } from '../api/product'
import { resolveProductImage } from '../utils/image'

const router = useRouter()
const categoryList = ref([])
const selectedFile = ref(null)
const selectedFileName = ref('')
const uploading = ref(false)

const form = reactive({
  title: '',
  description: '',
  price: 0,
  image: '',
  categoryId: ''
})

async function loadCategories() {
  const res = await getCategoryList()
  categoryList.value = res.data
}

// 选择图片后，先把文件保存在页面状态中，等用户点击上传。
function handleSelectImage(uploadFile) {
  const file = uploadFile.raw

  if (!file) {
    return
  }

  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过 5MB')
    selectedFile.value = null
    selectedFileName.value = ''
    return
  }

  selectedFile.value = file
  selectedFileName.value = uploadFile.name
}

// 调用后端上传接口，把图片保存到前端 public/images 目录。
async function handleUploadImage() {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择图片')
    return
  }

  uploading.value = true
  try {
    const res = await uploadProductImage(selectedFile.value)
    form.image = res.data
    selectedFileName.value = `已上传：${selectedFileName.value}`
    ElMessage.success('图片上传成功')
  } finally {
    uploading.value = false
  }
}

// 统一处理图片预览地址，兼容本地路径和默认占位图。
const previewImage = computed(() => resolveProductImage(form.image, form.title || '商品图片', 'card'))

async function handleSubmit() {
  if (selectedFile.value && !form.image) {
    await handleUploadImage()
  }

  if (!form.image) {
    ElMessage.warning('请先上传图片，或者手动填写图片地址')
    return
  }

  await addProduct(form)
  ElMessage.success('发布成功，等待管理员审核')
  router.push('/products')
}

onMounted(loadCategories)
</script>

<style scoped>
.upload-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.upload-tip {
  color: #909399;
  font-size: 14px;
}

.preview-box {
  margin-top: 16px;
}

.preview-image {
  width: 220px;
  height: 160px;
  object-fit: cover;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  display: block;
}

.preview-path {
  margin-top: 8px;
  color: #606266;
  word-break: break-all;
}
</style>

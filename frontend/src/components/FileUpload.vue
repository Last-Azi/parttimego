<template>
  <div>
    <el-upload
      :action="uploadUrl"
      :headers="uploadHeaders"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :accept="acceptTypes"
    >
      <slot>
        <el-button type="primary" :loading="loading">
          <el-icon><Upload /></el-icon>
          {{ loading ? '上传中...' : buttonText }}
        </el-button>
      </slot>
    </el-upload>
    <div v-if="fileUrl" style="margin-top: 10px;">
      <el-tag v-if="type === 'avatar'" closable @close="handleRemove">
        <img :src="fileUrl" style="width: 60px; height: 60px; object-fit: cover; border-radius: 4px;" />
      </el-tag>
      <el-tag v-else closable @close="handleRemove">
        <el-icon><Document /></el-icon>
        {{ fileName || '已上传文件' }}
      </el-tag>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { deleteFile } from '../api/file'

const props = defineProps({
  modelValue: { type: String, default: '' },
  type: { type: String, default: 'avatar' },
  buttonText: { type: String, default: '上传文件' }
})

const emit = defineEmits(['update:modelValue'])

const loading = ref(false)
const fileName = ref('')

const uploadUrl = computed(() => {
  return props.type === 'avatar' ? '/api/file/upload/avatar' : '/api/file/upload/resume'
})

const uploadHeaders = computed(() => {
  return { Authorization: `Bearer ${localStorage.getItem('token')}` }
})

const acceptTypes = computed(() => {
  return props.type === 'avatar' ? '.jpg,.jpeg,.png,.gif' : '.pdf,.doc,.docx'
})

const fileUrl = computed(() => props.modelValue)

const beforeUpload = (file) => {
  const maxSize = props.type === 'avatar' ? 2 * 1024 * 1024 : 10 * 1024 * 1024
  if (file.size > maxSize) {
    ElMessage.error('文件大小超出限制')
    return false
  }
  fileName.value = file.name
  loading.value = true
  return true
}

const handleSuccess = (response) => {
  loading.value = false
  if (response.code === 200) {
    emit('update:modelValue', response.data)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const handleError = () => {
  loading.value = false
  ElMessage.error('上传失败')
}

const handleRemove = () => {
  emit('update:modelValue', '')
  fileName.value = ''
}
</script>

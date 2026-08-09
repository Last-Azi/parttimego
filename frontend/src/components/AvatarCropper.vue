<template>
  <div v-if="visible" class="cropper-overlay" @click.self="$emit('close')">
    <div class="cropper-dialog">
      <div class="cropper-header">
        <h3>裁切头像</h3>
        <button class="cropper-close" @click="$emit('close')">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="6" x2="6" y2="18"/>
            <line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
      </div>
      <div class="cropper-body">
        <Cropper
          ref="cropperRef"
          class="cropper"
          :src="imageSrc"
          :stencil-props="{ aspectRatio: 1 }"
          :auto-zoom="true"
          image-restriction="stencil"
        />
      </div>
      <div class="cropper-footer">
        <button class="btn-cancel" @click="$emit('close')">取消</button>
        <button class="btn-confirm" @click="handleConfirm">确认上传</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Cropper } from 'vue-advanced-cropper'
import 'vue-advanced-cropper/dist/style.css'

const props = defineProps({
  visible: Boolean,
  imageSrc: String
})

const emit = defineEmits(['close', 'crop'])

const cropperRef = ref(null)

async function handleConfirm() {
  const { canvas } = cropperRef.value.getResult()
  if (!canvas) return
  canvas.toBlob((blob) => {
    emit('crop', blob)
  }, 'image/jpeg', 0.9)
}
</script>

<style scoped>
.cropper-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.cropper-dialog {
  background: white;
  border-radius: 20px;
  width: 480px;
  max-width: 90%;
  overflow: hidden;
}

.cropper-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #F1F5F9;
}

.cropper-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin: 0;
}

.cropper-close {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #94A3B8;
  transition: color 0.2s;
}

.cropper-close:hover {
  color: #1E293B;
}

.cropper-body {
  padding: 20px 24px;
}

.cropper {
  height: 320px;
  background: #F8FAFC;
  border-radius: 12px;
}

.cropper-footer {
  display: flex;
  gap: 12px;
  padding: 0 24px 24px;
  justify-content: flex-end;
}

.btn-cancel {
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-confirm {
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-confirm:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}
</style>

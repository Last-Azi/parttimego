<template>
  <div class="publish-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="16"/>
            <line x1="8" y1="12" x2="16" y2="12"/>
          </svg>
          {{ isEdit ? '修改岗位' : '发布岗位' }}
        </h1>
        <p class="page-subtitle">{{ isEdit ? '修改岗位信息' : '发布新的兼职岗位' }}</p>
      </div>
    </div>

    <div class="container">
      <div class="form-layout">
        <!-- 左侧表单 -->
        <div class="form-section">
          <!-- 基本信息 -->
          <div class="form-card">
            <h2 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
              基本信息
            </h2>
            <div class="form-group">
              <label>岗位标题 <span class="required">*</span></label>
              <input v-model="form.title" type="text" placeholder="如：周末兼职促销员" :class="{ error: errors.title }">
              <span v-if="errors.title" class="error-msg">{{ errors.title }}</span>
            </div>
            <div class="form-group">
              <label>岗位描述</label>
              <textarea v-model="form.description" rows="6" placeholder="详细描述工作内容、要求等"></textarea>
            </div>
          </div>

          <!-- 薪资信息 -->
          <div class="form-card">
            <h2 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <line x1="12" y1="1" x2="12" y2="23"/>
                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
              </svg>
              薪资信息
            </h2>
            <div class="form-row">
              <div class="form-group">
                <label>最低薪资 <span class="required">*</span></label>
                <div class="input-with-unit">
                  <input v-model.number="form.salaryMin" type="number" placeholder="0" :class="{ error: errors.salaryMin }">
                  <span class="unit">元</span>
                </div>
                <span v-if="errors.salaryMin" class="error-msg">{{ errors.salaryMin }}</span>
              </div>
              <div class="form-group">
                <label>最高薪资 <span class="required">*</span></label>
                <div class="input-with-unit">
                  <input v-model.number="form.salaryMax" type="number" placeholder="0" :class="{ error: errors.salaryMax }">
                  <span class="unit">元</span>
                </div>
                <span v-if="errors.salaryMax" class="error-msg">{{ errors.salaryMax }}</span>
              </div>
            </div>
            <div class="form-group">
              <label>结算方式</label>
              <div class="radio-group">
                <label
                  v-for="type in salaryTypes"
                  :key="type"
                  class="radio-option"
                  :class="{ active: form.salaryType === type }"
                >
                  <input type="radio" v-model="form.salaryType" :value="type">
                  <span>{{ type }}</span>
                </label>
              </div>
            </div>
          </div>

          <!-- 工作信息 -->
          <div class="form-card">
            <h2 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              工作信息
            </h2>
            <div class="form-row">
              <div class="form-group">
                <label>工作城市 <span class="required">*</span></label>
                <select v-model="form.city" :class="{ error: errors.city }">
                  <option value="">请选择城市</option>
                  <option v-for="city in cities" :key="city" :value="city">{{ city }}</option>
                </select>
                <span v-if="errors.city" class="error-msg">{{ errors.city }}</span>
              </div>
              <div class="form-group">
                <label>岗位分类</label>
                <select v-model="form.category">
                  <option value="">请选择分类</option>
                  <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label>工作地址</label>
              <input v-model="form.address" type="text" placeholder="详细工作地址">
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>工作时间</label>
                <input v-model="form.workTime" type="text" placeholder="如：周六日 9:00-18:00">
              </div>
              <div class="form-group">
                <label>招聘人数</label>
                <div class="input-with-unit">
                  <input v-model.number="form.headcount" type="number" min="1" placeholder="1">
                  <span class="unit">人</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 提交按钮 -->
          <div class="form-actions">
            <button class="btn-cancel" @click="$router.back()">取消</button>
            <button class="btn-submit" @click="handleSubmit" :disabled="loading">
              <span v-if="loading" class="loading-spinner"></span>
              <span v-else>{{ isEdit ? '修改岗位' : '发布岗位' }}</span>
            </button>
          </div>
        </div>

        <!-- 右侧预览 -->
        <div class="preview-section">
          <div class="preview-card">
            <h3 class="preview-title">岗位预览</h3>
            <div class="preview-content">
              <div class="preview-header">
                <h2>{{ form.title || '岗位标题' }}</h2>
                <div class="preview-salary">
                  <span class="amount">{{ form.salaryMin || 0 }}-{{ form.salaryMax || 0 }}</span>
                  <span class="unit">元/{{ form.salaryType }}</span>
                </div>
              </div>

              <div class="preview-tags">
                <span v-if="form.city">{{ form.city }}</span>
                <span v-if="form.category">{{ form.category }}</span>
                <span v-if="form.workTime">{{ form.workTime }}</span>
                <span v-if="form.headcount">招{{ form.headcount }}人</span>
              </div>

              <div v-if="form.description" class="preview-desc">
                <h4>岗位描述</h4>
                <p>{{ form.description }}</p>
              </div>

              <div v-if="form.address" class="preview-address">
                <h4>工作地址</h4>
                <p>{{ form.address }}</p>
              </div>

              <div v-if="!form.title && !form.description" class="preview-empty">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="2">
                  <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                  <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg>
                <p>填写左侧信息后</p>
                <p>这里会实时预览岗位效果</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { publishJob, updateJob, getJobDetail } from '../../api/job'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const isEdit = computed(() => !!route.query.editId)

const form = ref({
  title: '', description: '', salaryMin: 0, salaryMax: 0, salaryType: '日结',
  city: '', address: '', category: '', workTime: '', headcount: 1
})

const errors = ref({
  title: '', salaryMin: '', salaryMax: '', city: ''
})

const salaryTypes = ['日结', '周结', '月结', '完工结']
const cities = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '重庆', '西安', '天津', '苏州', '长沙', '郑州']
const categories = ['餐饮', '家教', '促销', '服务员', '传单派发', '物流配送', '超市零售', '活动执行', '文案编辑', '设计美工', 'IT技术', '客服', '行政文员', '翻译', '摄影摄像', '其他']

function validate() {
  errors.value = { title: '', salaryMin: '', salaryMax: '', city: '' }
  let valid = true

  if (!form.value.title.trim()) {
    errors.value.title = '请输入岗位标题'
    valid = false
  }
  if (!form.value.salaryMin && form.value.salaryMin !== 0) {
    errors.value.salaryMin = '请输入最低薪资'
    valid = false
  }
  if (!form.value.salaryMax && form.value.salaryMax !== 0) {
    errors.value.salaryMax = '请输入最高薪资'
    valid = false
  }
  if (!form.value.city) {
    errors.value.city = '请选择城市'
    valid = false
  }

  return valid
}

onMounted(async () => {
  if (isEdit.value) {
    const res = await getJobDetail(route.query.editId)
    Object.assign(form.value, res.data)
  }
})

async function handleSubmit() {
  if (!validate()) return

  loading.value = true
  try {
    if (isEdit.value) {
      await updateJob(route.query.editId, form.value)
      ElMessage.success('修改成功')
    } else {
      await publishJob(form.value)
      ElMessage.success('发布成功')
    }
    router.push('/employer/jobs')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.publish-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  background: white;
  padding: 32px 0;
  border-bottom: 1px solid #E2E8F0;
  margin-bottom: 32px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 15px;
  color: #64748B;
}

.form-layout {
  display: flex;
  gap: 32px;
  padding-bottom: 40px;
}

.form-section {
  flex: 1;
}

.preview-section {
  width: 360px;
  flex-shrink: 0;
}

/* 表单卡片 */
.form-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  margin-bottom: 24px;
  border: 1px solid #E2E8F0;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 8px;
}

.required {
  color: #EF4444;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #E2E8F0;
  border-radius: 10px;
  font-size: 15px;
  outline: none;
  transition: all 0.2s;
  background: #F8FAFC;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  border-color: #FF6B35;
  background: white;
  box-shadow: 0 0 0 4px rgba(255, 107, 53, 0.1);
}

.form-group input.error,
.form-group select.error {
  border-color: #EF4444;
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.error-msg {
  font-size: 13px;
  color: #EF4444;
  margin-top: 4px;
  display: block;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* 输入框带单位 */
.input-with-unit {
  display: flex;
  align-items: center;
}

.input-with-unit input {
  flex: 1;
  border-right: none;
  border-radius: 10px 0 0 10px;
}

.input-with-unit .unit {
  padding: 12px 16px;
  background: #F1F5F9;
  border: 2px solid #E2E8F0;
  border-left: none;
  border-radius: 0 10px 10px 0;
  font-size: 14px;
  color: #64748B;
}

/* 单选按钮组 */
.radio-group {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.radio-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 2px solid #E2E8F0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.radio-option input {
  display: none;
}

.radio-option:hover {
  border-color: #FF6B35;
}

.radio-option.active {
  border-color: #FF6B35;
  background: #FFF5F0;
  color: #FF6B35;
}

/* 操作按钮 */
.form-actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
}

.btn-cancel {
  padding: 14px 28px;
  border: 1px solid #E2E8F0;
  border-radius: 12px;
  background: white;
  color: #64748B;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-submit {
  padding: 14px 32px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 120px;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 预览卡片 */
.preview-card {
  position: sticky;
  top: 80px;
  background: white;
  border-radius: 16px;
  border: 1px solid #E2E8F0;
  overflow: hidden;
}

.preview-title {
  padding: 16px 20px;
  background: #F8FAFC;
  border-bottom: 1px solid #E2E8F0;
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
}

.preview-content {
  padding: 24px;
}

.preview-header {
  margin-bottom: 20px;
}

.preview-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
}

.preview-salary {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.preview-salary .amount {
  font-size: 24px;
  font-weight: 700;
  color: #FF6B35;
}

.preview-salary .unit {
  font-size: 14px;
  color: #64748B;
}

.preview-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.preview-tags span {
  padding: 6px 12px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 13px;
}

.preview-desc,
.preview-address {
  margin-bottom: 20px;
}

.preview-desc h4,
.preview-address h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 8px;
}

.preview-desc p,
.preview-address p {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
  white-space: pre-wrap;
}

.preview-empty {
  text-align: center;
  padding: 40px 0;
  color: #CBD5E1;
}

.preview-empty svg {
  margin-bottom: 16px;
}

.preview-empty p {
  font-size: 14px;
  color: #94A3B8;
  margin-bottom: 4px;
}

@media (max-width: 1024px) {
  .form-layout {
    flex-direction: column;
  }

  .preview-section {
    width: 100%;
  }

  .preview-card {
    position: static;
  }
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>

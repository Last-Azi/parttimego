<template>
  <div class="resume-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container">
        <div class="header-content">
          <div class="header-left">
            <h1 class="page-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
                <line x1="16" y1="13" x2="8" y2="13"/>
                <line x1="16" y1="17" x2="8" y2="17"/>
                <polyline points="10 9 9 9 8 9"/>
              </svg>
              我的简历
            </h1>
            <p class="page-subtitle">完善简历信息，提高求职成功率</p>
          </div>
          <div class="header-actions">
            <button class="btn-preview" @click="showPreview = !showPreview">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              {{ showPreview ? '编辑模式' : '预览简历' }}
            </button>
            <button class="btn-save" @click="handleSave">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
                <polyline points="17 21 17 13 7 13 7 21"/>
                <polyline points="7 3 7 8 15 8"/>
              </svg>
              保存简历
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container">
      <div class="resume-layout">
        <!-- 编辑模式 -->
        <div v-if="!showPreview" class="edit-section">
          <!-- 基本信息 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
                基本信息
              </h2>
              <span class="card-tip">填写真实信息，方便企业联系你</span>
            </div>
            <div class="form-grid">
              <div class="form-group">
                <label>真实姓名 <span class="required">*</span></label>
                <input v-model="form.realName" type="text" placeholder="请输入真实姓名">
              </div>
              <div class="form-group">
                <label>性别</label>
                <div class="radio-group">
                  <label class="radio-option" :class="{ active: form.gender === '男' }">
                    <input type="radio" v-model="form.gender" value="男">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="5"/>
                      <line x1="12" y1="1" x2="12" y2="3"/>
                      <line x1="12" y1="21" x2="12" y2="23"/>
                      <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>
                      <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
                      <line x1="1" y1="12" x2="3" y2="12"/>
                      <line x1="21" y1="12" x2="23" y2="12"/>
                      <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>
                      <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
                    </svg>
                    男
                  </label>
                  <label class="radio-option" :class="{ active: form.gender === '女' }">
                    <input type="radio" v-model="form.gender" value="女">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="8" r="5"/>
                      <line x1="12" y1="13" x2="12" y2="23"/>
                      <line x1="9" y1="18" x2="15" y2="18"/>
                    </svg>
                    女
                  </label>
                </div>
              </div>
              <div class="form-group">
                <label>联系电话 <span class="required">*</span></label>
                <input v-model="form.phone" type="tel" placeholder="请输入手机号">
              </div>
              <div class="form-group">
                <label>邮箱</label>
                <input v-model="form.email" type="email" placeholder="请输入邮箱">
              </div>
            </div>
          </div>

          <!-- 教育信息 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
                  <path d="M6 12v5c3 3 10 3 12 0v-5"/>
                </svg>
                教育信息
              </h2>
            </div>
            <div class="form-grid">
              <div class="form-group">
                <label>学校 <span class="required">*</span></label>
                <input v-model="form.school" type="text" placeholder="请输入学校名称">
              </div>
              <div class="form-group">
                <label>专业</label>
                <input v-model="form.major" type="text" placeholder="请输入专业">
              </div>
              <div class="form-group">
                <label>年级</label>
                <select v-model="form.grade">
                  <option value="">请选择年级</option>
                  <option value="大一">大一</option>
                  <option value="大二">大二</option>
                  <option value="大三">大三</option>
                  <option value="大四">大四</option>
                  <option value="研一">研一</option>
                  <option value="研二">研二</option>
                  <option value="研三">研三</option>
                </select>
              </div>
            </div>
          </div>

          <!-- 求职意向 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <polyline points="12 6 12 12 16 14"/>
                </svg>
                求职意向
              </h2>
            </div>
            <div class="form-grid">
              <div class="form-group">
                <label>期望城市</label>
                <select v-model="form.expectCity">
                  <option value="">请选择城市</option>
                  <option v-for="city in cities" :key="city" :value="city">{{ city }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>期望薪资</label>
                <input v-model="form.expectSalary" type="text" placeholder="如：3000-5000">
              </div>
            </div>
          </div>

          <!-- 个人技能 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <polyline points="16 18 22 12 16 6"/>
                  <polyline points="8 6 2 12 8 18"/>
                </svg>
                个人技能
              </h2>
              <span class="card-tip">多个技能用逗号分隔</span>
            </div>
            <div class="form-group full-width">
              <input v-model="form.skills" type="text" placeholder="如：Java, Python, Vue, PS">
            </div>
            <div class="skill-tags" v-if="form.skills">
              <span v-for="skill in form.skills.split(',')" :key="skill" class="skill-tag">
                {{ skill.trim() }}
              </span>
            </div>
          </div>

          <!-- 经历描述 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <path d="M12 20h9"/>
                  <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/>
                </svg>
                经历描述
              </h2>
            </div>
            <div class="form-group full-width">
              <label>实践经历</label>
              <textarea v-model="form.experience" rows="4" placeholder="描述你的实习、兼职、社会实践等经历"></textarea>
            </div>
            <div class="form-group full-width">
              <label>项目经历</label>
              <textarea v-model="form.projectExperience" rows="4" placeholder="描述你参与的项目，包括项目背景、你的职责、成果等"></textarea>
            </div>
            <div class="form-group full-width">
              <label>自我介绍</label>
              <textarea v-model="form.selfIntro" rows="4" placeholder="简单介绍自己的优势、特长、性格等"></textarea>
            </div>
          </div>

          <!-- 简历附件 -->
          <div class="form-card">
            <div class="card-header">
              <h2 class="card-title">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/>
                </svg>
                简历附件
              </h2>
              <span class="card-tip">上传简历附件，支持 PDF、DOCX 格式</span>
            </div>
            <div class="upload-area" @click="triggerUpload" @dragover.prevent @drop.prevent="handleDrop">
              <input ref="fileInput" type="file" accept=".pdf,.docx" style="display: none" @change="handleFileChange">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                <polyline points="17 8 12 3 7 8"/>
                <line x1="12" y1="3" x2="12" y2="15"/>
              </svg>
              <p>点击或拖拽文件到此处上传</p>
              <span>支持 PDF、DOCX 格式，最大 10MB</span>
            </div>
            <div v-if="form.attachmentUrl" class="file-info">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                <polyline points="22 4 12 14.01 9 11.01"/>
              </svg>
              <span>已上传简历附件</span>
              <button class="btn-remove" @click="form.attachmentUrl = ''">删除</button>
            </div>
            <div v-if="uploadLoading" class="loading-info">
              <div class="loading-spinner"></div>
              <span>正在解析简历，请稍候...</span>
            </div>
          </div>
        </div>

        <!-- 预览模式 -->
        <div v-else class="preview-section">
          <div class="preview-card">
            <div class="preview-header">
              <div class="preview-avatar">
                {{ form.realName?.charAt(0) || '?' }}
              </div>
              <div class="preview-basic">
                <h2>{{ form.realName || '姓名' }}</h2>
                <p>{{ form.school || '学校' }} · {{ form.major || '专业' }} · {{ form.grade || '年级' }}</p>
              </div>
            </div>

            <div class="preview-section-item" v-if="form.phone || form.email">
              <h4>联系方式</h4>
              <div class="preview-tags">
                <span v-if="form.phone">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
                  </svg>
                  {{ form.phone }}
                </span>
                <span v-if="form.email">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                    <polyline points="22,6 12,13 2,6"/>
                  </svg>
                  {{ form.email }}
                </span>
              </div>
            </div>

            <div class="preview-section-item" v-if="form.expectCity || form.expectSalary">
              <h4>求职意向</h4>
              <div class="preview-tags">
                <span v-if="form.expectCity">{{ form.expectCity }}</span>
                <span v-if="form.expectSalary">{{ form.expectSalary }}元</span>
              </div>
            </div>

            <div class="preview-section-item" v-if="form.skills">
              <h4>个人技能</h4>
              <div class="preview-tags">
                <span v-for="skill in form.skills.split(',')" :key="skill">{{ skill.trim() }}</span>
              </div>
            </div>

            <div class="preview-section-item" v-if="form.experience">
              <h4>实践经历</h4>
              <p>{{ form.experience }}</p>
            </div>

            <div class="preview-section-item" v-if="form.projectExperience">
              <h4>项目经历</h4>
              <p>{{ form.projectExperience }}</p>
            </div>

            <div class="preview-section-item" v-if="form.selfIntro">
              <h4>自我介绍</h4>
              <p>{{ form.selfIntro }}</p>
            </div>

            <div v-if="!form.realName && !form.school && !form.skills" class="preview-empty">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
              </svg>
              <p>填写左侧信息后</p>
              <p>这里会实时预览简历效果</p>
            </div>
          </div>
        </div>

        <!-- 右侧提示 -->
        <aside class="tips-sidebar" v-if="!showPreview">
          <div class="tip-card">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="16" x2="12" y2="12"/>
                <line x1="12" y1="8" x2="12.01" y2="8"/>
              </svg>
              简历填写建议
            </h3>
            <ul>
              <li>填写真实信息，方便企业联系你</li>
              <li>技能标签用逗号分隔，便于系统识别</li>
              <li>实践经历要具体，突出你的能力和成果</li>
              <li>自我介绍简洁有力，突出个人优势</li>
              <li>上传简历附件可以提高求职成功率</li>
            </ul>
          </div>
          <div class="tip-card">
            <h3>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2">
                <polyline points="22 4 12 14.01 9 11.01"/>
                <path d="M21 12a9 9 0 1 1-6.219-8.56"/>
              </svg>
              完成度 {{ completionRate }}%
            </h3>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: completionRate + '%' }"></div>
            </div>
            <p class="tip-text">简历完成度越高，获得面试机会越大</p>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMyResume, parseResume, saveResume } from '../../api/resume'
import { uploadResume } from '../../api/file'
import { ElMessage } from 'element-plus'

const form = ref({
  realName: '', gender: '', school: '', major: '', grade: '',
  phone: '', email: '', skills: '', experience: '', projectExperience: '',
  selfIntro: '', expectCity: '', expectSalary: '', attachmentUrl: ''
})

const showPreview = ref(false)
const uploadLoading = ref(false)
const fileInput = ref(null)

const cities = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '重庆', '西安']
const supportedResumePattern = /\.(pdf|docx)$/i
const resumeFields = [
  'realName', 'gender', 'school', 'major', 'grade', 'phone', 'email',
  'skills', 'experience', 'projectExperience', 'selfIntro',
  'expectCity', 'expectSalary'
]

const completionRate = computed(() => {
  let filled = 0
  const total = 8
  if (form.value.realName) filled++
  if (form.value.phone) filled++
  if (form.value.school) filled++
  if (form.value.major) filled++
  if (form.value.skills) filled++
  if (form.value.experience || form.value.projectExperience) filled++
  if (form.value.selfIntro) filled++
  if (form.value.attachmentUrl) filled++
  return Math.round((filled / total) * 100)
})

function triggerUpload() {
  fileInput.value?.click()
}

onMounted(async () => {
  try {
    const res = await getMyResume()
    if (res.data) Object.assign(form.value, res.data)
  } catch {}
})

async function handleSave() {
  await saveResume(form.value)
  ElMessage.success('保存成功')
}

function mergeParsedResume(parsed) {
  if (!parsed) return 0
  let filled = 0
  resumeFields.forEach(field => {
    const value = parsed[field]
    if (value !== undefined && value !== null && String(value).trim() !== '') {
      form.value[field] = String(value).trim()
      filled++
    }
  })
  return filled
}

async function handleFileChange(e) {
  const file = e.target.files[0]
  if (!file) return
  if (!supportedResumePattern.test(file.name)) {
    ElMessage.error('解析回填暂支持 PDF、DOCX 格式')
    if (fileInput.value) fileInput.value.value = ''
    return
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小超出限制')
    if (fileInput.value) fileInput.value.value = ''
    return
  }
  uploadLoading.value = true
  try {
    const uploadRes = await uploadResume(file)
    const fileUrl = uploadRes.data
    if (!fileUrl) {
      throw new Error('简历上传失败：未返回文件地址')
    }
    form.value.attachmentUrl = fileUrl

    const parseRes = await parseResume(fileUrl, file.name)
    const filled = mergeParsedResume(parseRes.data)
    await saveResume(form.value)

    if (filled > 0) {
      ElMessage.success('简历上传并解析成功')
    } else {
      ElMessage.warning('简历上传成功，但未识别到可回填的信息')
    }
  } catch (error) {
    ElMessage.error(error.message || '简历上传或解析失败')
  } finally {
    uploadLoading.value = false
    if (fileInput.value) fileInput.value.value = ''
  }
}

function handleDrop(e) {
  const file = e.dataTransfer.files[0]
  if (file) {
    handleFileChange({ target: { files: [file] } })
  }
}
</script>

<style scoped>
.resume-page {
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
  padding: 24px 0;
  border-bottom: 1px solid #E2E8F0;
  margin-bottom: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 4px;
}

.page-subtitle {
  font-size: 14px;
  color: #64748B;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.btn-preview {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-preview:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-save {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-save:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

/* 布局 */
.resume-layout {
  display: flex;
  gap: 24px;
  padding-bottom: 40px;
}

.edit-section {
  flex: 1;
}

.preview-section {
  flex: 1;
}

.tips-sidebar {
  width: 280px;
  flex-shrink: 0;
}

/* 表单卡片 */
.form-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  margin-bottom: 20px;
  border: 1px solid #E2E8F0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
}

.card-tip {
  font-size: 13px;
  color: #94A3B8;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group.full-width {
  grid-column: span 2;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
}

.required {
  color: #EF4444;
}

.form-group input,
.form-group textarea,
.form-group select {
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

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

/* 单选按钮 */
.radio-group {
  display: flex;
  gap: 12px;
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

/* 技能标签 */
.skill-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 12px;
}

.skill-tag {
  padding: 6px 12px;
  background: #FFF5F0;
  color: #FF6B35;
  border-radius: 8px;
  font-size: 13px;
}

/* 上传区域 */
.upload-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 40px;
  border: 2px dashed #E2E8F0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.upload-area:hover {
  border-color: #FF6B35;
  background: #FFF5F0;
}

.upload-area p {
  font-size: 15px;
  color: #64748B;
}

.upload-area span {
  font-size: 13px;
  color: #94A3B8;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #F0FDF4;
  border-radius: 10px;
  margin-top: 12px;
  font-size: 14px;
  color: #059669;
}

.btn-remove {
  margin-left: auto;
  padding: 4px 12px;
  border: 1px solid #E2E8F0;
  border-radius: 6px;
  background: white;
  color: #EF4444;
  font-size: 13px;
  cursor: pointer;
}

.loading-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #FFF5F0;
  border-radius: 10px;
  margin-top: 12px;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #E2E8F0;
  border-top-color: #FF6B35;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-info span {
  font-size: 14px;
  color: #FF6B35;
}

/* 预览卡片 */
.preview-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  border: 1px solid #E2E8F0;
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 2px solid #F1F5F9;
}

.preview-avatar {
  width: 72px;
  height: 72px;
  border-radius: 18px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 600;
}

.preview-basic h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 8px;
}

.preview-basic p {
  font-size: 15px;
  color: #64748B;
}

.preview-section-item {
  margin-bottom: 28px;
}

.preview-section-item h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #F1F5F9;
}

.preview-section-item p {
  font-size: 15px;
  color: #64748B;
  line-height: 1.8;
  white-space: pre-wrap;
}

.preview-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.preview-tags span {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 14px;
}

.preview-empty {
  text-align: center;
  padding: 60px 0;
  color: #CBD5E1;
}

.preview-empty svg {
  margin-bottom: 20px;
}

.preview-empty p {
  font-size: 15px;
  color: #94A3B8;
  margin-bottom: 4px;
}

/* 提示卡片 */
.tip-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  border: 1px solid #E2E8F0;
  margin-bottom: 16px;
}

.tip-card h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
}

.tip-card ul {
  list-style: none;
  padding: 0;
}

.tip-card li {
  font-size: 13px;
  color: #64748B;
  padding: 6px 0;
  padding-left: 16px;
  position: relative;
}

.tip-card li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 12px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #CBD5E1;
}

.progress-bar {
  height: 8px;
  background: #E2E8F0;
  border-radius: 4px;
  margin-bottom: 8px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  border-radius: 4px;
  transition: width 0.3s;
}

.tip-text {
  font-size: 13px;
  color: #94A3B8;
}

/* 响应式 */
@media (max-width: 1024px) {
  .resume-layout {
    flex-direction: column;
  }

  .tips-sidebar {
    width: 100%;
    display: flex;
    gap: 16px;
  }

  .tip-card {
    flex: 1;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-group.full-width {
    grid-column: span 1;
  }

  .tips-sidebar {
    flex-direction: column;
  }
}
</style>

<template>
  <div class="applications-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
          岗位投递列表
        </h1>
        <p class="page-subtitle">查看并管理学生的投递申请</p>
      </div>
    </div>

    <div class="container">
      <!-- 状态筛选 -->
      <div class="filter-bar">
        <button
          v-for="status in statusOptions"
          :key="status.value"
          class="filter-btn"
          :class="{ active: currentStatus === status.value }"
          @click="filterByStatus(status.value)"
        >
          {{ status.label }}
        </button>
      </div>

      <!-- 申请列表 -->
      <div class="applications-list">
        <div
          v-for="item in list"
          :key="item.id"
          class="application-card"
        >
          <div class="card-header">
            <div class="student-info">
              <div class="student-avatar" :style="item.studentAvatar ? { backgroundImage: `url(${item.studentAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                {{ item.studentAvatar ? '' : item.studentName?.charAt(0) }}
              </div>
              <div>
                <h3 class="student-name">{{ item.studentName }}</h3>
                <span class="school">{{ item.school }}</span>
              </div>
            </div>
            <span class="status-badge" :class="statusClass(item.status)">
              {{ item.statusText }}
            </span>
          </div>

          <div class="card-meta">
            <span class="time">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              投递时间：{{ item.createTime }}
            </span>
          </div>

          <div class="card-actions">
            <button class="btn-chat" @click="startChat(item)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                <line x1="9" y1="10" x2="15" y2="10"/>
              </svg>
              联系学生
            </button>
            <button v-if="item.resumeId" class="btn-view" @click="viewResume(item.resumeId)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
              </svg>
              查看简历
            </button>
            <button v-if="item.status < 3" class="btn-interview" @click="openInterviewDialog(item.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                <line x1="16" y1="2" x2="16" y2="6"/>
                <line x1="8" y1="2" x2="8" y2="6"/>
                <line x1="3" y1="10" x2="21" y2="10"/>
              </svg>
              邀请面试
            </button>
            <button v-if="item.status < 3" class="btn-approve" @click="handleStatus(item.id, 3)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              录用
            </button>
            <button v-if="item.status < 3" class="btn-reject" @click="handleStatus(item.id, 4)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
              拒绝
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
          <circle cx="9" cy="7" r="4"/>
        </svg>
        <h3>暂无投递申请</h3>
        <p>等待学生投递你的岗位</p>
      </div>

      <!-- 分页 -->
      <div v-if="total > 0" class="pagination">
        <button class="page-btn" :disabled="pageNum === 1" @click="pageNum--; fetchData()">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
        </button>
        <button
          v-for="page in totalPages"
          :key="page"
          class="page-btn"
          :class="{ active: pageNum === page }"
          @click="pageNum = page; fetchData()"
        >
          {{ page }}
        </button>
        <button class="page-btn" :disabled="pageNum === totalPages" @click="pageNum++; fetchData()">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 简历详情弹窗 -->
    <div v-if="resumeVisible" class="modal-overlay" @click="resumeVisible = false">
      <div class="modal modal-large" @click.stop>
        <div class="modal-header">
          <h3>简历详情</h3>
          <button class="modal-close" @click="resumeVisible = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="modal-body" v-if="resume">
          <div class="resume-section">
            <h4>基本信息</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="label">姓名</span>
                <span class="value">{{ resume.realName }}</span>
              </div>
              <div class="info-item">
                <span class="label">性别</span>
                <span class="value">{{ resume.gender }}</span>
              </div>
              <div class="info-item">
                <span class="label">学校</span>
                <span class="value">{{ resume.school }}</span>
              </div>
              <div class="info-item">
                <span class="label">专业</span>
                <span class="value">{{ resume.major }}</span>
              </div>
              <div class="info-item">
                <span class="label">年级</span>
                <span class="value">{{ resume.grade }}</span>
              </div>
              <div class="info-item">
                <span class="label">电话</span>
                <span class="value">{{ resume.phone }}</span>
              </div>
            </div>
          </div>
          <div class="resume-section" v-if="resume.skills">
            <h4>技能标签</h4>
            <div class="skills-tags">
              <span v-for="skill in resume.skills.split(',')" :key="skill">{{ skill.trim() }}</span>
            </div>
          </div>
          <div class="resume-section" v-if="resume.experience">
            <h4>实践经历</h4>
            <p>{{ resume.experience }}</p>
          </div>
          <div class="resume-section" v-if="resume.selfIntro">
            <h4>自我介绍</h4>
            <p>{{ resume.selfIntro }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 面试邀请弹窗 -->
    <div v-if="interviewVisible" class="modal-overlay" @click="interviewVisible = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>发送面试邀请</h3>
          <button class="modal-close" @click="interviewVisible = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>面试时间 <span class="required">*</span></label>
            <input v-model="interviewForm.interviewTime" type="datetime-local">
          </div>
          <div class="form-group">
            <label>面试方式</label>
            <div class="radio-group">
              <label class="radio-option" :class="{ active: interviewForm.interviewType === '线下' }">
                <input type="radio" v-model="interviewForm.interviewType" value="线下">
                <span>线下面试</span>
              </label>
              <label class="radio-option" :class="{ active: interviewForm.interviewType === '线上' }">
                <input type="radio" v-model="interviewForm.interviewType" value="线上">
                <span>线上面试</span>
              </label>
            </div>
          </div>
          <div class="form-group">
            <label>面试地点</label>
            <input v-model="interviewForm.interviewPlace" type="text" placeholder="如：北京市海淀区中关村">
          </div>
          <div class="form-group">
            <label>面试说明</label>
            <textarea v-model="interviewForm.interviewContent" rows="3" placeholder="如：请携带简历参加面试"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="interviewVisible = false">取消</button>
          <button class="btn-confirm" @click="submitInterview" :disabled="interviewLoading">
            <span v-if="interviewLoading" class="loading-spinner"></span>
            <span v-else>发送邀请</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getJobApplications, updateApplicationStatus } from '../../api/application'
import { createInterview } from '../../api/interview'
import { getResumeById } from '../../api/resume'
import { getOrCreateSession } from '../../api/chat'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const jobId = route.params.id
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10
const currentStatus = ref('')
const resumeVisible = ref(false)
const resume = ref(null)
const interviewVisible = ref(false)
const interviewLoading = ref(false)
const currentApplicationId = ref(null)
const interviewForm = ref({
  interviewTime: '',
  interviewType: '线下',
  interviewPlace: '',
  interviewContent: ''
})

const statusOptions = [
  { label: '全部', value: '' },
  { label: '待处理', value: '0' },
  { label: '已邀请', value: '2' },
  { label: '已录用', value: '3' },
  { label: '已拒绝', value: '4' }
]

const totalPages = computed(() => Math.ceil(total.value / pageSize))

function statusClass(s) {
  return ['pending', '', 'invited', 'approved', 'rejected'][s] || 'pending'
}

function filterByStatus(status) {
  currentStatus.value = status
  pageNum.value = 1
  fetchData()
}

async function fetchData() {
  const params = { pageNum: pageNum.value, pageSize }
  if (currentStatus.value) params.status = currentStatus.value
  const res = await getJobApplications(jobId, params)
  list.value = res.data.records
  total.value = res.data.total
}

async function handleStatus(appId, status) {
  const labels = { 3: '录用', 4: '拒绝' }
  let remark = ''
  if (status === 4) {
    const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝', { inputType: 'textarea' })
    remark = value
  }
  await updateApplicationStatus(appId, status, remark)
  ElMessage.success(labels[status] + '成功')
  fetchData()
}

async function startChat(item) {
  try {
    // 获取或创建与学生的会话
    const res = await getOrCreateSession(item.userId)
    // 跳转到聊天页面，带上会话ID
    router.push({ path: '/chat', query: { sessionId: res.data } })
  } catch {
    ElMessage.error('无法创建会话')
  }
}

function openInterviewDialog(appId) {
  currentApplicationId.value = appId
  interviewForm.value = { interviewTime: '', interviewType: '线下', interviewPlace: '', interviewContent: '' }
  interviewVisible.value = true
}

async function submitInterview() {
  if (!interviewForm.value.interviewTime) {
    ElMessage.warning('请选择面试时间')
    return
  }
  interviewLoading.value = true
  try {
    await createInterview({
      applicationId: currentApplicationId.value,
      interviewTime: interviewForm.value.interviewTime,
      interviewType: interviewForm.value.interviewType,
      interviewPlace: interviewForm.value.interviewPlace,
      interviewContent: interviewForm.value.interviewContent
    })
    ElMessage.success('面试邀请已发送')
    interviewVisible.value = false
    fetchData()
  } finally {
    interviewLoading.value = false
  }
}

async function viewResume(id) {
  const res = await getResumeById(id)
  resume.value = res.data
  resumeVisible.value = true
}

onMounted(fetchData)
</script>

<style scoped>
.applications-page {
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
  margin-bottom: 24px;
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

/* 筛选栏 */
.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-btn:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.filter-btn.active {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  border-color: transparent;
}

/* 申请列表 */
.applications-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.application-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.application-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.student-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
}

.student-name {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.school {
  font-size: 14px;
  color: #64748B;
}

.status-badge {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
}

.status-badge.pending {
  background: #FEF3C7;
  color: #D97706;
}

.status-badge.invited {
  background: #DBEAFE;
  color: #2563EB;
}

.status-badge.approved {
  background: #D1FAE5;
  color: #059669;
}

.status-badge.rejected {
  background: #FEE2E2;
  color: #DC2626;
}

.card-meta {
  margin-bottom: 16px;
}

.time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #94A3B8;
}

/* 操作按钮 */
.card-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #F1F5F9;
  flex-wrap: wrap;
}

.card-actions button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-view {
  border: 1px solid #E2E8F0;
  background: white;
  color: #64748B;
}

.btn-view:hover {
  border-color: #3B82F6;
  color: #3B82F6;
}

.btn-chat {
  border: 1px solid #E2E8F0;
  background: white;
  color: #8B5CF6;
}

.btn-chat:hover {
  border-color: #8B5CF6;
  background: #F5F3FF;
}

.btn-interview {
  border: 1px solid #E2E8F0;
  background: white;
  color: #8B5CF6;
}

.btn-interview:hover {
  border-color: #8B5CF6;
  background: #F5F3FF;
}

.btn-approve {
  border: 1px solid #E2E8F0;
  background: white;
  color: #059669;
}

.btn-approve:hover {
  border-color: #059669;
  background: #D1FAE5;
}

.btn-reject {
  border: 1px solid #E2E8F0;
  background: white;
  color: #EF4444;
}

.btn-reject:hover {
  border-color: #EF4444;
  background: #FEF2F2;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 0;
  background: white;
  border-radius: 16px;
  border: 1px solid #E2E8F0;
}

.empty-state svg {
  margin-bottom: 24px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 20px;
  color: #1E293B;
  margin-bottom: 8px;
}

.empty-state p {
  font-size: 16px;
  color: #64748B;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  padding-bottom: 40px;
}

.page-btn {
  min-width: 40px;
  height: 40px;
  padding: 0 12px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-btn:hover:not(:disabled) {
  border-color: #FF6B35;
  color: #FF6B35;
}

.page-btn.active {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  border-color: transparent;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal {
  background: white;
  border-radius: 20px;
  width: 500px;
  max-width: 90%;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-large {
  width: 700px;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #F1F5F9;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
}

.modal-close {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #94A3B8;
  transition: color 0.2s;
}

.modal-close:hover {
  color: #1E293B;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
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
.form-group textarea {
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
.form-group textarea:focus {
  border-color: #FF6B35;
  background: white;
  box-shadow: 0 0 0 4px rgba(255, 107, 53, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

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

.modal-footer {
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
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 100px;
}

.btn-confirm:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.btn-confirm:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 简历详情 */
.resume-section {
  margin-bottom: 24px;
}

.resume-section h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #F1F5F9;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-item .label {
  font-size: 13px;
  color: #94A3B8;
}

.info-item .value {
  font-size: 15px;
  color: #1E293B;
}

.skills-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.skills-tags span {
  padding: 6px 12px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 13px;
}

.resume-section p {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 12px;
  }

  .card-actions {
    flex-direction: column;
  }

  .card-actions button {
    justify-content: center;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>

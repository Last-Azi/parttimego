<template>
  <div class="interview-manage-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>
          面试管理
        </h1>
        <p class="page-subtitle">管理面试邀请，安排面试流程</p>
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

      <!-- 面试列表 -->
      <div class="interviews-list">
        <div
          v-for="item in list"
          :key="item.id"
          class="interview-card"
          :class="{ highlight: highlightId == item.id }"
        >
          <div class="card-header">
            <div class="interview-info">
              <h3 class="job-title">{{ item.jobTitle }}</h3>
              <div class="student-info">
                <div class="student-avatar" :style="item.studentAvatar ? { backgroundImage: `url(${item.studentAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                  {{ item.studentAvatar ? '' : item.studentName?.charAt(0) }}
                </div>
                <div>
                  <span class="student-name">{{ item.studentName }}</span>
                  <span class="student-phone" v-if="item.studentPhone">{{ item.studentPhone }}</span>
                </div>
              </div>
            </div>
            <span class="status-badge" :class="statusClass(item.status)">
              {{ item.statusText }}
            </span>
          </div>

          <div class="interview-details">
            <div class="detail-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              <span>{{ item.interviewTime }}</span>
            </div>
            <div class="detail-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              <span>{{ item.interviewPlace || '待定' }}</span>
            </div>
            <div class="detail-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
              </svg>
              <span>{{ item.interviewType }}</span>
            </div>
          </div>

          <div v-if="item.interviewContent" class="interview-content">
            <h4>面试说明</h4>
            <p>{{ item.interviewContent }}</p>
          </div>

          <div class="card-actions">
            <button v-if="item.status === 1" class="btn-complete" @click="handleComplete(item.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              标记完成
            </button>
            <button v-if="item.status === 3" class="btn-hire" @click="handleHire(item)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="8.5" cy="7" r="4"/>
                <polyline points="17 11 19 13 23 9"/>
              </svg>
              录用
            </button>
            <button v-if="item.status === 3" class="btn-reject" @click="handleReject(item)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
              拒绝
            </button>
            <button v-if="item.status <= 1" class="btn-cancel" @click="handleCancel(item.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="15" y1="9" x2="9" y2="15"/>
                <line x1="9" y1="9" x2="15" y2="15"/>
              </svg>
              取消面试
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        <h3>暂无面试安排</h3>
        <p>邀请学生面试后会在这里显示</p>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getEmployerInterviews, completeInterview, cancelInterview } from '../../api/interview'
import { updateApplicationStatus } from '../../api/application'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10
const currentStatus = ref('')
const highlightId = ref(null)

const statusOptions = [
  { label: '全部', value: '' },
  { label: '待确认', value: '0' },
  { label: '已接受', value: '1' },
  { label: '已拒绝', value: '2' },
  { label: '已完成', value: '3' },
  { label: '已取消', value: '4' }
]

const totalPages = computed(() => Math.ceil(total.value / pageSize))

function statusClass(s) {
  return ['pending', 'accepted', 'rejected', 'completed', 'cancelled'][s] || 'pending'
}

function filterByStatus(status) {
  currentStatus.value = status
  pageNum.value = 1
  fetchData()
}

async function fetchData() {
  const params = { pageNum: pageNum.value, pageSize }
  if (currentStatus.value) params.status = currentStatus.value
  const res = await getEmployerInterviews(params)
  list.value = res.data.records
  total.value = res.data.total
}

async function handleComplete(id) {
  await ElMessageBox.confirm('确认标记面试完成？', '提示')
  await completeInterview(id)
  ElMessage.success('操作成功')
  fetchData()
}

async function handleHire(row) {
  await ElMessageBox.confirm(`确认录用 ${row.studentName}？`, '录用确认')
  await updateApplicationStatus(row.applicationId, 3, '')
  ElMessage.success('录用成功，已通知学生')
  fetchData()
}

async function handleReject(row) {
  const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝', { inputType: 'textarea' })
  await updateApplicationStatus(row.applicationId, 4, value || '')
  ElMessage.success('已拒绝，已通知学生')
  fetchData()
}

async function handleCancel(id) {
  await ElMessageBox.confirm('确认取消面试邀请？', '提示')
  await cancelInterview(id)
  ElMessage.success('操作成功')
  fetchData()
}

onMounted(() => {
  if (route.query.highlight) {
    highlightId.value = route.query.highlight
  }
  fetchData()
})
</script>

<style scoped>
.interview-manage-page {
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

/* 面试列表 */
.interviews-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 32px;
}

.interview-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.interview-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.interview-card.highlight {
  border-color: #FF6B35;
  background: #FFF5F0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.job-title {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
}

.student-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.student-avatar {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 600;
}

.student-name {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
}

.student-phone {
  font-size: 13px;
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

.status-badge.accepted {
  background: #D1FAE5;
  color: #059669;
}

.status-badge.rejected {
  background: #FEE2E2;
  color: #DC2626;
}

.status-badge.completed {
  background: #DBEAFE;
  color: #2563EB;
}

.status-badge.cancelled {
  background: #F3F4F6;
  color: #6B7280;
}

/* 面试详情 */
.interview-details {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748B;
}

.detail-item svg {
  color: #94A3B8;
}

/* 面试内容 */
.interview-content {
  padding: 16px;
  background: #F8FAFC;
  border-radius: 12px;
  margin-bottom: 20px;
}

.interview-content h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 8px;
}

.interview-content p {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
}

/* 操作按钮 */
.card-actions {
  display: flex;
  gap: 12px;
  padding-top: 20px;
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

.btn-complete {
  border: 1px solid #E2E8F0;
  background: white;
  color: #3B82F6;
}

.btn-complete:hover {
  border-color: #3B82F6;
  background: #EFF6FF;
}

.btn-hire {
  border: 1px solid #E2E8F0;
  background: white;
  color: #059669;
}

.btn-hire:hover {
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

.btn-cancel {
  border: 1px solid #E2E8F0;
  background: white;
  color: #D97706;
}

.btn-cancel:hover {
  border-color: #D97706;
  background: #FEF3C7;
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

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 12px;
  }

  .interview-details {
    flex-direction: column;
    gap: 12px;
  }

  .card-actions {
    flex-direction: column;
  }

  .card-actions button {
    justify-content: center;
  }
}
</style>

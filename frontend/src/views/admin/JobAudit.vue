<template>
  <div class="audit-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
          岗位审核
        </h1>
        <p class="page-subtitle">审核企业发布的兼职岗位</p>
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

      <!-- 岗位列表 -->
      <div class="jobs-list">
        <div
          v-for="job in list"
          :key="job.id"
          class="job-card"
        >
          <div class="card-header">
            <div class="job-info">
              <h3 class="job-title">{{ job.title }}</h3>
              <div class="job-meta">
                <span class="publisher">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                    <circle cx="12" cy="7" r="4"/>
                  </svg>
                  {{ job.publisherName }}
                </span>
                <span class="city">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                    <circle cx="12" cy="10" r="3"/>
                  </svg>
                  {{ job.city }}
                </span>
                <span class="time">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"/>
                    <polyline points="12 6 12 12 16 14"/>
                  </svg>
                  {{ job.createTime }}
                </span>
              </div>
            </div>
            <span class="status-badge" :class="statusClass(job.status)">
              {{ statusText(job.status) }}
            </span>
          </div>

          <div class="card-actions">
            <button class="btn-view" @click="$router.push(`/job/${job.id}`)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              查看详情
            </button>
            <button v-if="job.status === 0" class="btn-approve" @click="handleApprove(job.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              审核通过
            </button>
            <button v-if="job.status === 0" class="btn-reject" @click="handleReject(job.id)">
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
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
          <polyline points="22 4 12 14.01 9 11.01"/>
        </svg>
        <h3>暂无岗位</h3>
        <p>没有需要审核的岗位</p>
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
import { adminListJobs, approveJob, rejectJob } from '../../api/job'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10
const currentStatus = ref('')

const statusOptions = [
  { label: '全部', value: '' },
  { label: '待审核', value: '0' },
  { label: '已发布', value: '1' },
  { label: '已下架', value: '2' },
  { label: '已拒绝', value: '3' }
]

const totalPages = computed(() => Math.ceil(total.value / pageSize))

function statusClass(s) {
  return ['pending', 'published', 'offline', 'rejected'][s] || 'pending'
}

function statusText(s) {
  return ['待审核', '已发布', '已下架', '已拒绝'][s] || '未知'
}

function filterByStatus(status) {
  currentStatus.value = status
  pageNum.value = 1
  fetchData()
}

async function fetchData() {
  const params = { pageNum: pageNum.value, pageSize }
  if (currentStatus.value !== '') params.status = currentStatus.value
  const res = await adminListJobs(params)
  list.value = res.data.records
  total.value = res.data.total
}

async function handleApprove(id) {
  await approveJob(id)
  ElMessage.success('审核通过')
  fetchData()
}

async function handleReject(id) {
  const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝', { inputType: 'textarea' })
  await rejectJob(id, value)
  ElMessage.success('已拒绝')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.audit-page {
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

/* 岗位列表 */
.jobs-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.job-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.job-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
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
  margin-bottom: 8px;
}

.job-meta {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.job-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
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

.status-badge.published {
  background: #D1FAE5;
  color: #059669;
}

.status-badge.offline {
  background: #F3F4F6;
  color: #6B7280;
}

.status-badge.rejected {
  background: #FEE2E2;
  color: #DC2626;
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

.btn-view {
  border: 1px solid #E2E8F0;
  background: white;
  color: #64748B;
}

.btn-view:hover {
  border-color: #3B82F6;
  color: #3B82F6;
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

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 12px;
  }

  .job-meta {
    flex-direction: column;
    gap: 8px;
  }

  .card-actions {
    flex-direction: column;
  }

  .card-actions button {
    justify-content: center;
  }
}
</style>

<template>
  <div class="my-jobs-page">
    <div class="page-header">
      <div class="container">
        <div class="header-content">
          <div>
            <h1 class="page-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
              我的岗位
            </h1>
            <p class="page-subtitle">管理发布的兼职岗位</p>
          </div>
          <button class="btn-publish" @click="$router.push('/employer/job/publish')">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="16"/>
              <line x1="8" y1="12" x2="16" y2="12"/>
            </svg>
            发布岗位
          </button>
        </div>
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
          <div class="card-main">
            <div class="card-header">
              <div class="job-info">
                <h3 class="job-title">{{ job.title }}</h3>
                <div class="job-meta">
                  <span class="salary">{{ job.salaryMin }}-{{ job.salaryMax }}元/{{ job.salaryType }}</span>
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

            <div class="job-stats">
              <span class="stat">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                {{ job.viewCount || 0 }}人看过
              </span>
              <span class="stat">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                  <circle cx="9" cy="7" r="4"/>
                </svg>
                {{ job.applyCount || 0 }}人投递
              </span>
            </div>
          </div>

          <div class="card-actions">
            <button class="btn-view" @click="$router.push(`/employer/job/${job.id}/applications`)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="9" cy="7" r="4"/>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
              </svg>
              查看投递
            </button>
            <button class="btn-edit" @click="$router.push(`/employer/job/publish?editId=${job.id}`)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 20h9"/>
                <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/>
              </svg>
              编辑
            </button>
            <button v-if="job.status === 1" class="btn-offline" @click="handleOffline(job.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
                <line x1="1" y1="1" x2="23" y2="23"/>
              </svg>
              下架
            </button>
            <button class="btn-delete" @click="handleDelete(job.id)">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
              </svg>
              删除
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
          <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
        </svg>
        <h3>暂无岗位</h3>
        <p>发布第一个兼职岗位吧</p>
        <button class="btn-publish-main" @click="$router.push('/employer/job/publish')">发布岗位</button>
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
import { getMyJobs, offlineJob, deleteJob } from '../../api/job'
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
  if (currentStatus.value) params.status = currentStatus.value
  const res = await getMyJobs(params)
  list.value = res.data.records
  total.value = res.data.total
}

async function handleOffline(id) {
  await ElMessageBox.confirm('确认下架该岗位？', '提示')
  await offlineJob(id)
  ElMessage.success('已下架')
  fetchData()
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确认删除该岗位？删除后不可恢复', '警告', { type: 'warning' })
  await deleteJob(id)
  ElMessage.success('已删除')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.my-jobs-page {
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
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 15px;
  color: #64748B;
}

.btn-publish {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-publish:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
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
  border: 1px solid #E2E8F0;
  overflow: hidden;
  transition: all 0.3s;
}

.job-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.card-main {
  padding: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
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

.salary {
  color: #FF6B35 !important;
  font-weight: 600;
}

.status-badge {
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  white-space: nowrap;
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

.job-stats {
  display: flex;
  gap: 20px;
}

.stat {
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
  padding: 16px 24px;
  background: #F8FAFC;
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

.btn-edit {
  border: 1px solid #E2E8F0;
  background: white;
  color: #64748B;
}

.btn-edit:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-offline {
  border: 1px solid #E2E8F0;
  background: white;
  color: #D97706;
}

.btn-offline:hover {
  border-color: #D97706;
  background: #FEF3C7;
}

.btn-delete {
  border: 1px solid #E2E8F0;
  background: white;
  color: #EF4444;
}

.btn-delete:hover {
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
  margin-bottom: 24px;
}

.btn-publish-main {
  padding: 14px 28px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-publish-main:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
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
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

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

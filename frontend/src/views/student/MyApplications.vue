<template>
  <div class="applications-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
            <polyline points="22 4 12 14.01 9 11.01"/>
          </svg>
          我的投递
        </h1>
        <p class="page-subtitle">查看投递状态，跟踪求职进度</p>
      </div>
    </div>

    <div class="container">
      <!-- 状态统计 -->
      <div class="status-stats">
        <div class="stat-item" :class="{ active: currentStatus === '' }" @click="filterByStatus('')">
          <span class="stat-num">{{ total }}</span>
          <span class="stat-label">全部</span>
        </div>
        <div class="stat-item" :class="{ active: currentStatus === '0' }" @click="filterByStatus('0')">
          <span class="stat-num pending">{{ statusCount.pending }}</span>
          <span class="stat-label">待审核</span>
        </div>
        <div class="stat-item" :class="{ active: currentStatus === '1' }" @click="filterByStatus('1')">
          <span class="stat-num approved">{{ statusCount.approved }}</span>
          <span class="stat-label">已通过</span>
        </div>
        <div class="stat-item" :class="{ active: currentStatus === '2' }" @click="filterByStatus('2')">
          <span class="stat-num rejected">{{ statusCount.rejected }}</span>
          <span class="stat-label">已拒绝</span>
        </div>
      </div>

      <!-- 投递列表 -->
      <div class="applications-list">
        <div
          v-for="item in list"
          :key="item.id"
          class="application-card"
          :class="{ highlight: highlightId == item.id }"
        >
          <div class="card-main" @click="$router.push(`/job/${item.jobId}`)">
            <div class="card-header">
              <div class="job-info">
                <h3 class="job-title">{{ item.jobTitle }}</h3>
                <div class="job-meta">
                  <span class="company">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                      <circle cx="12" cy="7" r="4"/>
                    </svg>
                    {{ item.companyName }}
                  </span>
                  <span class="city">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                      <circle cx="12" cy="10" r="3"/>
                    </svg>
                    {{ item.city }}
                  </span>
                </div>
              </div>
              <span class="status-badge" :class="statusClass(item.status)">
                {{ item.statusText }}
              </span>
            </div>

            <div class="card-footer">
              <span class="apply-time">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <polyline points="12 6 12 12 16 14"/>
                </svg>
                投递于 {{ item.createTime }}
              </span>
            </div>
          </div>

          <div class="card-actions">
            <button class="btn-chat" @click="startChat(item)">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
              </svg>
              联系企业
            </button>
            <button v-if="item.status <= 1" class="btn-withdraw" @click="handleWithdraw(item.id)">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="1 4 1 10 7 10"/>
                <path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"/>
              </svg>
              撤回投递
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
        <h3>暂无投递记录</h3>
        <p>快去寻找心仪的兼职岗位吧</p>
        <button class="btn-search" @click="$router.push('/search')">去找兼职</button>
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
import { useRoute, useRouter } from 'vue-router'
import { getMyApplications, withdrawApplication } from '../../api/application'
import { getOrCreateSession } from '../../api/chat'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10
const currentStatus = ref('')
const highlightId = ref(null)
const statusCount = ref({ pending: 0, approved: 0, rejected: 0 })

const totalPages = computed(() => Math.ceil(total.value / pageSize))

function statusClass(s) {
  return ['pending', 'approved', 'rejected', 'cancelled'][s] || 'pending'
}

function filterByStatus(status) {
  currentStatus.value = status
  pageNum.value = 1
  fetchData()
}

async function fetchData() {
  const params = { pageNum: pageNum.value, pageSize }
  if (currentStatus.value) params.status = currentStatus.value
  const res = await getMyApplications(params)
  list.value = res.data.records
  total.value = res.data.total

  // 统计各状态数量（简化处理，实际应从后端获取）
  if (!currentStatus.value) {
    statusCount.value = {
      pending: list.value.filter(i => i.status === 0).length,
      approved: list.value.filter(i => i.status === 1).length,
      rejected: list.value.filter(i => i.status === 2).length
    }
  }
}

async function handleWithdraw(id) {
  await ElMessageBox.confirm('确认撤回该投递？', '提示')
  await withdrawApplication(id)
  ElMessage.success('已撤回')
  fetchData()
}

async function startChat(item) {
  try {
    // 获取或创建与企业的会话
    const res = await getOrCreateSession(item.employerId)
    // 跳转到聊天页面，带上会话ID
    router.push({ path: '/chat', query: { sessionId: res.data } })
  } catch {
    ElMessage.error('无法创建会话')
  }
}

onMounted(() => {
  if (route.query.highlight) {
    highlightId.value = route.query.highlight
  }
  fetchData()
})
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

/* 状态统计 */
.status-stats {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.stat-item {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.2s;
}

.stat-item:hover {
  border-color: #FF6B35;
}

.stat-item.active {
  border-color: #FF6B35;
  background: #FFF5F0;
}

.stat-num {
  display: block;
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 4px;
}

.stat-num.pending {
  color: #D97706;
}

.stat-num.approved {
  color: #059669;
}

.stat-num.rejected {
  color: #DC2626;
}

.stat-label {
  font-size: 14px;
  color: #64748B;
}

/* 投递列表 */
.applications-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.application-card {
  background: white;
  border-radius: 16px;
  border: 1px solid #E2E8F0;
  overflow: hidden;
  transition: all 0.3s;
}

.application-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.application-card.highlight {
  border-color: #FF6B35;
  background: #FFF5F0;
}

.card-main {
  padding: 24px;
  cursor: pointer;
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
  white-space: nowrap;
}

.status-badge.pending {
  background: #FEF3C7;
  color: #D97706;
}

.status-badge.approved {
  background: #D1FAE5;
  color: #059669;
}

.status-badge.rejected {
  background: #FEE2E2;
  color: #DC2626;
}

.status-badge.cancelled {
  background: #F3F4F6;
  color: #6B7280;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.apply-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #94A3B8;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  padding: 0 24px 16px;
}

.btn-withdraw {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: white;
  color: #EF4444;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-withdraw:hover {
  border-color: #EF4444;
  background: #FEF2F2;
}

.btn-chat {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: white;
  color: #3B82F6;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-chat:hover {
  border-color: #3B82F6;
  background: #EFF6FF;
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

.btn-search {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-search:hover {
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
  .status-stats {
    flex-wrap: wrap;
  }

  .stat-item {
    flex: 1 1 calc(50% - 8px);
  }

  .card-header {
    flex-direction: column;
    gap: 12px;
  }

  .job-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

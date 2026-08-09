<template>
  <div class="dashboard-page">
    <div class="page-header">
      <div class="container">
        <div class="header-content">
          <div>
            <h1 class="page-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M21.21 15.89A10 10 0 1 1 8 2.83"/>
                <path d="M22 12A10 10 0 0 0 12 2v10z"/>
              </svg>
              企业后台
            </h1>
            <p class="page-subtitle">管理企业招聘，查看数据统计</p>
          </div>
          <button class="btn-publish" @click="$router.push('/employer/job/publish')">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="16"/>
              <line x1="8" y1="12" x2="16" y2="12"/>
            </svg>
            发布新岗位
          </button>
        </div>
      </div>
    </div>

    <div class="container">
      <!-- 数据统计卡片 -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon jobs">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
              <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
            </svg>
          </div>
          <div class="stat-content">
            <span class="stat-number">{{ stats.jobCount }}</span>
            <span class="stat-label">发布岗位</span>
          </div>
          <div class="stat-trend up">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
            +12%
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon applications">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
              <circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
              <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
          </div>
          <div class="stat-content">
            <span class="stat-number">{{ stats.applicationCount }}</span>
            <span class="stat-label">收到投递</span>
          </div>
          <div class="stat-trend up">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
            +8%
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon interviews">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
              <line x1="16" y1="2" x2="16" y2="6"/>
              <line x1="8" y1="2" x2="8" y2="6"/>
              <line x1="3" y1="10" x2="21" y2="10"/>
            </svg>
          </div>
          <div class="stat-content">
            <span class="stat-number">{{ stats.interviewCount }}</span>
            <span class="stat-label">面试安排</span>
          </div>
          <div class="stat-trend neutral">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
            0%
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon passRate">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <polyline points="22 4 12 14.01 9 11.01"/>
              <path d="M21 12a9 9 0 1 1-6.219-8.56"/>
            </svg>
          </div>
          <div class="stat-content">
            <span class="stat-number">{{ stats.passRate.toFixed(1) }}%</span>
            <span class="stat-label">录用通过率</span>
          </div>
          <div class="stat-trend up">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/>
              <polyline points="17 6 23 6 23 12"/>
            </svg>
            +5%
          </div>
        </div>
      </div>

      <!-- 快捷操作和最近动态 -->
      <div class="content-grid">
        <!-- 快捷操作 -->
        <div class="quick-actions-card">
          <h2 class="card-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
              <polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/>
            </svg>
            快捷操作
          </h2>
          <div class="actions-grid">
            <div class="action-item" @click="$router.push('/employer/job/publish')">
              <div class="action-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="12" y1="8" x2="12" y2="16"/>
                  <line x1="8" y1="12" x2="16" y2="12"/>
                </svg>
              </div>
              <span>发布新岗位</span>
            </div>
            <div class="action-item" @click="$router.push('/employer/jobs')">
              <div class="action-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#3B82F6" stroke-width="2">
                  <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                  <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg>
              </div>
              <span>管理岗位</span>
            </div>
            <div class="action-item" @click="$router.push('/employer/interviews')">
              <div class="action-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#8B5CF6" stroke-width="2">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                  <line x1="16" y1="2" x2="16" y2="6"/>
                  <line x1="8" y1="2" x2="8" y2="6"/>
                  <line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
              </div>
              <span>面试管理</span>
            </div>
            <div class="action-item" @click="$router.push('/employer/messages')">
              <div class="action-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#059669" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                </svg>
              </div>
              <span>消息中心</span>
            </div>
          </div>
        </div>

        <!-- 最近投递 -->
        <div class="recent-card">
          <h2 class="card-title">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <polyline points="12 6 12 12 16 14"/>
            </svg>
            最近投递
          </h2>
          <div class="recent-list">
            <div v-for="item in recentApplications" :key="item.id" class="recent-item" @click="$router.push(`/employer/job/${item.jobId}/applications`)">
              <div class="recent-avatar">{{ item.studentName?.charAt(0) }}</div>
              <div class="recent-info">
                <span class="recent-name">{{ item.studentName }}</span>
                <span class="recent-job">投递了 {{ item.jobTitle }}</span>
              </div>
              <span class="recent-time">{{ item.createTime }}</span>
            </div>
            <div v-if="recentApplications.length === 0" class="recent-empty">
              <p>暂无投递记录</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getEmployerStats } from '../../api/employer'
import { getMyJobs } from '../../api/job'

const stats = ref({
  jobCount: 0,
  applicationCount: 0,
  interviewCount: 0,
  passRate: 0
})

const recentApplications = ref([])

onMounted(async () => {
  try {
    const res = await getEmployerStats()
    stats.value = res.data
  } catch {}

  // 获取最近投递（简化处理）
  try {
    const res = await getMyJobs({ pageNum: 1, pageSize: 5 })
    // 这里应该获取投递记录，简化处理
  } catch {}
})
</script>

<style scoped>
.dashboard-page {
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

/* 数据统计 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon.jobs {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
}

.stat-icon.applications {
  background: linear-gradient(135deg, #3B82F6, #60A5FA);
}

.stat-icon.interviews {
  background: linear-gradient(135deg, #8B5CF6, #A78BFA);
}

.stat-icon.passRate {
  background: linear-gradient(135deg, #059669, #34D399);
}

.stat-content {
  flex: 1;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #64748B;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 6px;
}

.stat-trend.up {
  color: #059669;
  background: #D1FAE5;
}

.stat-trend.down {
  color: #DC2626;
  background: #FEE2E2;
}

.stat-trend.neutral {
  color: #6B7280;
  background: #F3F4F6;
}

/* 内容网格 */
.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 40px;
}

/* 快捷操作 */
.quick-actions-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
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

.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #F8FAFC;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.action-item:hover {
  background: #FFF5F0;
  transform: translateX(4px);
}

.action-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.action-item span {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
}

/* 最近投递 */
.recent-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  border: 1px solid #E2E8F0;
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recent-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #F8FAFC;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.recent-item:hover {
  background: #FFF5F0;
}

.recent-avatar {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.recent-info {
  flex: 1;
  min-width: 0;
}

.recent-name {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 2px;
}

.recent-job {
  font-size: 12px;
  color: #64748B;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recent-time {
  font-size: 12px;
  color: #94A3B8;
  flex-shrink: 0;
}

.recent-empty {
  text-align: center;
  padding: 24px;
  color: #94A3B8;
  font-size: 14px;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }
}
</style>

<template>
  <div class="job-detail-page" v-if="job">
    <!-- 面包屑 -->
    <div class="breadcrumb-bar">
      <div class="container">
        <router-link to="/" class="breadcrumb-link">首页</router-link>
        <span class="breadcrumb-sep">/</span>
        <router-link to="/search" class="breadcrumb-link">找兼职</router-link>
        <span class="breadcrumb-sep">/</span>
        <span class="breadcrumb-current">{{ job.title }}</span>
      </div>
    </div>

    <div class="container">
      <div class="detail-layout">
        <!-- 左侧主内容 -->
        <main class="main-content">
          <!-- 岗位头部卡片 -->
          <div class="job-header-card">
            <div class="job-header-top">
              <div class="job-title-section">
                <h1 class="job-title">{{ job.title }}</h1>
                <div class="job-tags">
                  <span class="tag">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                      <circle cx="12" cy="10" r="3"/>
                    </svg>
                    {{ job.city }}
                  </span>
                  <span class="tag">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                      <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                    </svg>
                    {{ job.category }}
                  </span>
                  <span class="tag">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="10"/>
                      <polyline points="12 6 12 12 16 14"/>
                    </svg>
                    {{ job.workTime }}
                  </span>
                  <span class="tag">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                      <circle cx="9" cy="7" r="4"/>
                    </svg>
                    招{{ job.headcount }}人
                  </span>
                </div>
              </div>
              <div class="salary-section">
                <span class="salary-amount">{{ job.salaryMin }}-{{ job.salaryMax }}</span>
                <span class="salary-unit">元/{{ job.salaryType }}</span>
              </div>
            </div>

            <div class="job-actions" v-if="store.isStudent">
              <button
                class="btn-favorite"
                :class="{ active: isFav }"
                @click="toggleFav"
              >
                <svg width="18" height="18" viewBox="0 0 24 24" :fill="isFav ? '#FF6B35' : 'none'" :stroke="isFav ? '#FF6B35' : 'currentColor'" stroke-width="2">
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                </svg>
                {{ isFav ? '已收藏' : '收藏' }}
              </button>
              <button class="btn-chat" @click="contactEmployer">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                  <line x1="9" y1="10" x2="15" y2="10"/>
                </svg>
                联系企业
              </button>
              <button class="btn-apply" @click="handleApply">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 2L11 13"/>
                  <path d="M22 2L15 22L11 13L2 9L22 2Z"/>
                </svg>
                立即投递
              </button>
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
                  <path d="M22 2L11 13"/>
                  <path d="M22 2L15 22L11 13L2 9L22 2Z"/>
                </svg>
                {{ job.applyCount || 0 }}人投递
              </span>
              <span class="stat">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <polyline points="12 6 12 12 16 14"/>
                </svg>
                {{ job.createTime }}发布
              </span>
            </div>
          </div>

          <!-- 岗位描述 -->
          <div class="content-card">
            <h2 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
                <line x1="16" y1="13" x2="8" y2="13"/>
                <line x1="16" y1="17" x2="8" y2="17"/>
                <polyline points="10 9 9 9 8 9"/>
              </svg>
              岗位描述
            </h2>
            <div class="description-content">
              {{ job.description || '暂无描述' }}
            </div>
          </div>

          <!-- 工作信息 -->
          <div class="content-card">
            <h2 class="card-title">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              工作信息
            </h2>
            <div class="info-grid">
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                  <circle cx="12" cy="10" r="3"/>
                </svg>
                <div>
                  <span class="info-label">工作城市</span>
                  <span class="info-value">{{ job.city }}</span>
                </div>
              </div>
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                  <circle cx="12" cy="10" r="3"/>
                </svg>
                <div>
                  <span class="info-label">工作地址</span>
                  <span class="info-value">{{ job.address || '待定' }}</span>
                </div>
              </div>
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                  <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg>
                <div>
                  <span class="info-label">岗位分类</span>
                  <span class="info-value">{{ job.category }}</span>
                </div>
              </div>
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                  <circle cx="9" cy="7" r="4"/>
                </svg>
                <div>
                  <span class="info-label">招聘人数</span>
                  <span class="info-value">{{ job.headcount }}人</span>
                </div>
              </div>
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <polyline points="12 6 12 12 16 14"/>
                </svg>
                <div>
                  <span class="info-label">工作时间</span>
                  <span class="info-value">{{ job.workTime || '灵活安排' }}</span>
                </div>
              </div>
              <div class="info-item">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <line x1="12" y1="1" x2="12" y2="23"/>
                  <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
                </svg>
                <div>
                  <span class="info-label">结算方式</span>
                  <span class="info-value">{{ job.salaryType }}</span>
                </div>
              </div>
            </div>
          </div>
        </main>

        <!-- 右侧边栏 -->
        <aside class="sidebar">
          <!-- 企业信息卡片 -->
          <div class="company-card">
            <div class="company-header">
              <div class="company-avatar" :style="job.publisherAvatar ? { backgroundImage: `url(${job.publisherAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                {{ job.publisherAvatar ? '' : job.publisherName?.charAt(0) }}
              </div>
              <div class="company-info">
                <h3 class="company-name">{{ job.publisherName }}</h3>
                <span class="company-type">招聘方</span>
              </div>
            </div>
            <div class="company-stats">
              <div class="company-stat">
                <span class="stat-num">{{ job.viewCount || 0 }}</span>
                <span class="stat-label">浏览量</span>
              </div>
              <div class="company-stat">
                <span class="stat-num">{{ job.applyCount || 0 }}</span>
                <span class="stat-label">投递数</span>
              </div>
            </div>
          </div>

          <!-- 推荐岗位 -->
          <div class="recommend-card">
            <h3 class="recommend-title">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
              </svg>
              热门推荐
            </h3>
            <div class="recommend-list">
              <div
                v-for="item in hotJobs"
                :key="item.id"
                class="recommend-item"
                @click="$router.push(`/job/${item.id}`)"
              >
                <div class="recommend-info">
                  <h4>{{ item.title }}</h4>
                  <span class="recommend-salary">{{ item.salaryMin }}-{{ item.salaryMax }}元</span>
                </div>
                <span class="recommend-city">{{ item.city }}</span>
              </div>
            </div>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { getJobDetail, getHotJobs } from '../../api/job'
import { applyJob } from '../../api/application'
import { checkFavorite, addFavorite, removeFavorite } from '../../api/favorite'
import { getOrCreateSession } from '../../api/chat'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const job = ref(null)
const isFav = ref(false)
const hotJobs = ref([])

onMounted(async () => {
  const [jobRes, hotRes] = await Promise.all([
    getJobDetail(route.params.id),
    getHotJobs(5)
  ])
  job.value = jobRes.data
  hotJobs.value = hotRes.data

  if (store.isStudent) {
    try {
      const favRes = await checkFavorite(route.params.id)
      isFav.value = favRes.data
    } catch {}
  }
})

async function handleApply() {
  await ElMessageBox.confirm('确认投递该岗位？', '提示')
  await applyJob(route.params.id)
  ElMessage.success('投递成功')
}

async function contactEmployer() {
  if (!store.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    // 获取或创建与企业的会话
    const res = await getOrCreateSession(job.value.userId)
    // 跳转到聊天页面，带上会话ID
    router.push({ path: '/chat', query: { sessionId: res.data } })
  } catch {
    ElMessage.error('无法创建会话')
  }
}

async function toggleFav() {
  if (isFav.value) {
    await removeFavorite(route.params.id)
    isFav.value = false
    ElMessage.success('已取消收藏')
  } else {
    await addFavorite(route.params.id)
    isFav.value = true
    ElMessage.success('已收藏')
  }
}
</script>

<style scoped>
.job-detail-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 面包屑 */
.breadcrumb-bar {
  background: white;
  padding: 16px 0;
  border-bottom: 1px solid #E2E8F0;
}

.breadcrumb-bar .container {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.breadcrumb-link {
  color: #64748B;
  text-decoration: none;
}

.breadcrumb-link:hover {
  color: #FF6B35;
}

.breadcrumb-sep {
  color: #CBD5E1;
}

.breadcrumb-current {
  color: #1E293B;
  font-weight: 500;
}

/* 布局 */
.detail-layout {
  display: flex;
  gap: 24px;
  padding: 24px 0 40px;
}

.main-content {
  flex: 1;
  min-width: 0;
}

.sidebar {
  width: 320px;
  flex-shrink: 0;
}

/* 岗位头部卡片 */
.job-header-card {
  background: white;
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
  border: 1px solid #E2E8F0;
}

.job-header-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.job-title {
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 16px;
}

.job-tags {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.tag {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 13px;
}

.tag svg {
  color: #94A3B8;
}

.salary-section {
  text-align: right;
  flex-shrink: 0;
}

.salary-amount {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: #FF6B35;
}

.salary-unit {
  font-size: 14px;
  color: #64748B;
}

.job-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.btn-favorite {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 24px;
  border: 2px solid #E2E8F0;
  border-radius: 12px;
  background: white;
  color: #64748B;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-favorite:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-favorite.active {
  border-color: #FF6B35;
  color: #FF6B35;
  background: #FFF5F0;
}

.btn-apply {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-apply:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
}

.btn-chat {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 24px;
  border: 2px solid #3B82F6;
  border-radius: 12px;
  background: white;
  color: #3B82F6;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-chat:hover {
  background: #3B82F6;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(59, 130, 246, 0.3);
}

.job-stats {
  display: flex;
  gap: 24px;
  padding-top: 20px;
  border-top: 1px solid #F1F5F9;
}

.stat {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #94A3B8;
}

/* 内容卡片 */
.content-card {
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
  margin-bottom: 20px;
}

.description-content {
  font-size: 15px;
  line-height: 1.8;
  color: #64748B;
  white-space: pre-wrap;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.info-item svg {
  margin-top: 2px;
  flex-shrink: 0;
}

.info-label {
  display: block;
  font-size: 13px;
  color: #94A3B8;
  margin-bottom: 4px;
}

.info-value {
  font-size: 15px;
  color: #1E293B;
  font-weight: 500;
}

/* 企业卡片 */
.company-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  border: 1px solid #E2E8F0;
}

.company-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.company-avatar {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 600;
}

.company-name {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.company-type {
  font-size: 13px;
  color: #94A3B8;
}

.company-stats {
  display: flex;
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #F1F5F9;
}

.company-stat {
  flex: 1;
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #FF6B35;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #94A3B8;
}

/* 推荐卡片 */
.recommend-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
}

.recommend-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 16px;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommend-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #F8FAFC;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.recommend-item:hover {
  background: #FFF5F0;
}

.recommend-info h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.recommend-salary {
  font-size: 13px;
  color: #FF6B35;
  font-weight: 600;
}

.recommend-city {
  font-size: 12px;
  color: #94A3B8;
  padding: 4px 8px;
  background: white;
  border-radius: 6px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .detail-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .job-header-top {
    flex-direction: column;
    gap: 16px;
  }

  .salary-section {
    text-align: left;
  }

  .job-actions {
    flex-direction: column;
  }

  .btn-favorite,
  .btn-apply {
    width: 100%;
    justify-content: center;
  }
}
</style>

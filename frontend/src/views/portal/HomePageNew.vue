<template>
  <div class="home-page">
    <!-- Hero 区域 -->
    <section class="hero">
      <div class="container hero-content">
        <div class="hero-text">
          <div class="hero-badge">
            <span class="badge-dot"></span>
            为大学生量身打造
          </div>
          <h1 class="hero-title">
            发现你的
            <span class="title-highlight">理想兼职</span>
          </h1>
          <p class="hero-subtitle">
            连接优质企业与优秀学子，让每一份兼职都成为成长的阶梯
          </p>
          <div class="search-box">
            <div class="search-input-group">
              <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/>
                <path d="M21 21l-4.35-4.35"/>
              </svg>
              <input
                v-model="searchKeyword"
                type="text"
                placeholder="搜索岗位关键词..."
                class="search-input"
                @keyup.enter="handleSearch"
              >
            </div>
            <button class="btn-search" @click="handleSearch">
              搜索岗位
            </button>
          </div>
          <div class="hot-search-tags">
            <span class="hot-label">热搜：</span>
            <span
              v-for="word in hotSearchWords"
              :key="word"
              class="hot-search-word"
              @click="searchByKeyword(word)"
            >
              {{ word }}
            </span>
          </div>
        </div>
        <div class="hero-visual">
          <div class="visual-card">
            <div class="visual-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2Z"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
            </div>
            <span>快速匹配</span>
          </div>
          <div class="visual-card">
            <div class="visual-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2">
                <path d="M16 24L22 18L16 12"/>
                <path d="M2 18V6a2 2 0 0 1 2-2h16"/>
                <path d="M22 18V6a2 2 0 0 0-2-2H4"/>
              </svg>
            </div>
            <span>安全可靠</span>
          </div>
          <div class="visual-card">
            <div class="visual-icon">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#3B82F6" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
            </div>
            <span>即时上岗</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2Z"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
            </div>
            <div class="stat-number">{{ stats.jobCount }}+</div>
            <div class="stat-label">在招岗位</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
            </div>
            <div class="stat-number">{{ stats.employerCount }}+</div>
            <div class="stat-label">入驻企业</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                <circle cx="9" cy="7" r="4"/>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
              </svg>
            </div>
            <div class="stat-number">{{ stats.studentCount }}+</div>
            <div class="stat-label">注册学生</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                <polyline points="22 4 12 14.01 9 11.01"/>
              </svg>
            </div>
            <div class="stat-number">98%</div>
            <div class="stat-label">好评率</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 智能推荐岗位 -->
    <section class="recommend-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
              <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2z"/>
            </svg>
            为你推荐
          </h2>
          <p class="section-subtitle">根据你的浏览历史和偏好，智能推荐适合的岗位</p>
        </div>
        <div class="recommend-grid">
          <div
            v-for="job in recommendJobs"
            :key="job.id"
            class="job-card"
            @click="$router.push(`/job/${job.id}`)"
          >
            <div class="job-header">
              <h3 class="job-title">{{ job.title }}</h3>
              <span class="job-salary">{{ job.salaryMin }}-{{ job.salaryMax }}元/{{ job.salaryType }}</span>
            </div>
            <div class="job-tags">
              <span class="job-tag">{{ job.city }}</span>
              <span class="job-tag">{{ job.category }}</span>
              <span class="job-tag">{{ job.workTime }}</span>
            </div>
            <div class="job-footer">
              <div class="company-info">
                <div class="company-avatar" :style="job.publisherAvatar ? { backgroundImage: `url(${job.publisherAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">{{ job.publisherAvatar ? '' : job.publisherName?.charAt(0) }}</div>
                <span class="company-name">{{ job.publisherName }}</span>
              </div>
              <button class="btn-apply" @click.stop="$router.push(`/job/${job.id}`)">查看详情</button>
            </div>
          </div>
        </div>
        <div class="view-more">
          <button class="btn-view-more" @click="$router.push('/search')">
            查看更多岗位
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </button>
        </div>
      </div>
    </section>

    <!-- 岗位分类 -->
    <section id="categories" class="categories-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">热门岗位分类</h2>
          <p class="section-subtitle">选择你感兴趣的领域，开启兼职之旅</p>
        </div>
        <div class="categories-grid">
          <div
            v-for="cat in categories"
            :key="cat.name"
            class="category-card"
            @click="$router.push({ path: '/search', query: { category: cat.name } })"
          >
            <div class="category-icon" :style="{ background: cat.color }">
              <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                <path d="M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2Z"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
            </div>
            <h3 class="category-name">{{ cat.name }}</h3>
            <p class="category-count">{{ cat.count }}个岗位</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新岗位 -->
    <section id="jobs" class="jobs-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">最新招聘信息</h2>
          <p class="section-subtitle">优质岗位实时更新，抓住机会</p>
        </div>
        <div class="jobs-grid">
          <div
            v-for="job in latestJobs"
            :key="job.id"
            class="job-card"
            @click="$router.push(`/job/${job.id}`)"
          >
            <div class="job-header">
              <h3 class="job-title">{{ job.title }}</h3>
              <span class="job-salary">{{ job.salaryMin }}-{{ job.salaryMax }}元/{{ job.salaryType }}</span>
            </div>
            <div class="job-tags">
              <span class="job-tag">{{ job.city }}</span>
              <span class="job-tag">{{ job.category }}</span>
              <span class="job-tag">{{ job.workTime }}</span>
            </div>
            <div class="job-footer">
              <div class="company-info">
                <div class="company-avatar" :style="job.publisherAvatar ? { backgroundImage: `url(${job.publisherAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">{{ job.publisherAvatar ? '' : job.publisherName?.charAt(0) }}</div>
                <span class="company-name">{{ job.publisherName }}</span>
              </div>
              <div class="job-meta">
                <span class="view-count">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                  {{ job.viewCount || 0 }}
                </span>
                <button class="btn-apply" @click.stop="$router.push(`/job/${job.id}`)">查看详情</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 热门排行 -->
    <section class="hot-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
              <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
            </svg>
            热门岗位排行
          </h2>
          <p class="section-subtitle">大家都在投的热门兼职</p>
        </div>
        <div class="hot-list">
          <div
            v-for="(job, index) in hotJobs"
            :key="job.id"
            class="hot-item"
            @click="$router.push(`/job/${job.id}`)"
          >
            <div class="hot-rank" :class="{ 'top-3': index < 3 }">{{ index + 1 }}</div>
            <div class="hot-info">
              <h4 class="hot-title">{{ job.title }}</h4>
              <span class="hot-company">{{ job.publisherName }}</span>
            </div>
            <div class="hot-meta">
              <span class="hot-salary">{{ job.salaryMin }}-{{ job.salaryMax }}元</span>
              <span class="hot-views">{{ job.viewCount || 0 }}人看过</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 为什么选择我们 -->
    <section class="why-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">为什么选择 PartTimeGo</h2>
          <p class="section-subtitle">我们致力于为大学生提供最好的兼职体验</p>
        </div>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M16 24L22 18L16 12"/>
                <path d="M2 18V6a2 2 0 0 1 2-2h16"/>
                <path d="M22 18V6a2 2 0 0 0-2-2H4"/>
              </svg>
            </div>
            <h3>真实可靠</h3>
            <p>所有企业均经过实名认证，岗位信息真实有效</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#10B981" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
            </div>
            <h3>快速上岗</h3>
            <p>一键投递，企业快速响应，当天即可上岗</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#3B82F6" stroke-width="2">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2"/>
                <line x1="1" y1="10" x2="23" y2="10"/>
              </svg>
            </div>
            <h3>薪资保障</h3>
            <p>平台担保交易，工资按时发放，拒绝拖欠</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#8B5CF6" stroke-width="2">
                <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2z"/>
              </svg>
            </div>
            <h3>智能匹配</h3>
            <p>根据你的技能和偏好，推荐最适合的兼职</p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-content">
          <h2>准备好开始你的兼职之旅了吗？</h2>
          <p>立即注册，开启你的兼职生涯</p>
          <div class="cta-buttons">
            <button class="btn-primary" @click="$router.push('/register')">学生注册</button>
            <button class="btn-secondary" @click="$router.push('/register')">企业入驻</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeStats, getLatestJobs, getCategories, getHotJobs, getRecommendJobs, getHotSearchWords, getOnlineCount } from '../../api/portal'

const router = useRouter()
const searchKeyword = ref('')
const stats = ref({ jobCount: 0, employerCount: 0, studentCount: 0 })
const latestJobs = ref([])
const categories = ref([])
const hotJobs = ref([])
const recommendJobs = ref([])
const hotSearchWords = ref([])
const onlineCount = ref(0)

// 分类数据
const categoryData = {
  '餐饮': { color: '#FF6B35' },
  '家教': { color: '#10B981' },
  '促销': { color: '#3B82F6' },
  '服务员': { color: '#8B5CF6' },
  'IT技术': { color: '#F59E0B' },
  '物流配送': { color: '#6366F1' },
  '设计美工': { color: '#EC4899' },
  '行政文员': { color: '#6B7280' }
}

let onlineTimer = null

function handleSearch() {
  if (searchKeyword.value) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
  }
}

function searchByKeyword(keyword) {
  router.push({ path: '/search', query: { keyword } })
}

// 获取实时在线人数
async function fetchOnlineCount() {
  try {
    const res = await getOnlineCount()
    onlineCount.value = res.data || Math.floor(Math.random() * 100) + 50
  } catch {
    onlineCount.value = Math.floor(Math.random() * 100) + 50
  }
}

onMounted(async () => {
  const [statsRes, jobsRes, catsRes, hotRes, recommendRes, hotWordsRes] = await Promise.all([
    getHomeStats(),
    getLatestJobs(),
    getCategories(),
    getHotJobs(10),
    getRecommendJobs(),
    getHotSearchWords()
  ])

  stats.value = statsRes.data
  latestJobs.value = jobsRes.data
  hotJobs.value = hotRes.data
  recommendJobs.value = recommendRes.data || jobsRes.data.slice(0, 3)
  hotSearchWords.value = hotWordsRes.data || ['家教', '餐饮', '促销', '服务员', 'IT技术', '翻译']

  // 处理分类数据
  if (catsRes.data && catsRes.data.length > 0) {
    categories.value = catsRes.data.map(cat => ({
      ...cat,
      color: categoryData[cat.name]?.color || '#6B7280'
    }))
  } else {
    categories.value = [
      { name: '餐饮', count: 15, color: '#FF6B35' },
      { name: '家教', count: 12, color: '#10B981' },
      { name: '促销', count: 10, color: '#3B82F6' },
      { name: '服务员', count: 8, color: '#8B5CF6' },
      { name: 'IT技术', count: 6, color: '#F59E0B' },
      { name: '物流配送', count: 5, color: '#6366F1' },
      { name: '设计美工', count: 4, color: '#EC4899' },
      { name: '行政文员', count: 3, color: '#6B7280' }
    ]
  }

  // 获取实时在线人数
  fetchOnlineCount()
  onlineTimer = setInterval(fetchOnlineCount, 30000)
})

onUnmounted(() => {
  if (onlineTimer) {
    clearInterval(onlineTimer)
  }
})
</script>

<style scoped>
/* 基础样式 */
.home-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
  color: #1F2937;
  line-height: 1.6;
  background: #F9FAFB;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* Hero 区域 */
.hero {
  padding: 80px 0;
  background: #FFFFFF;
  border-bottom: 1px solid #E5E7EB;
}

.hero-content {
  display: flex;
  align-items: center;
  gap: 80px;
}

.hero-text {
  flex: 1;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: #FFF7ED;
  border: 1px solid #FFEDD5;
  border-radius: 100px;
  color: #EA580C;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 24px;
}

.badge-dot {
  width: 8px;
  height: 8px;
  background: #10B981;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 20px;
  color: #111827;
}

.title-highlight {
  color: #FF6B35;
}

.hero-subtitle {
  font-size: 18px;
  color: #6B7280;
  margin-bottom: 32px;
  max-width: 500px;
}

.search-box {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.search-input-group {
  flex: 1;
  display: flex;
  align-items: center;
  background: #F9FAFB;
  border: 2px solid #E5E7EB;
  border-radius: 12px;
  padding: 0 16px;
  transition: border-color 0.2s;
}

.search-input-group:focus-within {
  border-color: #FF6B35;
  background: #FFFFFF;
}

.search-icon {
  color: #9CA3AF;
  margin-right: 12px;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 16px 0;
  font-size: 16px;
  background: transparent;
  color: #1F2937;
}

.search-input::placeholder {
  color: #9CA3AF;
}

.btn-search {
  padding: 16px 32px;
  border: none;
  border-radius: 12px;
  background: #FF6B35;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-search:hover {
  background: #EA580C;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.hot-search-tags {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.hot-label {
  font-size: 14px;
  color: #6B7280;
}

.hot-search-word {
  padding: 6px 14px;
  background: #F3F4F6;
  border: 1px solid #E5E7EB;
  border-radius: 100px;
  color: #374151;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.hot-search-word:hover {
  background: #FF6B35;
  color: white;
  border-color: #FF6B35;
}

.hero-visual {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.visual-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 12px;
  transition: all 0.2s;
}

.visual-card:hover {
  border-color: #D1D5DB;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateX(-4px);
}

.visual-icon {
  width: 48px;
  height: 48px;
  background: #F9FAFB;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.visual-card span {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
}

/* 数据统计 */
.stats-section {
  padding: 60px 0;
  background: #FFFFFF;
  border-bottom: 1px solid #E5E7EB;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-card {
  text-align: center;
  padding: 32px 24px;
  background: #F9FAFB;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  transition: all 0.2s;
}

.stat-card:hover {
  border-color: #D1D5DB;
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 48px;
  height: 48px;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.stat-number {
  font-size: 36px;
  font-weight: 800;
  color: #111827;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6B7280;
}

/* 通用区块样式 */
.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.section-subtitle {
  font-size: 16px;
  color: #6B7280;
}

/* 推荐区域 */
.recommend-section {
  padding: 80px 0;
  background: #FFFFFF;
}

.recommend-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.job-card {
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.job-card:hover {
  border-color: #D1D5DB;
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
}

.view-more {
  text-align: center;
  margin-top: 48px;
}

.btn-view-more {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  background: #FFFFFF;
  border: 2px solid #FF6B35;
  border-radius: 12px;
  color: #FF6B35;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-view-more:hover {
  background: #FF6B35;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

/* 分类区域 */
.categories-section {
  padding: 80px 0;
  background: #F9FAFB;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.category-card {
  padding: 28px 20px;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}

.category-card:hover {
  border-color: #D1D5DB;
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.category-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.category-name {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 4px;
}

.category-count {
  font-size: 14px;
  color: #9CA3AF;
}

/* 岗位区域 */
.jobs-section {
  padding: 80px 0;
  background: #FFFFFF;
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.job-title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  flex: 1;
  margin-right: 12px;
}

.job-salary {
  font-size: 18px;
  font-weight: 700;
  color: #FF6B35;
  white-space: nowrap;
}

.job-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.job-tag {
  padding: 6px 12px;
  background: #F3F4F6;
  color: #6B7280;
  border-radius: 8px;
  font-size: 13px;
}

.job-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #F3F4F6;
}

.company-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.company-avatar {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: #F3F4F6;
  color: #6B7280;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.company-name {
  font-size: 14px;
  color: #6B7280;
}

.job-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.view-count {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #9CA3AF;
}

.btn-apply {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  background: #FF6B35;
  color: white;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-apply:hover {
  background: #EA580C;
  transform: translateY(-2px);
}

/* 热门排行 */
.hot-section {
  padding: 80px 0;
  background: #F9FAFB;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hot-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 24px;
  background: #FFFFFF;
  border: 1px solid #E5E7EB;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.hot-item:hover {
  border-color: #D1D5DB;
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.hot-rank {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: #F3F4F6;
  color: #6B7280;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 700;
}

.hot-rank.top-3 {
  background: #FF6B35;
  color: white;
}

.hot-info {
  flex: 1;
}

.hot-title {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 4px;
}

.hot-company {
  font-size: 14px;
  color: #9CA3AF;
}

.hot-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.hot-salary {
  font-size: 16px;
  font-weight: 700;
  color: #FF6B35;
}

.hot-views {
  font-size: 13px;
  color: #9CA3AF;
}

/* 为什么选择我们 */
.why-section {
  padding: 80px 0;
  background: #FFFFFF;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.feature-card {
  padding: 32px 24px;
  background: #F9FAFB;
  border: 1px solid #E5E7EB;
  border-radius: 16px;
  text-align: center;
  transition: all 0.2s;
}

.feature-card:hover {
  border-color: #D1D5DB;
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.feature-icon {
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 12px;
}

.feature-card p {
  font-size: 14px;
  color: #6B7280;
  line-height: 1.6;
}

/* CTA 区域 */
.cta-section {
  padding: 80px 0;
  background: #111827;
}

.cta-content {
  text-align: center;
  color: white;
}

.cta-content h2 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 16px;
}

.cta-content p {
  font-size: 18px;
  color: #D1D5DB;
  margin-bottom: 32px;
}

.cta-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.btn-primary {
  padding: 16px 32px;
  border: 2px solid white;
  border-radius: 12px;
  background: transparent;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary:hover {
  background: white;
  color: #111827;
  transform: translateY(-2px);
}

.btn-secondary {
  padding: 16px 32px;
  border: none;
  border-radius: 12px;
  background: #FF6B35;
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-secondary:hover {
  background: #EA580C;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
  }

  .hero-subtitle {
    max-width: 100%;
  }

  .search-box {
    flex-direction: column;
  }

  .hot-search-tags {
    justify-content: center;
  }

  .hero-visual {
    flex-direction: row;
    justify-content: center;
  }

  .categories-grid,
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .jobs-grid,
  .recommend-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 36px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .categories-grid,
  .jobs-grid,
  .recommend-grid,
  .features-grid {
    grid-template-columns: 1fr;
  }

  .cta-buttons {
    flex-direction: column;
  }
}
</style>

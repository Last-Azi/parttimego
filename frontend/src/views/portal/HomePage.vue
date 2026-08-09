<template>
  <div class="home-page">
    <!-- Hero 区域 -->
    <section class="hero">
      <div class="hero-bg">
        <div class="hero-shape shape-1"></div>
        <div class="hero-shape shape-2"></div>
        <div class="hero-shape shape-3"></div>
        <div class="hero-dots"></div>
      </div>
      <div class="hero-content">
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

        <!-- 搜索框 -->
        <div class="search-container">
          <div class="search-box">
            <div class="search-field">
              <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/>
                <path d="m21 21-4.3-4.3"/>
              </svg>
              <input
                v-model="searchKeyword"
                type="text"
                placeholder="搜索岗位关键词..."
                class="search-input"
                @keyup.enter="handleSearch"
              />
            </div>
            <div class="search-divider"></div>
            <div class="search-field city-field">
              <svg class="search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              <select v-model="searchCity" class="search-select">
                <option value="">选择城市</option>
                <option v-for="city in cities" :key="city" :value="city">{{ city }}</option>
              </select>
            </div>
            <button class="search-btn" @click="handleSearch">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/>
                <path d="m21 21-4.3-4.3"/>
              </svg>
              搜索岗位
            </button>
          </div>
          <div class="search-tags">
            <span class="tag-label">热门：</span>
            <button v-for="tag in hotTags" :key="tag" class="tag-item" @click="searchByKeyword(tag)">
              {{ tag }}
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- 数据统计 -->
    <section class="stats-section">
      <div class="stats-container">
        <div class="stat-card" v-for="(stat, index) in statsData" :key="index">
          <div class="stat-icon" :style="{ background: stat.gradient }">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
              <path :d="stat.icon"/>
            </svg>
          </div>
          <div class="stat-info">
            <div class="stat-number">
              <span class="stat-count">{{ stat.value }}</span>
              <span class="stat-suffix">+</span>
            </div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 岗位分类 -->
    <section class="categories-section">
      <div class="section-header">
        <div class="section-tag">探索机会</div>
        <h2 class="section-title">热门岗位分类</h2>
        <p class="section-desc">发现适合你的兼职类型，开启赚钱之旅</p>
      </div>
      <div class="categories-grid">
        <div
          v-for="cat in displayCategories"
          :key="cat.name"
          class="category-card"
          @click="searchByCategory(cat.name)"
        >
          <div class="category-icon" :style="{ background: cat.gradient }">
            <span class="category-emoji">{{ cat.emoji }}</span>
          </div>
          <h3 class="category-name">{{ cat.name }}</h3>
          <span class="category-count">{{ cat.count || 0 }} 个岗位</span>
        </div>
      </div>
    </section>

    <!-- 最新岗位 -->
    <section class="jobs-section">
      <div class="section-header">
        <div class="section-tag">最新机会</div>
        <h2 class="section-title">最新招聘信息</h2>
        <p class="section-desc">优质岗位实时更新，抓住每一个机会</p>
      </div>
      <div class="jobs-grid">
        <div
          v-for="job in latestJobs"
          :key="job.id"
          class="job-card"
          @click="$router.push(`/job/${job.id}`)"
        >
          <div class="job-header">
            <div class="job-company">
              <div class="company-logo">{{ getInitial(job.publisherName) }}</div>
              <div class="company-info">
                <span class="company-name">{{ job.publisherName }}</span>
                <span class="company-city">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="12" height="12">
                    <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
                    <circle cx="12" cy="10" r="3"/>
                  </svg>
                  {{ job.city }}
                </span>
              </div>
            </div>
            <div class="job-salary">
              {{ job.salaryMin }}-{{ job.salaryMax }}<span class="salary-unit">元/{{ job.salaryType }}</span>
            </div>
          </div>
          <h3 class="job-title">{{ job.title }}</h3>
          <div class="job-tags">
            <span class="job-tag">{{ job.category }}</span>
            <span class="job-tag">{{ job.salaryType }}</span>
            <span class="job-tag" v-if="job.headcount">招{{ job.headcount }}人</span>
          </div>
          <div class="job-footer">
            <span class="job-time">{{ formatTime(job.createTime) }}</span>
            <button class="job-apply-btn">查看详情</button>
          </div>
        </div>
      </div>
      <div class="view-all">
        <button class="view-all-btn" @click="$router.push('/search')">
          查看全部岗位
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18">
            <path d="M5 12h14M12 5l7 7-7 7"/>
          </svg>
        </button>
      </div>
    </section>

    <!-- 热门排行榜 -->
    <section class="hot-section" v-if="hotJobs.length > 0">
      <div class="section-header">
        <div class="section-tag">🔥 热门推荐</div>
        <h2 class="section-title">热门岗位排行</h2>
        <p class="section-desc">大家都在关注的热门兼职</p>
      </div>
      <div class="hot-list">
        <div
          v-for="(job, index) in hotJobs"
          :key="job.id"
          class="hot-item"
          @click="$router.push(`/job/${job.id}`)"
        >
          <div class="hot-rank" :class="{ 'rank-top': index < 3 }">
            {{ index + 1 }}
          </div>
          <div class="hot-info">
            <h4 class="hot-title">{{ job.title }}</h4>
            <span class="hot-company">{{ job.publisherName }}</span>
          </div>
          <div class="hot-meta">
            <span class="hot-salary">{{ job.salaryMin }}-{{ job.salaryMax }}元</span>
            <span class="hot-views">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14">
                <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              {{ job.viewCount || 0 }}
            </span>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section class="cta-section">
      <div class="cta-content">
        <h2 class="cta-title">准备好开始你的兼职之旅了吗？</h2>
        <p class="cta-desc">立即注册，发现更多优质兼职机会</p>
        <div class="cta-buttons">
          <button class="cta-btn cta-btn-primary" @click="$router.push('/register')">
            免费注册
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18">
              <path d="M5 12h14M12 5l7 7-7 7"/>
            </svg>
          </button>
          <button class="cta-btn cta-btn-secondary" @click="$router.push('/search')">
            浏览岗位
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeStats, getLatestJobs, getCategories, getHotJobs } from '../../api/portal'

const router = useRouter()
const searchKeyword = ref('')
const searchCity = ref('')
const stats = ref({ jobCount: 0, employerCount: 0, studentCount: 0 })
const latestJobs = ref([])
const categories = ref([])
const hotJobs = ref([])

const cities = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京', '重庆', '西安', '天津', '苏州', '长沙', '郑州', '青岛']

const hotTags = ['家教', '餐饮', '促销', 'IT技术', '设计', '翻译']

const defaultCategories = [
  { name: '餐饮', emoji: '🍔', gradient: 'linear-gradient(135deg, #FF6B6B, #FF8E8E)' },
  { name: '家教', emoji: '📚', gradient: 'linear-gradient(135deg, #4ECDC4, #6EE7DE)' },
  { name: '促销', emoji: '🎯', gradient: 'linear-gradient(135deg, #FFD93D, #FFE66D)' },
  { name: '服务员', emoji: '🍽️', gradient: 'linear-gradient(135deg, #6BCB77, #8EE09A)' },
  { name: '传单派发', emoji: '📋', gradient: 'linear-gradient(135deg, #FF9F45, #FFB76B)' },
  { name: '物流配送', emoji: '🚚', gradient: 'linear-gradient(135deg, #4D96FF, #6DAEFF)' },
  { name: '超市零售', emoji: '🛒', gradient: 'linear-gradient(135deg, #9B59B6, #B07CC6)' },
  { name: '活动执行', emoji: '🎪', gradient: 'linear-gradient(135deg, #E74C3C, #F07167)' },
  { name: '文案编辑', emoji: '✍️', gradient: 'linear-gradient(135deg, #1ABC9C, #3ED8B8)' },
  { name: '设计美工', emoji: '🎨', gradient: 'linear-gradient(135deg, #F39C12, #F7C15C)' },
  { name: 'IT技术', emoji: '💻', gradient: 'linear-gradient(135deg, #3498DB, #5DADE2)' },
  { name: '客服', emoji: '📞', gradient: 'linear-gradient(135deg, #E91E63, #F06292)' },
  { name: '行政文员', emoji: '📁', gradient: 'linear-gradient(135deg, #607D8B, #90A4AE)' },
  { name: '翻译', emoji: '🌐', gradient: 'linear-gradient(135deg, #00BCD4, #4DD0E1)' },
  { name: '摄影摄像', emoji: '📸', gradient: 'linear-gradient(135deg, #795548, #A1887F)' },
]

const displayCategories = computed(() => {
  if (categories.value.length > 0) {
    return categories.value.map((cat, index) => ({
      ...cat,
      ...defaultCategories[index % defaultCategories.length]
    }))
  }
  return defaultCategories
})

const statsData = computed(() => [
  {
    value: stats.value.jobCount,
    label: '在招岗位',
    gradient: 'linear-gradient(135deg, #FF6B35, #FF8E53)',
    icon: 'M20 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2Z M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16'
  },
  {
    value: stats.value.employerCount,
    label: '入驻企业',
    gradient: 'linear-gradient(135deg, #4ECDC4, #6EE7DE)',
    icon: 'M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z M9 22V12h6v10'
  },
  {
    value: stats.value.studentCount,
    label: '注册学生',
    gradient: 'linear-gradient(135deg, #FFD93D, #FFE66D)',
    icon: 'M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2 M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8z M23 21v-2a4 4 0 0 0-3-3.87 M16 3.13a4 4 0 0 1 0 7.75'
  }
])

function handleSearch() {
  const query = {}
  if (searchKeyword.value) query.keyword = searchKeyword.value
  if (searchCity.value) query.city = searchCity.value
  router.push({ path: '/search', query })
}

function searchByKeyword(keyword) {
  router.push({ path: '/search', query: { keyword } })
}

function searchByCategory(category) {
  router.push({ path: '/search', query: { category } })
}

function getInitial(name) {
  return name ? name.charAt(0) : '?'
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

onMounted(async () => {
  const [statsRes, jobsRes, catsRes, hotRes] = await Promise.all([
    getHomeStats(),
    getLatestJobs(),
    getCategories(),
    getHotJobs(10)
  ])
  stats.value = statsRes.data
  latestJobs.value = jobsRes.data
  categories.value = catsRes.data
  hotJobs.value = hotRes.data
})
</script>

<style scoped>
/* ========== 基础变量 ========== */
:root {
  --primary: #FF6B35;
  --primary-light: #FF8E53;
  --primary-dark: #E55A2B;
  --accent: #4ECDC4;
  --accent-light: #6EE7DE;
  --text-primary: #1A1A2E;
  --text-secondary: #6B7280;
  --text-muted: #9CA3AF;
  --bg-primary: #FFFFFF;
  --bg-secondary: #F8FAFC;
  --bg-tertiary: #F1F5F9;
  --border: #E2E8F0;
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;
}

/* ========== 页面基础 ========== */
.home-page {
  min-height: 100vh;
  background: var(--bg-secondary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* ========== Hero 区域 ========== */
.hero {
  position: relative;
  padding: 100px 24px 80px;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8E53 50%, #FFB088 100%);
  overflow: hidden;
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-bg {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.hero-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
}

.shape-1 {
  width: 400px;
  height: 400px;
  background: white;
  top: -100px;
  right: -100px;
  animation: float 6s ease-in-out infinite;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: white;
  bottom: -80px;
  left: -80px;
  animation: float 8s ease-in-out infinite reverse;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: white;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: float 7s ease-in-out infinite;
}

.hero-dots {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle, rgba(255,255,255,0.2) 1px, transparent 1px);
  background-size: 30px 30px;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 800px;
  width: 100%;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 100px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.badge-dot {
  width: 8px;
  height: 8px;
  background: #4ECDC4;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  color: white;
  margin: 0 0 16px;
  line-height: 1.1;
  letter-spacing: -0.02em;
}

.title-highlight {
  display: inline-block;
  background: linear-gradient(135deg, #FFD93D, #FFE66D);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 40px;
  line-height: 1.6;
}

/* ========== 搜索框 ========== */
.search-container {
  max-width: 700px;
  margin: 0 auto;
}

.search-box {
  display: flex;
  align-items: center;
  background: white;
  border-radius: var(--radius-lg);
  padding: 6px;
  box-shadow: var(--shadow-xl);
  transition: box-shadow 0.3s;
}

.search-box:focus-within {
  box-shadow: 0 20px 40px rgba(255, 107, 53, 0.2);
}

.search-field {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 12px 16px;
  gap: 12px;
}

.search-icon {
  width: 20px;
  height: 20px;
  color: var(--text-muted);
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 16px;
  color: var(--text-primary);
  background: transparent;
}

.search-input::placeholder {
  color: var(--text-muted);
}

.search-select {
  flex: 1;
  border: none;
  outline: none;
  font-size: 16px;
  color: var(--text-primary);
  background: transparent;
  cursor: pointer;
  appearance: none;
}

.search-divider {
  width: 1px;
  height: 32px;
  background: var(--border);
  flex-shrink: 0;
}

.search-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.search-btn:hover {
  background: linear-gradient(135deg, #E55A2B, #FF6B35);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.search-btn svg {
  width: 18px;
  height: 18px;
}

.search-tags {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.tag-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
}

.tag-item {
  padding: 6px 16px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 100px;
  color: white;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
  backdrop-filter: blur(10px);
}

.tag-item:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* ========== 数据统计 ========== */
.stats-section {
  padding: 0 24px;
  margin-top: -40px;
  position: relative;
  z-index: 10;
}

.stats-container {
  max-width: 1000px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-xl);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon svg {
  width: 28px;
  height: 28px;
}

.stat-number {
  display: flex;
  align-items: baseline;
  gap: 2px;
}

.stat-count {
  font-size: 32px;
  font-weight: 800;
  color: var(--text-primary);
  line-height: 1;
}

.stat-suffix {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-muted);
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
  margin-top: 4px;
}

/* ========== 通用区块样式 ========== */
.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.section-tag {
  display: inline-block;
  padding: 6px 16px;
  background: linear-gradient(135deg, rgba(255, 107, 53, 0.1), rgba(255, 142, 83, 0.1));
  color: var(--primary);
  border-radius: 100px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px;
}

.section-title {
  font-size: 36px;
  font-weight: 800;
  color: var(--text-primary);
  margin: 0 0 12px;
  letter-spacing: -0.02em;
}

.section-desc {
  font-size: 16px;
  color: var(--text-secondary);
  margin: 0;
}

/* ========== 岗位分类 ========== */
.categories-section {
  padding: 80px 24px;
  background: var(--bg-primary);
}

.categories-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
}

.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.category-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary);
  background: white;
}

.category-icon {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s;
}

.category-card:hover .category-icon {
  transform: scale(1.1) rotate(5deg);
}

.category-emoji {
  font-size: 28px;
}

.category-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.category-count {
  font-size: 13px;
  color: var(--text-muted);
}

/* ========== 最新岗位 ========== */
.jobs-section {
  padding: 80px 24px;
  background: var(--bg-secondary);
}

.jobs-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}

.job-card {
  background: white;
  border-radius: var(--radius-lg);
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid var(--border);
}

.job-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.job-company {
  display: flex;
  align-items: center;
  gap: 12px;
}

.company-logo {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, var(--primary), var(--primary-light));
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-weight: 700;
  flex-shrink: 0;
}

.company-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.company-city {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-muted);
  margin-top: 2px;
}

.job-salary {
  font-size: 20px;
  font-weight: 800;
  color: var(--primary);
  white-space: nowrap;
}

.salary-unit {
  font-size: 13px;
  font-weight: 500;
  color: var(--text-muted);
}

.job-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 12px;
  line-height: 1.4;
}

.job-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.job-tag {
  padding: 4px 12px;
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  border-radius: 100px;
  font-size: 12px;
  font-weight: 500;
}

.job-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}

.job-time {
  font-size: 13px;
  color: var(--text-muted);
}

.job-apply-btn {
  padding: 8px 20px;
  background: linear-gradient(135deg, var(--primary), var(--primary-light));
  color: white;
  border: none;
  border-radius: var(--radius-sm);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.job-apply-btn:hover {
  background: linear-gradient(135deg, var(--primary-dark), var(--primary));
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.view-all {
  text-align: center;
  margin-top: 40px;
}

.view-all-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  background: white;
  color: var(--primary);
  border: 2px solid var(--primary);
  border-radius: var(--radius-md);
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.view-all-btn:hover {
  background: var(--primary);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.view-all-btn:hover svg {
  stroke: white;
}

/* ========== 热门排行榜 ========== */
.hot-section {
  padding: 80px 24px;
  background: var(--bg-primary);
}

.hot-list {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hot-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px 24px;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}

.hot-item:hover {
  background: white;
  border-color: var(--primary);
  transform: translateX(8px);
  box-shadow: var(--shadow-md);
}

.hot-rank {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 800;
  flex-shrink: 0;
  transition: all 0.2s;
}

.rank-top {
  background: linear-gradient(135deg, var(--primary), var(--primary-light));
  color: white;
}

.hot-info {
  flex: 1;
  min-width: 0;
}

.hot-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hot-company {
  font-size: 13px;
  color: var(--text-muted);
}

.hot-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
  flex-shrink: 0;
}

.hot-salary {
  font-size: 16px;
  font-weight: 700;
  color: var(--primary);
}

.hot-views {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-muted);
}

/* ========== CTA 区域 ========== */
.cta-section {
  padding: 100px 24px;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8E53 50%, #FFB088 100%);
  position: relative;
  overflow: hidden;
}

.cta-section::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image: radial-gradient(circle, rgba(255,255,255,0.1) 1px, transparent 1px);
  background-size: 24px 24px;
}

.cta-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 600px;
  margin: 0 auto;
}

.cta-title {
  font-size: 36px;
  font-weight: 800;
  color: white;
  margin: 0 0 16px;
  line-height: 1.2;
}

.cta-desc {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0 0 32px;
}

.cta-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.cta-btn {
  padding: 16px 32px;
  border-radius: var(--radius-md);
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.cta-btn-primary {
  background: white;
  color: var(--primary);
  border: none;
}

.cta-btn-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.cta-btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid white;
}

.cta-btn-secondary:hover {
  background: white;
  color: var(--primary);
  transform: translateY(-3px);
}

.cta-btn svg {
  width: 18px;
  height: 18px;
}

/* ========== 响应式设计 ========== */
@media (max-width: 768px) {
  .hero {
    padding: 80px 20px 60px;
    min-height: auto;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .search-box {
    flex-direction: column;
    padding: 12px;
  }

  .search-field {
    width: 100%;
  }

  .search-divider {
    width: 100%;
    height: 1px;
  }

  .search-btn {
    width: 100%;
    justify-content: center;
  }

  .stats-container {
    grid-template-columns: 1fr;
  }

  .stat-card {
    justify-content: center;
  }

  .categories-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }

  .jobs-grid {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 28px;
  }

  .cta-title {
    font-size: 28px;
  }

  .cta-buttons {
    flex-direction: column;
  }

  .cta-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 28px;
  }

  .categories-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
  }

  .category-card {
    padding: 16px 8px;
  }

  .category-icon {
    width: 48px;
    height: 48px;
  }

  .category-emoji {
    font-size: 22px;
  }

  .category-name {
    font-size: 12px;
  }
}
</style>

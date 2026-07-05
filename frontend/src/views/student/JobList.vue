<template>
  <div class="search-page">
    <!-- 搜索头部 -->
    <div class="search-header">
      <div class="container">
        <!-- 搜索框 -->
        <div class="search-bar">
          <div class="search-input-wrapper">
            <svg class="search-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"/>
              <path d="M21 21l-4.35-4.35"/>
            </svg>
            <input
              v-model="query.keyword"
              type="text"
              placeholder="搜索岗位名称、公司、关键词..."
              class="search-input"
              @keyup.enter="handleSearch"
            >
            <button v-if="query.keyword" class="clear-btn" @click="query.keyword = ''; handleSearch()">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>
          <button class="btn-search" @click="handleSearch">搜索</button>
        </div>

        <!-- 热门搜索 -->
        <div class="hot-keywords">
          <span class="hot-label">热门搜索：</span>
          <span
            v-for="word in hotKeywords"
            :key="word"
            class="hot-word"
            @click="query.keyword = word; handleSearch()"
          >
            {{ word }}
          </span>
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="container main-content">
      <div class="content-layout">
        <!-- 左侧筛选栏 -->
        <aside class="filter-sidebar">
          <div class="filter-section">
            <h3 class="filter-title">工作城市</h3>
            <div class="filter-tags">
              <span
                class="filter-tag"
                :class="{ active: !query.city }"
                @click="query.city = ''; handleSearch()"
              >全国</span>
              <span
                v-for="city in hotCities"
                :key="city"
                class="filter-tag"
                :class="{ active: query.city === city }"
                @click="query.city = city; handleSearch()"
              >{{ city }}</span>
            </div>
          </div>

          <div class="filter-section">
            <h3 class="filter-title">岗位分类</h3>
            <div class="filter-tags">
              <span
                class="filter-tag"
                :class="{ active: !query.category }"
                @click="query.category = ''; handleSearch()"
              >全部</span>
              <span
                v-for="cat in categories"
                :key="cat"
                class="filter-tag"
                :class="{ active: query.category === cat }"
                @click="query.category = cat; handleSearch()"
              >{{ cat }}</span>
            </div>
          </div>

          <div class="filter-section">
            <h3 class="filter-title">薪资结算</h3>
            <div class="filter-tags">
              <span
                class="filter-tag"
                :class="{ active: !query.salaryType }"
                @click="query.salaryType = ''; handleSearch()"
              >不限</span>
              <span
                v-for="type in salaryTypes"
                :key="type"
                class="filter-tag"
                :class="{ active: query.salaryType === type }"
                @click="query.salaryType = type; handleSearch()"
              >{{ type }}</span>
            </div>
          </div>

          <div class="filter-section">
            <h3 class="filter-title">薪资范围</h3>
            <div class="salary-range">
              <input
                v-model.number="query.salaryMin"
                type="number"
                placeholder="最低"
                class="salary-input"
                @change="handleSearch"
              >
              <span class="range-separator">-</span>
              <input
                v-model.number="query.salaryMax"
                type="number"
                placeholder="最高"
                class="salary-input"
                @change="handleSearch"
              >
              <span class="range-unit">元</span>
            </div>
          </div>

          <button class="btn-reset" @click="resetFilters">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="1 4 1 10 7 10"/>
              <path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"/>
            </svg>
            重置筛选
          </button>
        </aside>

        <!-- 右侧结果区 -->
        <main class="results-main">
          <!-- 排序和统计 -->
          <div class="results-toolbar">
            <div class="results-count">
              共找到 <strong>{{ total }}</strong> 个岗位
            </div>
            <div class="sort-options">
              <button
                v-for="sort in sortOptions"
                :key="sort.value"
                class="sort-btn"
                :class="{ active: currentSort === sort.value }"
                @click="currentSort = sort.value; handleSearch()"
              >
                {{ sort.label }}
              </button>
            </div>
          </div>

          <!-- 岗位列表 -->
          <div class="job-list">
            <div
              v-for="job in jobs"
              :key="job.id"
              class="job-card"
              @click="$router.push(`/job/${job.id}`)"
            >
              <div class="job-main">
                <div class="job-header">
                  <h3 class="job-title">{{ job.title }}</h3>
                  <span class="job-salary">{{ job.salaryMin }}-{{ job.salaryMax }}元/{{ job.salaryType }}</span>
                </div>
                <div class="job-tags">
                  <span class="tag">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                      <circle cx="12" cy="10" r="3"/>
                    </svg>
                    {{ job.city }}
                  </span>
                  <span class="tag">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                      <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                    </svg>
                    {{ job.category }}
                  </span>
                  <span class="tag">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="10"/>
                      <polyline points="12 6 12 12 16 14"/>
                    </svg>
                    {{ job.workTime }}
                  </span>
                </div>
                <p v-if="job.description" class="job-desc">{{ job.description }}</p>
              </div>
              <div class="job-company">
                <div class="company-avatar" :style="job.publisherAvatar ? { backgroundImage: `url(${job.publisherAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">{{ job.publisherAvatar ? '' : job.publisherName?.charAt(0) }}</div>
                <div class="company-info">
                  <span class="company-name">{{ job.publisherName }}</span>
                  <span class="company-meta">
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                      <circle cx="12" cy="12" r="3"/>
                    </svg>
                    {{ job.viewCount || 0 }}人看过
                  </span>
                </div>
                <button class="btn-apply" @click.stop="$router.push(`/job/${job.id}`)">
                  立即投递
                </button>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div v-if="jobs.length === 0" class="empty-state">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
              <circle cx="11" cy="11" r="8"/>
              <path d="M21 21l-4.35-4.35"/>
              <path d="M8 11h6"/>
            </svg>
            <h3>暂无符合条件的岗位</h3>
            <p>试试调整筛选条件或搜索其他关键词</p>
            <button class="btn-reset-main" @click="resetFilters">重置筛选条件</button>
          </div>

          <!-- 分页 -->
          <div v-if="total > 0" class="pagination">
            <button
              class="page-btn"
              :disabled="query.pageNum === 1"
              @click="query.pageNum--; fetchData()"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="15 18 9 12 15 6"/>
              </svg>
            </button>
            <button
              v-for="page in displayedPages"
              :key="page"
              class="page-btn"
              :class="{ active: query.pageNum === page }"
              @click="query.pageNum = page; fetchData()"
            >
              {{ page }}
            </button>
            <button
              class="page-btn"
              :disabled="query.pageNum === totalPages"
              @click="query.pageNum++; fetchData()"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polyline points="9 18 15 12 9 6"/>
              </svg>
            </button>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchJobs } from '../../api/job'

const route = useRoute()
const router = useRouter()

const query = ref({
  keyword: route.query.keyword || '',
  city: route.query.city || '',
  category: route.query.category || '',
  salaryType: '',
  salaryMin: null,
  salaryMax: null,
  sort: 'default',
  pageNum: 1,
  pageSize: 10
})

const jobs = ref([])
const total = ref(0)
const currentSort = ref('default')

const hotKeywords = ['家教', '餐饮', '促销', '服务员', 'IT技术', '翻译', '摄影']
const hotCities = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '南京']
const categories = ['餐饮', '家教', '促销', '服务员', '物流配送', '超市零售', '活动执行', '文案编辑', '设计美工', 'IT技术', '客服', '行政文员', '翻译', '摄影摄像']
const salaryTypes = ['日结', '周结', '月结', '完工结']

const sortOptions = [
  { label: '默认排序', value: 'default' },
  { label: '最新发布', value: 'newest' },
  { label: '薪资最高', value: 'salary' },
  { label: '浏览最多', value: 'views' }
]

const totalPages = computed(() => Math.ceil(total.value / query.value.pageSize))
const displayedPages = computed(() => {
  const pages = []
  const start = Math.max(1, query.value.pageNum - 2)
  const end = Math.min(totalPages.value, start + 4)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 监听路由参数变化
watch(() => route.query, (newQuery) => {
  if (newQuery.keyword !== undefined) query.value.keyword = newQuery.keyword
  if (newQuery.city !== undefined) query.value.city = newQuery.city
  if (newQuery.category !== undefined) query.value.category = newQuery.category
  fetchData()
}, { deep: true })

function handleSearch() {
  query.value.pageNum = 1
  // 更新 URL 参数
  const queryParams = {}
  if (query.value.keyword) queryParams.keyword = query.value.keyword
  if (query.value.city) queryParams.city = query.value.city
  if (query.value.category) queryParams.category = query.value.category
  router.replace({ path: '/search', query: queryParams })
  fetchData()
}

function resetFilters() {
  query.value = {
    keyword: '',
    city: '',
    category: '',
    salaryType: '',
    salaryMin: null,
    salaryMax: null,
    sort: 'default',
    pageNum: 1,
    pageSize: 10
  }
  currentSort.value = 'default'
  router.replace({ path: '/search' })
  fetchData()
}

async function fetchData() {
  const params = { ...query.value }
  params.sort = currentSort.value
  // 移除空值
  Object.keys(params).forEach(key => {
    if (params[key] === '' || params[key] === null || params[key] === undefined) {
      delete params[key]
    }
  })
  const res = await searchJobs(params)
  jobs.value = res.data.records
  total.value = res.data.total
}

onMounted(fetchData)
</script>

<style scoped>
.search-page {
  min-height: 100vh;
  background: #F8FAFC;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 搜索头部 */
.search-header {
  background: linear-gradient(135deg, #FFF5F0 0%, #FFE8D6 100%);
  padding: 32px 0;
  border-bottom: 1px solid #E2E8F0;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.search-input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 0 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 2px solid transparent;
  transition: all 0.2s;
}

.search-input-wrapper:focus-within {
  border-color: #FF6B35;
  box-shadow: 0 4px 20px rgba(255, 107, 53, 0.15);
}

.search-icon {
  color: #94A3B8;
  margin-right: 12px;
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 16px 0;
  font-size: 16px;
  background: transparent;
}

.search-input::placeholder {
  color: #94A3B8;
}

.clear-btn {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #94A3B8;
  transition: color 0.2s;
}

.clear-btn:hover {
  color: #EF4444;
}

.btn-search {
  padding: 16px 32px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.btn-search:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
}

.hot-keywords {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.hot-label {
  font-size: 14px;
  color: #64748B;
}

.hot-word {
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.8);
  color: #64748B;
  border-radius: 16px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.hot-word:hover {
  background: #FF6B35;
  color: white;
}

/* 主内容布局 */
.main-content {
  padding: 24px 0 40px;
}

.content-layout {
  display: flex;
  gap: 24px;
}

/* 左侧筛选栏 */
.filter-sidebar {
  width: 240px;
  flex-shrink: 0;
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  height: fit-content;
  position: sticky;
  top: 80px;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-title {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 12px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-tag {
  padding: 6px 12px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-tag:hover {
  background: #FFF5F0;
  color: #FF6B35;
}

.filter-tag.active {
  background: #FF6B35;
  color: white;
}

.salary-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.salary-input {
  flex: 1;
  width: 80px;
  padding: 8px 10px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  font-size: 13px;
  outline: none;
  transition: all 0.2s;
}

.salary-input:focus {
  border-color: #FF6B35;
}

.range-separator {
  color: #94A3B8;
}

.range-unit {
  font-size: 13px;
  color: #64748B;
}

.btn-reset {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 10px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-reset:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

/* 右侧结果区 */
.results-main {
  flex: 1;
  min-width: 0;
}

.results-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.results-count {
  font-size: 14px;
  color: #64748B;
}

.results-count strong {
  color: #FF6B35;
  font-size: 18px;
}

.sort-options {
  display: flex;
  gap: 8px;
}

.sort-btn {
  padding: 8px 16px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: white;
  color: #64748B;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.sort-btn:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.sort-btn.active {
  background: #FF6B35;
  color: white;
  border-color: transparent;
}

/* 岗位列表 */
.job-list {
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
  cursor: pointer;
  transition: all 0.3s;
}

.job-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: #FF6B35;
}

.job-main {
  padding: 24px 24px 16px;
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.job-title {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  flex: 1;
  margin-right: 16px;
}

.job-salary {
  font-size: 20px;
  font-weight: 700;
  color: #FF6B35;
  white-space: nowrap;
}

.job-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.tag {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 6px;
  font-size: 12px;
}

.tag svg {
  color: #94A3B8;
}

.job-desc {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.job-company {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 24px;
  background: #F8FAFC;
  border-top: 1px solid #F1F5F9;
}

.company-avatar {
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
  flex-shrink: 0;
}

.company-info {
  flex: 1;
  min-width: 0;
}

.company-name {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 2px;
}

.company-meta {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #94A3B8;
}

.btn-apply {
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.btn-apply:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
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

.btn-reset-main {
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

.btn-reset-main:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
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

/* 响应式 */
@media (max-width: 1024px) {
  .filter-sidebar {
    display: none;
  }

  .job-company {
    flex-wrap: wrap;
  }

  .btn-apply {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
  }

  .btn-search {
    width: 100%;
  }

  .results-toolbar {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .sort-options {
    flex-wrap: wrap;
  }

  .job-header {
    flex-direction: column;
    gap: 8px;
  }

  .job-salary {
    font-size: 18px;
  }
}
</style>

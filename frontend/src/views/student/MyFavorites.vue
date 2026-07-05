<template>
  <div class="favorites-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
          </svg>
          我的收藏
        </h1>
        <p class="page-subtitle">收藏的岗位，随时查看</p>
      </div>
    </div>

    <div class="container">
      <!-- 收藏列表 -->
      <div class="favorites-grid">
        <div
          v-for="job in list"
          :key="job.id"
          class="favorite-card"
        >
          <div class="card-header" @click="$router.push(`/job/${job.id}`)">
            <h3 class="job-title">{{ job.title }}</h3>
            <button class="btn-remove" @click.stop="handleRemove(job.id)" title="取消收藏">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="#FF6B35" stroke="#FF6B35" stroke-width="2">
                <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
              </svg>
            </button>
          </div>
          <div class="job-salary" @click="$router.push(`/job/${job.id}`)">
            {{ job.salaryMin }}-{{ job.salaryMax }}元/{{ job.salaryType }}
          </div>
          <div class="job-tags" @click="$router.push(`/job/${job.id}`)">
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
          </div>
          <div class="card-footer">
            <div class="company-info">
              <div class="company-avatar" :style="job.publisherAvatar ? { backgroundImage: `url(${job.publisherAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">{{ job.publisherAvatar ? '' : job.publisherName?.charAt(0) }}</div>
              <span class="company-name">{{ job.publisherName }}</span>
            </div>
            <button class="btn-detail" @click="$router.push(`/job/${job.id}`)">查看详情</button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
        </svg>
        <h3>暂无收藏</h3>
        <p>收藏感兴趣的岗位，方便随时查看</p>
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
import { getMyFavorites, removeFavorite } from '../../api/favorite'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 12

const totalPages = computed(() => Math.ceil(total.value / pageSize))

async function fetchData() {
  const res = await getMyFavorites({ pageNum: pageNum.value, pageSize })
  list.value = res.data.records
  total.value = res.data.total
}

async function handleRemove(jobId) {
  await ElMessageBox.confirm('确认取消收藏该岗位？', '提示')
  await removeFavorite(jobId)
  ElMessage.success('已取消收藏')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.favorites-page {
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

/* 收藏网格 */
.favorites-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.favorite-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.favorite-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: #FF6B35;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  cursor: pointer;
}

.job-title {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
  flex: 1;
  margin-right: 12px;
}

.btn-remove {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.btn-remove:hover {
  transform: scale(1.2);
}

.job-salary {
  font-size: 20px;
  font-weight: 700;
  color: #FF6B35;
  margin-bottom: 16px;
  cursor: pointer;
}

.job-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  cursor: pointer;
}

.tag {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background: #F8FAFC;
  color: #64748B;
  border-radius: 8px;
  font-size: 13px;
}

.tag svg {
  color: #94A3B8;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #F1F5F9;
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
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.company-name {
  font-size: 14px;
  color: #64748B;
}

.btn-detail {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-detail:hover {
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

@media (max-width: 1024px) {
  .favorites-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .favorites-grid {
    grid-template-columns: 1fr;
  }
}
</style>

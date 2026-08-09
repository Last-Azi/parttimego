<template>
  <div class="message-page">
    <div class="page-header">
      <div class="container">
        <div class="header-content">
          <div>
            <h1 class="page-title">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              消息中心
            </h1>
            <p class="page-subtitle">查看系统消息和通知</p>
          </div>
          <button v-if="unreadCount > 0" class="btn-read-all" @click="handleReadAll">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            全部已读
          </button>
        </div>
      </div>
    </div>

    <div class="container">
      <!-- 标签页 -->
      <div class="tabs">
        <button
          class="tab"
          :class="{ active: activeTab === 'all' }"
          @click="activeTab = 'all'; fetchData()"
        >
          全部消息
        </button>
        <button
          class="tab"
          :class="{ active: activeTab === 'unread' }"
          @click="activeTab = 'unread'; fetchData()"
        >
          未读
          <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
        </button>
        <button
          class="tab"
          :class="{ active: activeTab === 'read' }"
          @click="activeTab = 'read'; fetchData()"
        >
          已读
        </button>
      </div>

      <!-- 消息列表 -->
      <div class="messages-list">
        <div
          v-for="msg in list"
          :key="msg.id"
          class="message-card"
          :class="{ unread: msg.isRead === 0 }"
          @click="handleRowClick(msg)"
        >
          <div class="message-header">
            <div class="message-type">
              <span class="type-badge" :class="typeClass(msg.type)">
                {{ getTypeText(msg.type) }}
              </span>
              <span v-if="msg.isRead === 0" class="unread-dot"></span>
            </div>
            <span class="message-time">{{ msg.createTime }}</span>
          </div>
          <h3 class="message-title">{{ msg.title }}</h3>
          <p class="message-content">{{ msg.content }}</p>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
          <polyline points="22,6 12,13 2,6"/>
        </svg>
        <h3>暂无消息</h3>
        <p>系统消息会在这里显示</p>
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

    <!-- 消息详情弹窗 -->
    <div v-if="detailVisible" class="modal-overlay" @click="detailVisible = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>消息详情</h3>
          <button class="modal-close" @click="detailVisible = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="modal-body" v-if="currentMessage">
          <div class="detail-item">
            <span class="label">类型</span>
            <span class="type-badge" :class="typeClass(currentMessage.type)">
              {{ getTypeText(currentMessage.type) }}
            </span>
          </div>
          <div class="detail-item">
            <span class="label">标题</span>
            <span class="value">{{ currentMessage.title }}</span>
          </div>
          <div class="detail-item">
            <span class="label">时间</span>
            <span class="value">{{ currentMessage.createTime }}</span>
          </div>
          <div class="detail-item full-width">
            <span class="label">内容</span>
            <div class="content">{{ currentMessage.content }}</div>
          </div>
        </div>
        <div class="modal-footer">
          <button v-if="getRouteByType(currentMessage)" class="btn-primary" @click="goToRelated">查看相关</button>
          <button class="btn-secondary" @click="detailVisible = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { getMessageList, markAsRead, markAllAsRead, getUnreadCount } from '../../api/message'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useUserStore()
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = 10
const activeTab = ref('all')
const unreadCount = ref(0)
const detailVisible = ref(false)
const currentMessage = ref(null)
let timer = null

const totalPages = computed(() => Math.ceil(total.value / pageSize))

function typeClass(type) {
  const map = {
    'APPLICATION': 'type-apply',
    'INTERVIEW': 'type-interview',
    'INTERVIEW_ACCEPTED': 'type-success',
    'INTERVIEW_REJECTED': 'type-danger',
    'INTERVIEW_COMPLETED': 'type-info',
    'ACCEPTED': 'type-success',
    'REJECTED': 'type-danger',
    'SYSTEM': 'type-system'
  }
  return map[type] || 'type-system'
}

function getTypeText(type) {
  const map = {
    'APPLICATION': '投递',
    'INTERVIEW': '面试邀请',
    'INTERVIEW_ACCEPTED': '面试接受',
    'INTERVIEW_REJECTED': '面试拒绝',
    'INTERVIEW_COMPLETED': '面试完成',
    'ACCEPTED': '录用',
    'REJECTED': '拒绝',
    'SYSTEM': '系统'
  }
  return map[type] || type
}

async function fetchData() {
  const isRead = activeTab.value === 'unread' ? 0 : activeTab.value === 'read' ? 1 : null
  const res = await getMessageList({ isRead, pageNum: pageNum.value, pageSize })
  list.value = res.data.records
  total.value = res.data.total
}

async function fetchUnreadCount() {
  const res = await getUnreadCount()
  unreadCount.value = res.data
}

async function handleRead(id) {
  await markAsRead(id)
  fetchData()
  fetchUnreadCount()
}

async function handleReadAll() {
  await markAllAsRead()
  ElMessage.success('已全部标记已读')
  fetchData()
  fetchUnreadCount()
}

function handleRowClick(row) {
  if (row.isRead === 0) {
    handleRead(row.id)
  }
  currentMessage.value = row
  detailVisible.value = true
}

function goToRelated() {
  detailVisible.value = false
  const routeInfo = getRouteByType(currentMessage.value)
  if (routeInfo) {
    router.push({ path: routeInfo.path, query: routeInfo.query })
  }
}

function getRouteByType(row) {
  if (!row) return null
  const isStudent = store.isStudent
  const isEmployer = store.isEmployer

  switch (row.type) {
    case 'APPLICATION':
      if (isEmployer) return { path: '/employer/jobs' }
      return null
    case 'INTERVIEW':
      if (isStudent) return { path: '/student/interviews', query: { highlight: row.relatedId } }
      if (isEmployer) return { path: '/employer/interviews', query: { highlight: row.relatedId } }
      return null
    case 'INTERVIEW_ACCEPTED':
      if (isEmployer) return { path: '/employer/interviews', query: { highlight: row.relatedId } }
      return null
    case 'INTERVIEW_REJECTED':
      if (isEmployer) return { path: '/employer/interviews', query: { highlight: row.relatedId } }
      return null
    case 'INTERVIEW_COMPLETED':
      if (isStudent) return { path: '/student/interviews', query: { highlight: row.relatedId } }
      return null
    case 'ACCEPTED':
      if (isStudent) return { path: '/student/applications', query: { highlight: row.relatedId } }
      return null
    case 'REJECTED':
      if (isStudent) return { path: '/student/applications', query: { highlight: row.relatedId } }
      return null
    default:
      return null
  }
}

onMounted(() => {
  fetchData()
  fetchUnreadCount()
  timer = setInterval(fetchUnreadCount, 5000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.message-page {
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

.btn-read-all {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #FF6B35;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-read-all:hover {
  border-color: #FF6B35;
  background: #FFF5F0;
}

/* 标签页 */
.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  background: white;
  padding: 8px;
  border-radius: 12px;
  border: 1px solid #E2E8F0;
}

.tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background: transparent;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.tab:hover {
  background: #F8FAFC;
}

.tab.active {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
}

.badge {
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  background: #EF4444;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tab.active .badge {
  background: rgba(255, 255, 255, 0.3);
}

/* 消息列表 */
.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.message-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  cursor: pointer;
  transition: all 0.3s;
}

.message-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.message-card.unread {
  border-left: 4px solid #FF6B35;
  background: #FFFAF5;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.message-type {
  display: flex;
  align-items: center;
  gap: 8px;
}

.type-badge {
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
}

.type-apply {
  background: #DBEAFE;
  color: #2563EB;
}

.type-interview {
  background: #FEF3C7;
  color: #D97706;
}

.type-success {
  background: #D1FAE5;
  color: #059669;
}

.type-danger {
  background: #FEE2E2;
  color: #DC2626;
}

.type-info {
  background: #F3F4F6;
  color: #6B7280;
}

.type-system {
  background: #E0E7FF;
  color: #4F46E5;
}

.unread-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #FF6B35;
}

.message-time {
  font-size: 13px;
  color: #94A3B8;
}

.message-title {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 8px;
}

.message-content {
  font-size: 14px;
  color: #64748B;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 0;
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

/* 弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal {
  background: white;
  border-radius: 20px;
  width: 500px;
  max-width: 90%;
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #F1F5F9;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1E293B;
}

.modal-close {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #94A3B8;
  transition: color 0.2s;
}

.modal-close:hover {
  color: #1E293B;
}

.modal-body {
  padding: 24px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
}

.detail-item.full-width {
  flex-direction: column;
}

.detail-item .label {
  font-size: 14px;
  color: #94A3B8;
  min-width: 60px;
}

.detail-item .value {
  font-size: 14px;
  color: #1E293B;
}

.detail-item .content {
  font-size: 14px;
  color: #1E293B;
  line-height: 1.8;
  white-space: pre-wrap;
  margin-top: 8px;
  padding: 16px;
  background: #F8FAFC;
  border-radius: 12px;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 0 24px 24px;
  justify-content: flex-end;
}

.btn-primary {
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.btn-secondary {
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-secondary:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .tabs {
    flex-wrap: wrap;
  }
}
</style>

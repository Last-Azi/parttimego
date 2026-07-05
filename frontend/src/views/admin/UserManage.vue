<template>
  <div class="users-page">
    <div class="page-header">
      <div class="container">
        <h1 class="page-title">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
          用户管理
        </h1>
        <p class="page-subtitle">管理平台用户</p>
      </div>
    </div>

    <div class="container">
      <!-- 筛选栏 -->
      <div class="filter-bar">
        <div class="filter-group">
          <label>角色</label>
          <select v-model="query.role" @change="search">
            <option value="">全部角色</option>
            <option value="STUDENT">学生</option>
            <option value="EMPLOYER">招聘方</option>
            <option value="ADMIN">管理员</option>
          </select>
        </div>
        <div class="filter-group">
          <label>状态</label>
          <select v-model="query.status" @change="search">
            <option value="">全部状态</option>
            <option :value="1">正常</option>
            <option :value="0">禁用</option>
          </select>
        </div>
      </div>

      <!-- 用户列表 -->
      <div class="users-list">
        <div
          v-for="user in list"
          :key="user.id"
          class="user-card"
        >
          <div class="card-header">
            <div class="user-info">
              <div class="user-avatar" :class="roleClass(user.role)">
                {{ user.nickname?.charAt(0) || user.username?.charAt(0) }}
              </div>
              <div>
                <h3 class="user-name">{{ user.nickname || user.username }}</h3>
                <span class="user-username">@{{ user.username }}</span>
              </div>
            </div>
            <div class="user-badges">
              <span class="role-badge" :class="roleClass(user.role)">
                {{ roleText(user.role) }}
              </span>
              <span class="status-badge" :class="{ active: user.status === 1 }">
                {{ user.status === 1 ? '正常' : '禁用' }}
              </span>
            </div>
          </div>

          <div class="user-meta">
            <span v-if="user.phone">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
              </svg>
              {{ user.phone }}
            </span>
            <span v-if="user.email">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
              {{ user.email }}
            </span>
            <span>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              {{ user.createTime }}
            </span>
          </div>

          <div class="card-actions" v-if="user.role !== 'ADMIN'">
            <button
              :class="user.status === 1 ? 'btn-disable' : 'btn-enable'"
              @click="handleToggle(user.id)"
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line v-if="user.status === 1" x1="4.93" y1="4.93" x2="19.07" y2="19.07"/>
                <polyline v-else points="20 6 9 17 4 12"/>
              </svg>
              {{ user.status === 1 ? '禁用' : '启用' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="list.length === 0" class="empty-state">
        <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
          <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
          <circle cx="9" cy="7" r="4"/>
        </svg>
        <h3>暂无用户</h3>
        <p>没有符合条件的用户</p>
      </div>

      <!-- 分页 -->
      <div v-if="total > 0" class="pagination">
        <button class="page-btn" :disabled="query.pageNum === 1" @click="query.pageNum--; fetchData()">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="15 18 9 12 15 6"/>
          </svg>
        </button>
        <button
          v-for="page in totalPages"
          :key="page"
          class="page-btn"
          :class="{ active: query.pageNum === page }"
          @click="query.pageNum = page; fetchData()"
        >
          {{ page }}
        </button>
        <button class="page-btn" :disabled="query.pageNum === totalPages" @click="query.pageNum++; fetchData()">
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
import { listUsers, toggleUserStatus } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const query = ref({ role: '', status: '', pageNum: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)

const totalPages = computed(() => Math.ceil(total.value / query.value.pageSize))

function roleClass(role) {
  return {
    'STUDENT': 'role-student',
    'EMPLOYER': 'role-employer',
    'ADMIN': 'role-admin'
  }[role] || 'role-student'
}

function roleText(role) {
  return {
    'STUDENT': '学生',
    'EMPLOYER': '招聘方',
    'ADMIN': '管理员'
  }[role] || role
}

function search() {
  query.value.pageNum = 1
  fetchData()
}

async function fetchData() {
  const params = { pageNum: query.value.pageNum, pageSize: query.value.pageSize }
  if (query.value.role) params.role = query.value.role
  if (query.value.status !== '' && query.value.status !== null) params.status = query.value.status
  const res = await listUsers(params)
  list.value = res.data.records
  total.value = res.data.total
}

async function handleToggle(id) {
  await ElMessageBox.confirm('确认切换该用户状态？', '提示')
  await toggleUserStatus(id)
  ElMessage.success('操作成功')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.users-page {
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
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-group label {
  font-size: 14px;
  color: #64748B;
  font-weight: 500;
}

.filter-group select {
  padding: 10px 16px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  font-size: 14px;
  color: #1E293B;
  cursor: pointer;
  outline: none;
  transition: all 0.2s;
}

.filter-group select:focus {
  border-color: #FF6B35;
  box-shadow: 0 0 0 3px rgba(255, 107, 53, 0.1);
}

/* 用户列表 */
.users-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.user-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  border: 1px solid #E2E8F0;
  transition: all 0.3s;
}

.user-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  color: white;
}

.user-avatar.role-student {
  background: linear-gradient(135deg, #3B82F6, #60A5FA);
}

.user-avatar.role-employer {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
}

.user-avatar.role-admin {
  background: linear-gradient(135deg, #8B5CF6, #A78BFA);
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
  margin-bottom: 4px;
}

.user-username {
  font-size: 13px;
  color: #94A3B8;
}

.user-badges {
  display: flex;
  gap: 8px;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
}

.role-badge.role-student {
  background: #DBEAFE;
  color: #2563EB;
}

.role-badge.role-employer {
  background: #FFF5F0;
  color: #FF6B35;
}

.role-badge.role-admin {
  background: #F5F3FF;
  color: #8B5CF6;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  background: #FEE2E2;
  color: #DC2626;
}

.status-badge.active {
  background: #D1FAE5;
  color: #059669;
}

.user-meta {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 16px;
}

.user-meta span {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #64748B;
}

.user-meta svg {
  color: #94A3B8;
}

.card-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #F1F5F9;
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

.btn-disable {
  border: 1px solid #E2E8F0;
  background: white;
  color: #EF4444;
}

.btn-disable:hover {
  border-color: #EF4444;
  background: #FEF2F2;
}

.btn-enable {
  border: 1px solid #E2E8F0;
  background: white;
  color: #059669;
}

.btn-enable:hover {
  border-color: #059669;
  background: #D1FAE5;
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

  .user-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

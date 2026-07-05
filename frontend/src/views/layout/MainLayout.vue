<template>
  <div class="layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="container header-content">
        <div class="header-left">
          <div class="logo" @click="$router.push('/')">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <rect width="32" height="32" rx="8" fill="url(#logo-gradient)"/>
              <path d="M10 16L14 20L22 12" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
              <defs>
                <linearGradient id="logo-gradient" x1="0" y1="0" x2="32" y2="32">
                  <stop stop-color="#FF6B35"/>
                  <stop offset="1" stop-color="#FF8E53"/>
                </linearGradient>
              </defs>
            </svg>
            <span class="logo-text">PartTimeGo</span>
          </div>
          <nav class="nav-links">
            <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
              </svg>
              首页
            </router-link>
            <router-link to="/search" class="nav-link" :class="{ active: $route.path === '/search' }">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/>
                <path d="M21 21l-4.35-4.35"/>
              </svg>
              找兼职
            </router-link>

            <!-- 学生菜单 -->
            <template v-if="store.isStudent">
              <router-link to="/student/resume" class="nav-link" :class="{ active: $route.path === '/student/resume' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                  <polyline points="14 2 14 8 20 8"/>
                </svg>
                我的简历
              </router-link>
              <router-link to="/student/applications" class="nav-link" :class="{ active: $route.path === '/student/applications' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
                我的投递
              </router-link>
              <router-link to="/student/favorites" class="nav-link" :class="{ active: $route.path === '/student/favorites' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                </svg>
                我的收藏
              </router-link>
              <router-link to="/student/interviews" class="nav-link" :class="{ active: $route.path === '/student/interviews' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                  <line x1="16" y1="2" x2="16" y2="6"/>
                  <line x1="8" y1="2" x2="8" y2="6"/>
                  <line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
                我的面试
              </router-link>
            </template>

            <!-- 企业菜单 -->
            <template v-if="store.isEmployer">
              <router-link to="/employer/dashboard" class="nav-link" :class="{ active: $route.path === '/employer/dashboard' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M21.21 15.89A10 10 0 1 1 8 2.83"/>
                  <path d="M22 12A10 10 0 0 0 12 2v10z"/>
                </svg>
                企业后台
              </router-link>
              <router-link to="/employer/jobs" class="nav-link" :class="{ active: $route.path === '/employer/jobs' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                  <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg>
                我的岗位
              </router-link>
              <router-link to="/employer/job/publish" class="nav-link" :class="{ active: $route.path === '/employer/job/publish' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="12" y1="8" x2="12" y2="16"/>
                  <line x1="8" y1="12" x2="16" y2="12"/>
                </svg>
                发布岗位
              </router-link>
              <router-link to="/employer/interviews" class="nav-link" :class="{ active: $route.path === '/employer/interviews' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                  <line x1="16" y1="2" x2="16" y2="6"/>
                  <line x1="8" y1="2" x2="8" y2="6"/>
                  <line x1="3" y1="10" x2="21" y2="10"/>
                </svg>
                面试管理
              </router-link>
            </template>

            <!-- 管理员菜单 -->
            <template v-if="store.isAdmin">
              <router-link to="/admin/dashboard" class="nav-link" :class="{ active: $route.path === '/admin/dashboard' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M18 20V10"/>
                  <path d="M12 20V4"/>
                  <path d="M6 20v-6"/>
                </svg>
                数据统计
              </router-link>
              <router-link to="/admin/jobs" class="nav-link" :class="{ active: $route.path === '/admin/jobs' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
                岗位审核
              </router-link>
              <router-link to="/admin/users" class="nav-link" :class="{ active: $route.path === '/admin/users' }">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                  <circle cx="9" cy="7" r="4"/>
                  <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
                  <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                </svg>
                用户管理
              </router-link>
            </template>
          </nav>
        </div>

        <div class="header-right">
          <!-- 实时在线人数 -->
          <div class="online-indicator">
            <span class="online-dot"></span>
            <span class="online-text">{{ onlineCount }}人在线</span>
          </div>

          <!-- 未登录 -->
          <template v-if="!store.isLoggedIn">
            <button class="btn-login" @click="$router.push('/login')">登录</button>
            <button class="btn-register" @click="$router.push('/register')">注册</button>
          </template>

          <!-- 已登录 -->
          <template v-else>
            <!-- 系统消息 -->
            <div class="notification" @click="goToMessages" title="系统消息">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
                <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
              </svg>
              <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
            </div>

            <!-- 即时聊天 -->
            <div class="notification" @click="goToChat" title="即时聊天">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                <line x1="9" y1="10" x2="15" y2="10"/>
              </svg>
            </div>

            <!-- 用户菜单 -->
            <input
              type="file"
              ref="avatarInput"
              accept="image/*"
              style="display: none"
              @change="handleAvatarChange"
            />
            <div class="user-menu" @click="showUserMenu = !showUserMenu">
              <div class="user-avatar" :style="avatarStyle">
                <span v-if="!store.user?.avatar">{{ (store.user?.nickname || store.user?.username || '').charAt(0).toUpperCase() }}</span>
              </div>
              <span class="username">{{ store.user?.nickname || store.user?.username }}</span>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" :class="{ rotated: showUserMenu }">
                <polyline points="6 9 12 15 18 9"/>
              </svg>

              <!-- 下拉菜单 -->
              <transition name="dropdown">
                <div v-if="showUserMenu" class="dropdown-menu" @click.stop>
                  <!-- 个人信息 -->
                  <div class="dropdown-header">
                    <div class="dropdown-avatar" :style="avatarStyle">
                      <span v-if="!store.user?.avatar">{{ (store.user?.nickname || store.user?.username || '').charAt(0).toUpperCase() }}</span>
                    </div>
                    <div>
                      <div class="dropdown-name">{{ store.user?.nickname || store.user?.username }}</div>
                      <div class="dropdown-role">{{ store.isStudent ? '学生' : store.isEmployer ? '招聘方' : '管理员' }}</div>
                    </div>
                  </div>

                  <div class="dropdown-divider"></div>

                  <!-- 修改昵称 -->
                  <a class="dropdown-item" @click="editNickname">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M12 20h9"/>
                      <path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/>
                    </svg>
                    修改昵称
                  </a>
                  <!-- 上传头像 -->
                  <a class="dropdown-item" @click="uploadAvatar">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <circle cx="12" cy="12" r="10"/>
                      <path d="M12 6v6l4 2"/>
                    </svg>
                    上传头像
                  </a>

                  <!-- 退出登录 -->
                  <a class="dropdown-item logout" @click="handleLogout">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                      <polyline points="16 17 21 12 16 7"/>
                      <line x1="21" y1="12" x2="9" y2="12"/>
                    </svg>
                    退出登录
                  </a>
                </div>
              </transition>
            </div>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部 -->
    <footer class="footer">
      <div class="container footer-content">
        <div class="footer-brand">
          <div class="logo">
            <svg width="24" height="24" viewBox="0 0 32 32" fill="none">
              <rect width="32" height="32" rx="8" fill="url(#footer-logo-gradient)"/>
              <path d="M10 16L14 20L22 12" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
              <defs>
                <linearGradient id="footer-logo-gradient" x1="0" y1="0" x2="32" y2="32">
                  <stop stop-color="#FF6B35"/>
                  <stop offset="1" stop-color="#FF8E53"/>
                </linearGradient>
              </defs>
            </svg>
            <span class="logo-text">PartTimeGo</span>
          </div>
          <p class="footer-desc">大学生兼职招聘平台</p>
        </div>
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">帮助中心</a>
          <a href="#">联系客服</a>
          <a href="#">隐私政策</a>
        </div>
        <p class="copyright">&copy; 2024 PartTimeGo. All rights reserved.</p>
      </div>
    </footer>

    <!-- 修改昵称弹窗 -->
    <div v-if="nicknameVisible" class="modal-overlay" @click="nicknameVisible = false">
      <div class="modal" @click.stop>
        <div class="modal-header">
          <h3>修改昵称</h3>
          <button class="modal-close" @click="nicknameVisible = false">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18"/>
              <line x1="6" y1="6" x2="18" y2="18"/>
            </svg>
          </button>
        </div>
        <div class="modal-body">
          <input
            v-model="newNickname"
            type="text"
            placeholder="请输入新昵称"
            class="modal-input"
            maxlength="20"
            @keyup.enter="handleUpdateNickname"
          >
          <span class="char-count">{{ newNickname.length }}/20</span>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="nicknameVisible = false">取消</button>
          <button class="btn-confirm" @click="handleUpdateNickname">确定</button>
        </div>
      </div>
    </div>

    <!-- 头像裁切弹窗 -->
    <AvatarCropper
      :visible="cropperVisible"
      :image-src="cropperImageSrc"
      @close="cropperVisible = false"
      @crop="handleCropConfirm"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { logout, updateNickname } from '../../api/auth'
import { getUnreadCount } from '../../api/message'
import { getOnlineCount, heartbeat } from '../../api/portal'

import { ElMessage } from 'element-plus'
import AvatarCropper from '../../components/AvatarCropper.vue'

const uploadAvatarApi = (formData) => {
  const token = localStorage.getItem('token')
  return fetch('/api/file/upload/avatar', {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` },
    body: formData
  }).then(r => r.json()).then(res => {
    if (res.code === 200) return res
    throw new Error(res.msg)
  })
}

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const unreadCount = ref(0)
const onlineCount = ref(0)
const showUserMenu = ref(false)
const nicknameVisible = ref(false)
const newNickname = ref('')
const avatarInput = ref(null)
const cropperVisible = ref(false)
const cropperImageSrc = ref('')
let timer = null

const avatarStyle = computed(() => {
  if (store.user?.avatar) {
    return { backgroundImage: `url(${store.user.avatar})`, backgroundSize: 'cover', backgroundPosition: 'center' }
  }
  return {}
})

async function fetchUnreadCount() {
  if (!store.isLoggedIn) return
  try {
    const res = await getUnreadCount()
    unreadCount.value = res.data
  } catch {}
}

async function fetchOnlineCount() {
  try {
    const res = await getOnlineCount()
    onlineCount.value = res.data || 0
  } catch {
    onlineCount.value = 0
  }
}

function goToMessages() {
  // 系统消息（投递通知、面试通知等）
  if (store.isEmployer) {
    router.push('/employer/messages')
  } else {
    router.push('/messages')
  }
}

function goToChat() {
  // 即时聊天
  router.push('/chat')
}

function editNickname() {
  showUserMenu.value = false
  newNickname.value = store.user?.nickname || ''
  nicknameVisible.value = true
}

function uploadAvatar() {
  showUserMenu.value = false
  avatarInput.value?.click()
}

async function handleAvatarChange(e) {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过 5MB')
    return
  }
  // 读取文件为 data URL，打开裁切弹窗
  const reader = new FileReader()
  reader.onload = (ev) => {
    cropperImageSrc.value = ev.target.result
    cropperVisible.value = true
  }
  reader.readAsDataURL(file)
  e.target.value = ''
}

async function handleCropConfirm(blob) {
  cropperVisible.value = false
  try {
    const formData = new FormData()
    formData.append('file', blob, 'avatar.jpg')
    const res = await uploadAvatarApi(formData)
    store.updateUser({ avatar: res.data })
    ElMessage.success('头像上传成功')
  } catch {
    ElMessage.error('头像上传失败')
  }
}

async function handleUpdateNickname() {
  if (!newNickname.value.trim()) {
    ElMessage.warning('昵称不能为空')
    return
  }
  await updateNickname(newNickname.value.trim())
  store.updateUser({ nickname: newNickname.value.trim() })
  nicknameVisible.value = false
  ElMessage.success('昵称修改成功')
}

async function handleLogout() {
  showUserMenu.value = false
  try { await logout() } catch {}
  store.logout()
  router.push('/login')
}

// 点击外部关闭菜单
function closeMenu(e) {
  if (!e.target.closest('.user-menu')) {
    showUserMenu.value = false
  }
}

onMounted(() => {
  fetchUnreadCount()
  fetchOnlineCount()
  // 心跳上报（登录用户每30秒发一次）
  if (store.isLoggedIn) {
    heartbeat().catch(() => {})
  }
  timer = setInterval(() => {
    fetchUnreadCount()
    fetchOnlineCount()
    if (store.isLoggedIn) {
      heartbeat().catch(() => {})
    }
  }, 30000) // 每30秒更新
  document.addEventListener('click', closeMenu)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
  document.removeEventListener('click', closeMenu)
})
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 头部导航 */
.header {
  position: sticky;
  top: 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #E2E8F0;
  z-index: 1000;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #FF6B35;
}

.nav-links {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  color: #64748B;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.2s;
  white-space: nowrap;
}

.nav-link:hover {
  color: #FF6B35;
  background: #FFF5F0;
}

.nav-link.active {
  color: #FF6B35;
  background: #FFF5F0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 在线人数 */
.online-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #F0FDF4;
  border-radius: 20px;
  font-size: 13px;
}

.online-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #22C55E;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.online-text {
  color: #16A34A;
  font-weight: 500;
}

/* 登录注册按钮 */
.btn-login {
  padding: 8px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  background: transparent;
  color: #1E293B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-login:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-register {
  padding: 8px 20px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-register:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

/* 通知图标 */
.notification {
  position: relative;
  padding: 8px;
  cursor: pointer;
  color: #64748B;
  transition: color 0.2s;
}

.notification:hover {
  color: #FF6B35;
}

.badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  background: #EF4444;
  color: white;
  border-radius: 9px;
  font-size: 11px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 用户菜单 */
.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  cursor: pointer;
  border-radius: 12px;
  transition: background 0.2s;
}

.user-menu:hover {
  background: #F8FAFC;
}

.user-avatar {
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

.username {
  color: #1E293B;
  font-size: 14px;
  font-weight: 500;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-menu svg {
  color: #94A3B8;
  transition: transform 0.2s;
}

.user-menu svg.rotated {
  transform: rotate(180deg);
}

/* 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  width: 200px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  border: 1px solid #E2E8F0;
  overflow: hidden;
  z-index: 1000;
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #F8FAFC;
}

.dropdown-avatar {
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
}

.dropdown-name {
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
}

.dropdown-role {
  font-size: 13px;
  color: #94A3B8;
}

.dropdown-divider {
  height: 1px;
  background: #F1F5F9;
  margin: 4px 0;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.dropdown-item:hover {
  background: #FFF5F0;
  color: #FF6B35;
}

.dropdown-item svg {
  color: #94A3B8;
}

.dropdown-item:hover svg {
  color: #FF6B35;
}

.dropdown-item.logout {
  color: #EF4444;
}

.dropdown-item.logout:hover {
  background: #FEF2F2;
}

.dropdown-item.logout svg {
  color: #EF4444;
}

/* 下拉动画 */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.2s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* 主内容 */
.main-content {
  flex: 1;
  background: #F8FAFC;
}

/* 底部 */
.footer {
  background: #1E293B;
  color: white;
  padding: 40px 0 20px;
}

.footer-content {
  text-align: center;
}

.footer-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 16px;
}

.footer-brand .logo-text {
  color: white;
}

.footer-desc {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  margin-bottom: 24px;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 24px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: white;
}

.copyright {
  color: rgba(255, 255, 255, 0.4);
  font-size: 13px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
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
  width: 400px;
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
  position: relative;
}

.modal-input {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid #E2E8F0;
  border-radius: 12px;
  font-size: 15px;
  outline: none;
  transition: all 0.2s;
}

.modal-input:focus {
  border-color: #FF6B35;
  box-shadow: 0 0 0 4px rgba(255, 107, 53, 0.1);
}

.char-count {
  position: absolute;
  right: 36px;
  bottom: 36px;
  font-size: 13px;
  color: #94A3B8;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 0 24px 24px;
  justify-content: flex-end;
}

.btn-cancel {
  padding: 10px 20px;
  border: 1px solid #E2E8F0;
  border-radius: 10px;
  background: white;
  color: #64748B;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  border-color: #FF6B35;
  color: #FF6B35;
}

.btn-confirm {
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

.btn-confirm:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

/* 响应式 */
@media (max-width: 1024px) {
  .nav-links {
    display: none;
  }

  .username {
    display: none;
  }

  .footer-links {
    flex-direction: column;
    gap: 12px;
  }
}
</style>

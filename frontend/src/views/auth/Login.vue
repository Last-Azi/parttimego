<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌区域 -->
      <div class="brand-section">
        <div class="brand-content">
          <div class="logo">
            <svg width="48" height="48" viewBox="0 0 32 32" fill="none">
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
          <h1 class="brand-title">欢迎回来</h1>
          <p class="brand-subtitle">登录你的账号，开启兼职之旅</p>
          <div class="features">
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                <polyline points="22 4 12 14.01 9 11.01"/>
              </svg>
              <span>海量兼职岗位</span>
            </div>
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
              <span>安全可靠保障</span>
            </div>
            <div class="feature-item">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#FF6B35" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <polyline points="12 6 12 12 16 14"/>
              </svg>
              <span>快速上岗入职</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="form-section">
        <div class="form-content">
          <h2 class="form-title">账号登录</h2>
          <p class="form-subtitle">请输入你的账号和密码</p>

          <form @submit.prevent="handleLogin" class="login-form">
            <div class="form-group">
              <label class="form-label">用户名</label>
              <div class="input-wrapper">
                <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
                <input
                  v-model="form.username"
                  type="text"
                  placeholder="请输入用户名"
                  class="form-input"
                  :class="{ error: errors.username }"
                >
              </div>
              <span v-if="errors.username" class="error-message">{{ errors.username }}</span>
            </div>

            <div class="form-group">
              <label class="form-label">密码</label>
              <div class="input-wrapper">
                <svg class="input-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
                <input
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="请输入密码"
                  class="form-input"
                  :class="{ error: errors.password }"
                  @keyup.enter="handleLogin"
                >
                <button type="button" class="toggle-password" @click="showPassword = !showPassword">
                  <svg v-if="!showPassword" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                  </svg>
                  <svg v-else width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                    <line x1="1" y1="1" x2="23" y2="23"/>
                  </svg>
                </button>
              </div>
              <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
            </div>

            <div class="form-options">
              <label class="remember-me">
                <input type="checkbox" v-model="rememberMe">
                <span>记住我</span>
              </label>
              <a href="#" class="forgot-password">忘记密码？</a>
            </div>

            <button type="submit" class="btn-login" :disabled="loading">
              <span v-if="loading" class="loading-spinner"></span>
              <span v-else>登录</span>
            </button>
          </form>

          <div class="divider">
            <span>或</span>
          </div>

          <div class="register-link">
            <span>还没有账号？</span>
            <router-link to="/register" class="link">立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { login } from '../../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useUserStore()
const loading = ref(false)
const showPassword = ref(false)
const rememberMe = ref(false)
const form = ref({ username: '', password: '' })
const errors = ref({ username: '', password: '' })

function validate() {
  errors.value = { username: '', password: '' }
  let valid = true

  if (!form.value.username.trim()) {
    errors.value.username = '请输入用户名'
    valid = false
  }

  if (!form.value.password) {
    errors.value.password = '请输入密码'
    valid = false
  } else if (form.value.password.length < 6) {
    errors.value.password = '密码长度不能少于6位'
    valid = false
  }

  return valid
}

async function handleLogin() {
  if (!validate()) return

  loading.value = true
  try {
    const res = await login(form.value)
    store.setLoginInfo(res.data.token, res.data.user)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #FFF5F0 0%, #FFE8D6 50%, #FFF0E6 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(255, 107, 53, 0.1), rgba(255, 142, 83, 0.05));
}

.circle-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  right: -100px;
}

.circle-2 {
  width: 300px;
  height: 300px;
  bottom: -50px;
  left: -50px;
}

.circle-3 {
  width: 200px;
  height: 200px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

/* 登录容器 */
.login-container {
  display: flex;
  width: 100%;
  max-width: 1000px;
  background: white;
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

/* 左侧品牌区域 */
.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #FF6B35 0%, #FF8E53 100%);
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-content {
  color: white;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 40px;
}

.logo-text {
  font-size: 28px;
  font-weight: 700;
}

.brand-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 12px;
  line-height: 1.2;
}

.brand-subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 40px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  opacity: 0.95;
}

.feature-item svg {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  padding: 4px;
}

/* 右侧表单区域 */
.form-section {
  flex: 1;
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-content {
  width: 100%;
  max-width: 360px;
}

.form-title {
  font-size: 28px;
  font-weight: 700;
  color: #1E293B;
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 15px;
  color: #64748B;
  margin-bottom: 32px;
}

/* 表单样式 */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-label {
  font-size: 14px;
  font-weight: 600;
  color: #1E293B;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #F8FAFC;
  border: 2px solid #E2E8F0;
  border-radius: 12px;
  padding: 0 16px;
  transition: all 0.2s;
}

.input-wrapper:focus-within {
  border-color: #FF6B35;
  background: white;
  box-shadow: 0 0 0 4px rgba(255, 107, 53, 0.1);
}

.input-wrapper.error {
  border-color: #EF4444;
}

.input-icon {
  color: #94A3B8;
  margin-right: 12px;
  flex-shrink: 0;
}

.form-input {
  flex: 1;
  border: none;
  outline: none;
  padding: 14px 0;
  font-size: 15px;
  background: transparent;
  color: #1E293B;
}

.form-input::placeholder {
  color: #94A3B8;
}

.toggle-password {
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  color: #94A3B8;
  transition: color 0.2s;
}

.toggle-password:hover {
  color: #64748B;
}

.error-message {
  font-size: 13px;
  color: #EF4444;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748B;
  cursor: pointer;
}

.remember-me input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: #FF6B35;
}

.forgot-password {
  font-size: 14px;
  color: #FF6B35;
  text-decoration: none;
  font-weight: 500;
}

.forgot-password:hover {
  text-decoration: underline;
}

/* 登录按钮 */
.btn-login {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-login:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 107, 53, 0.3);
}

.btn-login:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 分割线 */
.divider {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 24px 0;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #E2E8F0;
}

.divider span {
  font-size: 14px;
  color: #94A3B8;
}

/* 注册链接 */
.register-link {
  text-align: center;
  font-size: 14px;
  color: #64748B;
}

.register-link .link {
  color: #FF6B35;
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
}

.register-link .link:hover {
  text-decoration: underline;
}

/* 响应式 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }

  .brand-section {
    padding: 40px;
  }

  .brand-title {
    font-size: 28px;
  }

  .form-section {
    padding: 40px;
  }

  .features {
    display: none;
  }
}
</style>

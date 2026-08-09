<template>
  <div class="chat-page">
    <div class="chat-layout">
      <!-- 左侧会话列表 -->
      <div class="session-panel">
        <!-- 搜索栏 -->
        <div class="session-search">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="M21 21l-4.35-4.35"/>
          </svg>
          <input type="text" placeholder="搜索联系人..." v-model="searchText">
        </div>

        <!-- 会话列表 -->
        <div class="session-list">
          <div
            v-for="session in filteredSessions"
            :key="session.id"
            class="session-item"
            :class="{ active: currentSession?.id === session.id }"
            @click="selectSession(session)"
          >
            <div class="session-avatar" :style="session.otherUserAvatar ? { backgroundImage: `url(${session.otherUserAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
              {{ session.otherUserAvatar ? '' : (session.otherUserName?.charAt(0) || '?') }}
              <span v-if="session.unreadCount > 0" class="online-dot"></span>
            </div>
            <div class="session-content">
              <div class="session-header">
                <span class="session-name">{{ session.otherUserName }}</span>
                <span class="session-time">{{ formatTime(session.lastTime) }}</span>
              </div>
              <div class="session-footer">
                <span class="session-msg">{{ session.lastMessage || '暂无消息' }}</span>
                <span v-if="session.unreadCount > 0" class="unread-badge">{{ session.unreadCount }}</span>
              </div>
            </div>
          </div>

          <div v-if="filteredSessions.length === 0" class="empty-session">
            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#CBD5E1" stroke-width="1.5">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            </svg>
            <p>暂无聊天记录</p>
          </div>
        </div>
      </div>

      <!-- 右侧聊天区域 -->
      <div class="chat-panel">
        <template v-if="currentSession">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <div class="chat-user">
              <div class="chat-avatar" :style="currentSession.otherUserAvatar ? { backgroundImage: `url(${currentSession.otherUserAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                {{ currentSession.otherUserAvatar ? '' : (currentSession.otherUserName?.charAt(0) || '?') }}
              </div>
              <div class="chat-info">
                <h4>{{ currentSession.otherUserName }}</h4>
                <span class="chat-status">在线</span>
              </div>
            </div>
            <div class="chat-actions">
              <button class="action-btn" title="查看岗位">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="2" y="7" width="20" height="14" rx="2" ry="2"/>
                  <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- 消息列表 -->
          <div class="message-list" ref="messageList">
            <div
              v-for="(msg, index) in messages"
              :key="msg.id"
              class="message-item"
              :class="{ 'message-mine': msg.senderId === currentUserId }"
            >
              <!-- 时间显示在聊天框中间 -->
              <div class="message-time-center" v-if="showTimeLabel(index, msg)">
                {{ formatMessageTime(msg.createTime) }}
              </div>

              <!-- 消息气泡 -->
              <div class="message-row">
                <!-- 对方消息：头像在左，气泡在右 -->
                <template v-if="msg.senderId !== currentUserId">
                  <div class="message-avatar" :style="msg.senderAvatar ? { backgroundImage: `url(${msg.senderAvatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                    {{ msg.senderAvatar ? '' : getDisplayName(msg.senderName).charAt(0) }}
                  </div>
                  <div class="message-bubble other">
                    <div v-if="msg.msgType === 'IMAGE'" class="message-image">
                      <img :src="msg.content" @click="previewImage(msg.content)" />
                    </div>
                    <div v-else-if="msg.msgType === 'FILE'" class="message-file">
                      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                        <polyline points="14 2 14 8 20 8"/>
                      </svg>
                      <a :href="msg.content" target="_blank" download>下载文件</a>
                    </div>
                    <div v-else class="message-text">{{ msg.content }}</div>
                  </div>
                </template>
                <!-- 自己消息：气泡在左，头像在右 -->
                <template v-else>
                  <div class="message-bubble mine">
                    <div v-if="msg.msgType === 'IMAGE'" class="message-image">
                      <img :src="msg.content" @click="previewImage(msg.content)" />
                    </div>
                    <div v-else-if="msg.msgType === 'FILE'" class="message-file">
                      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                        <polyline points="14 2 14 8 20 8"/>
                      </svg>
                      <a :href="msg.content" target="_blank" download>下载文件</a>
                    </div>
                    <div v-else class="message-text">{{ msg.content }}</div>
                  </div>
                  <div class="message-avatar" :style="currentUser?.avatar ? { backgroundImage: `url(${currentUser.avatar})`, backgroundSize: 'cover', backgroundPosition: 'center' } : {}">
                    {{ currentUser?.avatar ? '' : getDisplayName(currentUser?.nickname || currentUser?.username).charAt(0) }}
                  </div>
                </template>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="chat-input">
            <div class="input-toolbar">
              <button class="toolbar-btn" title="表情">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <path d="M8 14s1.5 2 4 2 4-2 4-2"/>
                  <line x1="9" y1="9" x2="9.01" y2="9"/>
                  <line x1="15" y1="9" x2="15.01" y2="9"/>
                </svg>
              </button>
              <button class="toolbar-btn" title="图片" @click="$refs.imageInput.click()">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
                  <circle cx="8.5" cy="8.5" r="1.5"/>
                  <polyline points="21 15 16 10 5 21"/>
                </svg>
              </button>
              <button class="toolbar-btn" title="文件" @click="$refs.fileInput.click()">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
                  <path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/>
                </svg>
              </button>
              <input type="file" ref="imageInput" accept="image/*" style="display:none" @change="handleImageUpload" />
              <input type="file" ref="fileInput" accept=".pdf,.doc,.docx,.xls,.xlsx,.zip,.rar" style="display:none" @change="handleFileUpload" />
            </div>
            <div class="input-box">
              <textarea
                v-model="inputMessage"
                placeholder="输入消息..."
                @keyup.enter.prevent="sendMessage"
                @input="adjustTextarea"
                ref="textareaRef"
                rows="1"
              ></textarea>
              <button class="btn-send" @click="sendMessage" :disabled="!inputMessage.trim()">
                发送
              </button>
            </div>
          </div>
        </template>

        <!-- 未选择会话 -->
        <div v-else class="empty-chat">
          <svg width="100" height="100" viewBox="0 0 24 24" fill="none" stroke="#E2E8F0" stroke-width="1">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
            <path d="M8 9h8"/>
            <path d="M8 13h6"/>
          </svg>
          <h3>开始聊天</h3>
          <p>选择一个会话，或在岗位详情页联系企业</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { getChatSessions, getChatMessages, sendChatMessage, markChatAsRead } from '../../api/chat'
import { ElMessage } from 'element-plus'

const route = useRoute()
const store = useUserStore()
const currentUserId = ref(store.user?.id)
const currentUser = ref(store.user)
const sessions = ref([])
const currentSession = ref(null)
const messages = ref([])
const inputMessage = ref('')
const messageList = ref(null)
const textareaRef = ref(null)
const searchText = ref('')

// 过滤会话
const filteredSessions = computed(() => {
  if (!searchText.value) return sessions.value
  return sessions.value.filter(s =>
    s.otherUserName?.includes(searchText.value)
  )
})

// 格式化时间
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'

  const month = date.getMonth() + 1
  const day = date.getDate()

  if (date.getFullYear() === now.getFullYear()) {
    return `${month}月${day}日`
  }
  return `${date.getFullYear()}/${month}/${day}`
}

// 格式化消息时间
function formatMessageTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const hours = date.getHours().toString().padStart(2, '0')
  const minutes = date.getMinutes().toString().padStart(2, '0')
  return `${hours}:${minutes}`
}

function formatMessageTimeShort(time) {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

// 获取显示名称：优先昵称，没有则用账户名
function getDisplayName(name) {
  return name || '用户'
}

// 是否显示时间标签
function showTimeLabel(index, msg) {
  if (index === 0) return true
  const prevMsg = messages.value[index - 1]
  const prevTime = new Date(prevMsg.createTime).getTime()
  const currTime = new Date(msg.createTime).getTime()
  return currTime - prevTime > 300000 // 5分钟
}

// 调整文本框高度
function adjustTextarea() {
  const textarea = textareaRef.value
  if (textarea) {
    textarea.style.height = 'auto'
    textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px'
  }
}

// 获取会话列表
async function fetchSessions() {
  try {
    const res = await getChatSessions()
    sessions.value = res.data
  } catch {}
}

// 选择会话
async function selectSession(session) {
  currentSession.value = session
  await fetchMessages(session.id)
  // 标记已读
  if (session.unreadCount > 0) {
    await markChatAsRead(session.id)
    session.unreadCount = 0
  }
}

// 获取消息
async function fetchMessages(sessionId) {
  try {
    const res = await getChatMessages(sessionId, { pageNum: 1, pageSize: 50 })
    messages.value = res.data
    await nextTick()
    scrollToBottom()
  } catch {}
}

// 发送消息
async function sendMessage() {
  if (!inputMessage.value.trim() || !currentSession.value) return

  try {
    const res = await sendChatMessage({
      sessionId: currentSession.value.id,
      content: inputMessage.value.trim()
    })

    messages.value.push(res.data)
    inputMessage.value = ''

    // 更新会话列表
    currentSession.value.lastMessage = res.data.content
    currentSession.value.lastTime = res.data.createTime

    // 重置文本框高度
    if (textareaRef.value) {
      textareaRef.value.style.height = 'auto'
    }

    await nextTick()
    scrollToBottom()
  } catch {
    ElMessage.error('发送失败')
  }
}

// 上传并发送图片
async function handleImageUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过 2MB')
    return
  }
  await sendFileMessage(file, 'IMAGE', '/api/file/upload/chat')
  e.target.value = ''
}

// 上传并发送文件
async function handleFileUpload(e) {
  const file = e.target.files[0]
  if (!file) return
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.warning('文件大小不能超过 10MB')
    return
  }
  await sendFileMessage(file, 'FILE', '/api/file/upload/chat')
  e.target.value = ''
}

// 通用上传并发送
async function sendFileMessage(file, msgType, uploadUrl) {
  if (!currentSession.value) return
  try {
    const token = localStorage.getItem('token')
    const formData = new FormData()
    formData.append('file', file)
    const uploadRes = await fetch(uploadUrl, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token}` },
      body: formData
    }).then(r => r.json())
    if (uploadRes.code !== 200) throw new Error(uploadRes.msg)

    const res = await sendChatMessage({
      sessionId: currentSession.value.id,
      content: uploadRes.data,
      msgType
    })
    messages.value.push(res.data)
    currentSession.value.lastMessage = msgType === 'IMAGE' ? '[图片]' : '[文件]'
    currentSession.value.lastTime = res.data.createTime
    await nextTick()
    scrollToBottom()
  } catch {
    ElMessage.error('发送失败')
  }
}

// 图片预览
function previewImage(url) {
  window.open(url, '_blank')
}

// 滚动到底部
function scrollToBottom() {
  if (messageList.value) {
    messageList.value.scrollTop = messageList.value.scrollHeight
  }
}

// 监听路由参数变化
watch(() => route.query.sessionId, (newSessionId) => {
  if (newSessionId) {
    const session = sessions.value.find(s => s.id === Number(newSessionId))
    if (session) {
      selectSession(session)
    }
  }
})

onMounted(async () => {
  await fetchSessions()

  // 如果URL带有sessionId参数，自动选中该会话
  if (route.query.sessionId) {
    const session = sessions.value.find(s => s.id === Number(route.query.sessionId))
    if (session) {
      selectSession(session)
    }
  }
})
</script>

<style scoped>
.chat-page {
  height: calc(100vh - 64px);
  background: #F8FAFC;
  padding: 20px;
}

.chat-layout {
  display: flex;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid #E2E8F0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* 左侧会话面板 */
.session-panel {
  width: 320px;
  border-right: 1px solid #E2E8F0;
  display: flex;
  flex-direction: column;
  background: #FAFBFC;
}

.session-search {
  padding: 16px;
  border-bottom: 1px solid #E2E8F0;
  display: flex;
  align-items: center;
  gap: 10px;
  background: white;
}

.session-search input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
  color: #1E293B;
  background: transparent;
}

.session-search input::placeholder {
  color: #94A3B8;
}

.session-list {
  flex: 1;
  overflow-y: auto;
}

.session-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 1px solid #F1F5F9;
}

.session-item:hover {
  background: #F1F5F9;
}

.session-item.active {
  background: #FFF5F0;
  border-left: 3px solid #FF6B35;
}

.session-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
  flex-shrink: 0;
  position: relative;
}

.online-dot {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #22C55E;
  border: 2px solid white;
}

.session-content {
  flex: 1;
  min-width: 0;
}

.session-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.session-name {
  font-size: 15px;
  font-weight: 600;
  color: #1E293B;
}

.session-time {
  font-size: 12px;
  color: #94A3B8;
}

.session-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.session-msg {
  font-size: 13px;
  color: #64748B;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.unread-badge {
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  background: #EF4444;
  color: white;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.empty-session {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #94A3B8;
}

.empty-session svg {
  margin-bottom: 12px;
  opacity: 0.5;
}

/* 右侧聊天面板 */
.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 16px 20px;
  border-bottom: 1px solid #E2E8F0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
}

.chat-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.chat-avatar {
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

.chat-info h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1E293B;
}

.chat-status {
  font-size: 12px;
  color: #22C55E;
}

.chat-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 8px;
  border: none;
  border-radius: 8px;
  background: #F1F5F9;
  color: #64748B;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn:hover {
  background: #E2E8F0;
  color: #1E293B;
}

/* 消息列表 */
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: #F8FAFC;
}

.message-time-label {
  text-align: center;
  font-size: 12px;
  color: #94A3B8;
  margin: 8px 0;
}

.message-item {
  display: flex;
  flex-direction: column;
}

.message-item.message-mine {
  align-items: flex-end;
}

.message-item:not(.message-mine) {
  align-items: flex-start;
}

.message-row {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.message-avatar {
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

.message-bubble {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.message-text {
  padding: 12px 16px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.message-bubble.other .message-text {
  background: white;
  color: #1E293B;
  border-radius: 12px 12px 12px 0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.message-bubble.mine .message-text {
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  border-radius: 12px 12px 0 12px;
}

.message-image {
  max-width: 240px;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
}

.message-image img {
  width: 100%;
  display: block;
  border-radius: 12px;
}

.message-bubble.other .message-image {
  border-radius: 12px 12px 12px 0;
}

.message-bubble.mine .message-image {
  border-radius: 12px 12px 0 12px;
}

.message-file {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 12px;
  background: #F8FAFC;
}

.message-bubble.other .message-file {
  background: white;
  border-radius: 12px 12px 12px 0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.message-bubble.mine .message-file {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px 12px 0 12px;
}

.message-file a {
  color: #FF6B35;
  text-decoration: none;
  font-size: 14px;
}

.message-file a:hover {
  text-decoration: underline;
}

.message-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
}

.message-time-center {
  text-align: center;
  font-size: 12px;
  color: #94A3B8;
  margin: 8px 0;
  padding: 4px 12px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 10px;
  align-self: center;
}

/* 输入区域 */
.chat-input {
  padding: 16px 20px;
  border-top: 1px solid #E2E8F0;
  background: white;
}

.input-toolbar {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.toolbar-btn {
  padding: 6px;
  border: none;
  border-radius: 6px;
  background: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.toolbar-btn:hover {
  background: #F1F5F9;
}

.input-box {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-box textarea {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #E2E8F0;
  border-radius: 12px;
  font-size: 14px;
  resize: none;
  outline: none;
  transition: border-color 0.2s;
  max-height: 120px;
  min-height: 44px;
  font-family: inherit;
}

.input-box textarea:focus {
  border-color: #FF6B35;
}

.btn-send {
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #FF6B35, #FF8E53);
  color: white;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  flex-shrink: 0;
}

.btn-send:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.3);
}

.btn-send:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 空状态 */
.empty-chat {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #94A3B8;
  background: #F8FAFC;
}

.empty-chat svg {
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-chat h3 {
  font-size: 20px;
  color: #64748B;
  margin-bottom: 8px;
}

.empty-chat p {
  font-size: 14px;
}

/* 响应式 */
@media (max-width: 768px) {
  .chat-page {
    padding: 0;
  }

  .chat-layout {
    border-radius: 0;
  }

  .session-panel {
    width: 100%;
  }

  .session-panel.hidden {
    display: none;
  }
}
</style>

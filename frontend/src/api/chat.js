import request from './request'

// 获取聊天会话列表
export const getChatSessions = () => request.get('/chat/sessions')

// 获取或创建与指定用户的会话
export const getOrCreateSession = (targetUserId) => request.post(`/chat/session/${targetUserId}`)

// 获取会话的历史消息
export const getChatMessages = (sessionId, params) => request.get(`/chat/messages/${sessionId}`, { params })

// 发送消息
export const sendChatMessage = (data) => request.post('/chat/send', data)

// 标记消息已读
export const markChatAsRead = (sessionId) => request.put(`/chat/read/${sessionId}`)

// 获取未读消息总数
export const getChatUnreadCount = () => request.get('/chat/unread/count')

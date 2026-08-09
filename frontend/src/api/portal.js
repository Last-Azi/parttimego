import request from './request'

export const getHomeStats = () => request.get('/portal/home')
export const getLatestJobs = () => request.get('/portal/latest-jobs')
export const getCategories = () => request.get('/portal/categories')
export const getHotJobs = (top) => request.get('/job/hot', { params: { top } })

// 新增功能接口
export const getRecommendJobs = () => request.get('/portal/recommend')
export const getHotSearchWords = () => request.get('/portal/hot-search-words')
export const getOnlineCount = () => request.get('/portal/online-count')
export const heartbeat = () => request.get('/portal/heartbeat')
export const getUnreadCount = () => request.get('/message/unread-count')

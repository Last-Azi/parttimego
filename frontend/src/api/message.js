import request from './request'

export const getUnreadCount = () => request.get('/message/unread/count')
export const getMessageList = (params) => request.get('/message/list', { params })
export const markAsRead = (id) => request.put(`/message/${id}/read`)
export const markAllAsRead = () => request.put('/message/read/all')

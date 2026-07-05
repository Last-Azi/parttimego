import request from './request'

export const getStats = () => request.get('/admin/stats')
export const listUsers = (params) => request.get('/admin/users', { params })
export const toggleUserStatus = (id) => request.put(`/admin/users/${id}/toggle`)

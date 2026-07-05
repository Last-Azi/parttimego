import request from './request'

export const searchJobs = (params) => request.get('/job/search', { params })
export const getJobDetail = (id) => request.get(`/job/${id}`)
export const publishJob = (data) => request.post('/job', data)
export const updateJob = (id, data) => request.put(`/job/${id}`, data)
export const offlineJob = (id) => request.put(`/job/${id}/offline`)
export const deleteJob = (id) => request.delete(`/job/${id}`)
export const getMyJobs = (params) => request.get('/job/mine', { params })
export const approveJob = (id) => request.put(`/job/${id}/approve`)
export const rejectJob = (id, reason) => request.put(`/job/${id}/reject`, null, { params: { reason } })
export const adminListJobs = (params) => request.get('/job/admin/list', { params })
export const getHotJobs = (top) => request.get('/job/hot', { params: { top } })

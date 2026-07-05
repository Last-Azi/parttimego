import request from './request'

export const applyJob = (jobId) => request.post(`/application/${jobId}`)
export const withdrawApplication = (id) => request.delete(`/application/${id}`)
export const updateApplicationStatus = (id, status, remark) =>
  request.put(`/application/${id}/status`, null, { params: { status, remark } })
export const getMyApplications = (params) => request.get('/application/mine', { params })
export const getJobApplications = (jobId, params) => request.get(`/application/job/${jobId}`, { params })

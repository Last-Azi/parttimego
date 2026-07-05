import request from './request'

export const addReview = (data) => request.post('/review', data)
export const getJobReviews = (jobId, params) => request.get(`/review/job/${jobId}`, { params })
export const getUserReviews = (userId, params) => request.get(`/review/user/${userId}`, { params })

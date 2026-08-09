import request from './request'

export const createInterview = (data) => request.post('/interview', data)
export const getEmployerInterviews = (params) => request.get('/interview/employer/list', { params })
export const getStudentInterviews = (params) => request.get('/interview/student/list', { params })
export const getInterviewDetail = (id) => request.get(`/interview/${id}`)
export const acceptInterview = (id) => request.put(`/interview/${id}/accept`)
export const rejectInterview = (id, remark) => request.put(`/interview/${id}/reject`, null, { params: { remark } })
export const completeInterview = (id) => request.put(`/interview/${id}/complete`)
export const cancelInterview = (id) => request.put(`/interview/${id}/cancel`)

import request from './request'

export const getEmployerStats = () => request.get('/job/employer/stats')

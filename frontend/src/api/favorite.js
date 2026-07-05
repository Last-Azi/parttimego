import request from './request'

export const addFavorite = (jobId) => request.post(`/favorite/${jobId}`)
export const removeFavorite = (jobId) => request.delete(`/favorite/${jobId}`)
export const checkFavorite = (jobId) => request.get(`/favorite/${jobId}/check`)
export const getMyFavorites = (params) => request.get('/favorite/mine', { params })

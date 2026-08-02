import request from './request'

export const saveResume = (data) => request.post('/resume', data)
export const getMyResume = () => request.get('/resume/mine')
export const getResumeById = (id) => request.get(`/resume/${id}`)
export const parseResume = (fileUrl, fileName) => request.get('/resume/parse', { params: { fileUrl, fileName }, timeout: 30000 })
export const parseResumeFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/resume/parse/file', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 30000
  })
}
export const parseResumeAsync = (fileUrl, fileName) => request.post('/resume/parse/async', null, { params: { fileUrl, fileName } })

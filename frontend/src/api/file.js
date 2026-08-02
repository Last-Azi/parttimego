import request from './request'

export const uploadAvatar = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload/avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export const uploadResume = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload/resume', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 60000
  })
}

export const deleteFile = (id) => request.delete(`/file/${id}`)

import request from '../utils/request'

export function getMe() {
  return request.get('/user/me')
}

export function listUsers() {
  return request.get('/user/list')
}

export function createUser(data) {
  return request.post('/user', data)
}

export function updateUserStatus(id, status) {
  return request.put(`/user/${id}/status`, { status })
}

export function resetPassword(id) {
  return request.put(`/user/${id}/reset-password`)
}

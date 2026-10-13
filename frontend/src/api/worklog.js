import request from '../utils/request'

export function submitWorklog(data) {
  return request.post('/worklog', data)
}

export function myWorklogs() {
  return request.get('/worklog/mine')
}

export function pendingWorklogs(projectId) {
  return request.get('/worklog/pending', { params: { projectId } })
}

export function auditWorklog(data) {
  return request.put('/worklog/audit', data)
}

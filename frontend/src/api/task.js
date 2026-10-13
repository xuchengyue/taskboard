import request from '../utils/request'

export function getBoardTasks(projectId) {
  return request.get('/task/board', { params: { projectId } })
}

export function createTask(data) {
  return request.post('/task', data)
}

export function updateTask(data) {
  return request.put('/task', data)
}

export function deleteTask(id) {
  return request.delete(`/task/${id}`)
}

export function updateTaskStatus(taskId, status) {
  return request.put('/task/status', { taskId, status })
}

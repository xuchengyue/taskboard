import request from '../utils/request'

export function listProjects() {
  return request.get('/project')
}

export function createProject(data) {
  return request.post('/project', data)
}

export function updateProject(data) {
  return request.put('/project', data)
}

export function listProjectMembers(projectId) {
  return request.get(`/project/${projectId}/members`)
}

export function addProjectMember(projectId, userId) {
  return request.post(`/project/${projectId}/members/${userId}`)
}

export function removeProjectMember(projectId, userId) {
  return request.delete(`/project/${projectId}/members/${userId}`)
}

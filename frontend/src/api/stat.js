import request from '../utils/request'

export function memberStats(params) {
  return request.get('/stat/member', { params })
}

export function dailyTrend(params) {
  return request.get('/stat/daily', { params })
}

import { defineStore } from 'pinia'
import { loginApi } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    realName: localStorage.getItem('realName') || '',
    role: localStorage.getItem('role') || ''
  }),

  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN',
    isPM: (state) => state.role === 'PM'
  },

  actions: {
    async login(username, password) {
      const data = await loginApi({ username, password })
      this.token = data.token
      this.userId = data.userId
      this.username = data.username
      this.realName = data.realName
      this.role = data.role

      localStorage.setItem('token', data.token)
      localStorage.setItem('userId', data.userId)
      localStorage.setItem('username', data.username)
      localStorage.setItem('realName', data.realName)
      localStorage.setItem('role', data.role)
    },

    logout() {
      this.token = ''
      this.userId = ''
      this.username = ''
      this.realName = ''
      this.role = ''
      localStorage.clear()
    }
  }
})

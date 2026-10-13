<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../store/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const activeMenu = computed(() => route.path)

const roleLabel = computed(() => {
  const map = { ADMIN: '系统管理员', PM: '项目经理', MEMBER: '团队成员' }
  return map[auth.role] || auth.role
})

function handleLogout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo">敏捷任务面板</div>
      <el-menu :default-active="activeMenu" router class="menu" background-color="#001529" text-color="#c9c9c9"
        active-text-color="#fff">
        <el-menu-item index="/board">
          <span>看板</span>
        </el-menu-item>
        <el-menu-item index="/projects">
          <span>项目管理</span>
        </el-menu-item>
        <el-menu-item index="/worklog">
          <span>工时填报</span>
        </el-menu-item>
        <el-menu-item v-if="auth.role === 'PM' || auth.role === 'ADMIN'" index="/worklog-audit">
          <span>工时审核</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <span>工时统计</span>
        </el-menu-item>
        <el-menu-item v-if="auth.role === 'ADMIN'" index="/users">
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="title">{{ route.meta.title || '' }}</div>
        <div class="user-info">
          <span>{{ auth.realName }}（{{ roleLabel }}）</span>
          <el-button link type="danger" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
}

.aside {
  background: #001529;
}

.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-weight: bold;
  font-size: 15px;
  border-bottom: 1px solid #1c2b3d;
}

.menu {
  border-right: none;
}

.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e6e6e6;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #606266;
}

.main {
  background: #f0f2f5;
}
</style>

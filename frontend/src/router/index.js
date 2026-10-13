import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/layout/Layout.vue'),
    redirect: '/board',
    children: [
      {
        path: 'board',
        name: 'Board',
        component: () => import('../views/board/Board.vue'),
        meta: { title: '敏捷看板' }
      },
      {
        path: 'projects',
        name: 'Projects',
        component: () => import('../views/project/ProjectList.vue'),
        meta: { title: '项目管理' }
      },
      {
        path: 'worklog',
        name: 'Worklog',
        component: () => import('../views/worklog/WorklogSubmit.vue'),
        meta: { title: '工时填报' }
      },
      {
        path: 'worklog-audit',
        name: 'WorklogAudit',
        component: () => import('../views/worklog/WorklogAudit.vue'),
        meta: { title: '工时审核', roles: ['PM', 'ADMIN'] }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('../views/statistics/Statistics.vue'),
        meta: { title: '工时统计' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/user/UserManage.vue'),
        meta: { title: '用户管理', roles: ['ADMIN'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()

  if (to.name !== 'Login' && !auth.isLogin) {
    next('/login')
    return
  }

  if (to.name === 'Login' && auth.isLogin) {
    next('/board')
    return
  }

  if (to.meta && to.meta.roles && !to.meta.roles.includes(auth.role)) {
    next('/board')
    return
  }

  next()
})

export default router

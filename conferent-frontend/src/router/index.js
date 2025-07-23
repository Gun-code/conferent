import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/pages/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/rooms',
    name: 'RoomList',
    component: () => import('@/pages/rooms/index.vue')
  },
  {
    path: '/rooms/create',
    name: 'RoomCreate',
    component: () => import('@/pages/rooms/Create.vue')
  },
  {
    path: '/rooms/:id',
    name: 'RoomDetail',
    component: () => import('@/pages/rooms/Detail.vue')
  },
  {
    path: '/rooms/:id/edit',
    name: 'RoomEdit',
    component: () => import('@/pages/rooms/Edit.vue')
  },
  {
    path: '/reservations',
    name: 'ReservationList',
    component: () => import('@/pages/reservations/index.vue')
  },
  {
    path: '/reservations/create',
    name: 'ReservationCreate',
    component: () => import('@/pages/reservations/Create.vue')
  },
  {
    path: '/reservations/:id',
    name: 'ReservationDetail',
    component: () => import('@/pages/reservations/Detail.vue')
  },
  {
    path: '/users',
    name: 'UserList',
    component: () => import('@/pages/users/index.vue')
  },
  // 관리자 페이지들
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('@/pages/admin/index.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/auth/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/auth/Register.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/NotFound.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 네비게이션 가드
router.beforeEach((to, from, next) => {
  // 인증이 필요한 페이지 체크
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  const isAuthenticated = localStorage.getItem('token') // 실제로는 더 복잡한 인증 로직 필요
  const isAdmin = localStorage.getItem('userRole') === 'ADMIN' // 실제로는 더 복잡한 권한 체크 필요
  
  if (requiresAuth && !isAuthenticated) {
    next('/login')
  } else if (requiresAdmin && !isAdmin) {
    next('/') // 관리자가 아닌 경우 홈으로 리다이렉트
  } else {
    next()
  }
})

export default router 
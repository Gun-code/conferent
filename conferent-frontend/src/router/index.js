import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/authStore.js'
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
    component: () => import('@/pages/rooms/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/rooms/create',
    name: 'RoomCreate',
    component: () => import('@/pages/rooms/Create.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/rooms/:id',
    name: 'RoomDetail',
    component: () => import('@/pages/rooms/Detail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/rooms/:id/edit',
    name: 'RoomEdit',
    component: () => import('@/pages/rooms/Edit.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/reservations',
    name: 'ReservationList',
    component: () => import('@/pages/reservations/index.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/reservations/create',
    name: 'ReservationCreate',
    component: () => import('@/pages/reservations/Create.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/reservations/:id',
    name: 'ReservationDetail',
    component: () => import('@/pages/reservations/Detail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/users',
    name: 'UserList',
    component: () => import('@/pages/users/index.vue'),
    meta: { requiresAuth: true, requiresAdmin: true }
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
    component: () => import('@/pages/auth/Login.vue'),
    meta: { guestOnly: true } // 이미 로그인한 사용자는 접근 불가
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/auth/Register.vue'),
    meta: { guestOnly: true } // 이미 로그인한 사용자는 접근 불가
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
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  
  // 라우트 메타 정보 확인
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresAdmin = to.matched.some(record => record.meta.requiresAdmin)
  const guestOnly = to.matched.some(record => record.meta.guestOnly)
  
  // 토큰이 있지만 인증 상태가 아닌 경우 토큰 검증 시도
  if (!authStore.isAuthenticated && localStorage.getItem('token')) {
    try {
      await authStore.restoreUserFromToken()
    } catch (error) {
      console.error('Token validation failed during navigation:', error)
    }
  }
  
  // 인증 상태 확인
  const isAuthenticated = authStore.isAuthenticated
  const isAdmin = authStore.isAdmin
  
  if (guestOnly && isAuthenticated) {
    // 이미 로그인한 사용자가 로그인/회원가입 페이지에 접근하려는 경우
    next('/')
  } else if (requiresAuth && !isAuthenticated) {
    // 인증이 필요한 페이지에 비로그인 사용자가 접근하려는 경우
    next('/login')
  } else if (requiresAdmin && !isAdmin) {
    // 관리자 권한이 필요한 페이지에 일반 사용자가 접근하려는 경우
    next('/')
  } else {
    // 모든 조건을 만족하는 경우
    next()
  }
})

export default router 
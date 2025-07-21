import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'
import RoomsView from '../views/RoomList.vue'
import CreateReservationView from '../views/CreateReservation.vue'
import MyReservationsView from '../views/MyReservations.vue'
import AdminPanelView from '../views/AdminPanel.vue'

/**
 * Vue Router 설정
 * 애플리케이션의 라우팅을 관리합니다.
 */
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        title: '홈'
      }
    },
    {
      path: '/rooms',
      name: 'rooms',
      component: RoomsView,
      meta: {
        title: '회의실 목록'
      }
    },
    {
      path: '/reservations/create',
      name: 'create-reservation',
      component: CreateReservationView,
      meta: {
        title: '예약 생성'
      }
    },
    {
      path: '/my-reservations',
      name: 'my-reservations',
      component: MyReservationsView,
      meta: {
        title: '내 예약'
      }
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminPanelView,
      meta: {
        title: '관리자 패널'
      }
    }
  ]
})

/**
 * 라우터 전역 가드 - 페이지 제목 설정
 */
router.beforeEach((to, from, next) => {
  // 페이지 제목 설정
  if (to.meta?.title) {
    document.title = `${to.meta.title} - Conferent`
  } else {
    document.title = 'Conferent - 회의실 예약 시스템'
  }
  
  next()
})

export default router 
/**
 * 라우트 이름 상수
 * 라우트 이름을 중앙에서 관리하여 오타를 방지합니다.
 */

export const ROUTE_NAMES = {
  // 홈
  HOME: 'Home',
  
  // 회의실 관련
  ROOM_LIST: 'RoomList',
  ROOM_CREATE: 'RoomCreate',
  ROOM_DETAIL: 'RoomDetail',
  ROOM_EDIT: 'RoomEdit',
  
  // 예약 관련
  RESERVATION_LIST: 'ReservationList',
  RESERVATION_CREATE: 'ReservationCreate',
  RESERVATION_DETAIL: 'ReservationDetail',
  
  // 사용자 관련
  USER_LIST: 'UserList',
  USER_CREATE: 'UserCreate',
  USER_DETAIL: 'UserDetail',
  USER_EDIT: 'UserEdit',
  
  // 인증 관련
  LOGIN: 'Login',
  REGISTER: 'Register',
  
  // 관리자
  ADMIN_PANEL: 'AdminPanel',
  
  // 기타
  NOT_FOUND: 'NotFound'
}

/**
 * 라우트 경로 상수
 */
export const ROUTE_PATHS = {
  HOME: '/',
  ROOM_LIST: '/rooms',
  ROOM_CREATE: '/rooms/create',
  ROOM_DETAIL: '/rooms/:id',
  ROOM_EDIT: '/rooms/:id/edit',
  RESERVATION_LIST: '/reservations',
  RESERVATION_CREATE: '/reservations/create',
  RESERVATION_DETAIL: '/reservations/:id',
  USER_LIST: '/users',
  USER_CREATE: '/users/create',
  USER_DETAIL: '/users/:id',
  USER_EDIT: '/users/:id/edit',
  LOGIN: '/login',
  REGISTER: '/register',
  ADMIN_PANEL: '/admin',
  NOT_FOUND: '/:pathMatch(.*)*'
}

/**
 * 라우트 메타 정보
 */
export const ROUTE_META = {
  REQUIRES_AUTH: 'requiresAuth',
  REQUIRES_ADMIN: 'requiresAdmin',
  TITLE: 'title',
  DESCRIPTION: 'description'
}

/**
 * 라우트별 메타데이터
 */
export const ROUTE_METADATA = {
  [ROUTE_NAMES.HOME]: {
    title: '홈 - Conferent',
    description: '회의실 예약 관리 시스템'
  },
  [ROUTE_NAMES.ROOM_LIST]: {
    title: '회의실 목록 - Conferent',
    description: '사용 가능한 회의실 목록을 확인하세요'
  },
  [ROUTE_NAMES.ROOM_CREATE]: {
    title: '회의실 추가 - Conferent',
    description: '새로운 회의실을 추가하세요'
  },
  [ROUTE_NAMES.RESERVATION_LIST]: {
    title: '예약 목록 - Conferent',
    description: '내 예약 목록을 확인하세요'
  },
  [ROUTE_NAMES.RESERVATION_CREATE]: {
    title: '예약 생성 - Conferent',
    description: '새로운 예약을 생성하세요'
  },
  [ROUTE_NAMES.ADMIN_PANEL]: {
    title: '관리자 패널 - Conferent',
    description: '시스템 관리'
  },
  [ROUTE_NAMES.LOGIN]: {
    title: '로그인 - Conferent',
    description: '계정에 로그인하세요'
  },
  [ROUTE_NAMES.REGISTER]: {
    title: '회원가입 - Conferent',
    description: '새로운 계정을 만드세요'
  }
} 
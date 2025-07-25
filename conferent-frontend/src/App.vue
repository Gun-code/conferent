<template>
  <div id="app">
    <!-- 역할에 따른 동적 레이아웃 -->
    <component :is="currentLayout">
      <router-view />
    </component>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/authStore.js'
import AdminLayout from '@/layouts/AdminLayout.vue'
import UserLayout from '@/layouts/UserLayout.vue'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

export default {
  name: 'App',
  components: {
    AdminLayout,
    UserLayout,
    DefaultLayout
  },
  computed: {
    authStore() {
      return useAuthStore()
    },
    currentLayout() {
      // 인증되지 않은 사용자나 인증 페이지 - DefaultLayout
      if (!this.authStore.isAuthenticated || this.isAuthPage) {
        return 'DefaultLayout'
      }
      
      // 관리자 - AdminLayout
      if (this.authStore.isAdmin) {
        return 'AdminLayout'
      }
      
      // 일반 사용자 - UserLayout
      return 'UserLayout'
    },
    isAuthPage() {
      // 로그인/회원가입 페이지인지 확인
      return ['/login', '/register'].includes(this.$route.path)
    }
  },
  async created() {
    // 앱 시작 시 토큰에서 사용자 정보 복원
    const authStore = useAuthStore()
    try {
      await authStore.restoreUserFromToken()
    } catch (error) {
      console.warn('Failed to restore user from token:', error)
    }
  }
}
</script>

<style>
/* 전역 스타일 */
* {
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f9fafb;
  color: #1f2937;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 스크롤바 스타일링 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 포커스 스타일 */
:focus {
  outline: 2px solid #3b82f6;
  outline-offset: 2px;
}

/* 링크 스타일 */
a {
  color: #3b82f6;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* 버튼 기본 스타일 리셋 */
button {
  font-family: inherit;
}

/* 입력 필드 기본 스타일 */
input, textarea, select {
  font-family: inherit;
  font-size: inherit;
}

/* 유틸리티 클래스 */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

.text-center {
  text-align: center;
}

.text-left {
  text-align: left;
}

.text-right {
  text-align: right;
}

.flex {
  display: flex;
}

.flex-col {
  flex-direction: column;
}

.items-center {
  align-items: center;
}

.justify-center {
  justify-content: center;
}

.justify-between {
  justify-content: space-between;
}

.gap-1 {
  gap: 0.25rem;
}

.gap-2 {
  gap: 0.5rem;
}

.gap-4 {
  gap: 1rem;
}

.mt-4 {
  margin-top: 1rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

.p-4 {
  padding: 1rem;
}

.rounded {
  border-radius: 0.375rem;
}

.shadow {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.bg-white {
  background-color: #ffffff;
}

.border {
  border: 1px solid #e5e7eb;
}
</style> 
<template>
  <header class="header">
    <div class="header__container">
      <div class="header__logo">
        <router-link to="/" class="header__logo-link">
          <h1 class="header__title">Conferent</h1>
        </router-link>
      </div>
      
      <nav class="header__nav">
        <ul class="header__nav-list">
          <li class="header__nav-item">
            <router-link to="/" class="header__nav-link">홈</router-link>
          </li>
          <li v-if="authStore.isAuthenticated" class="header__nav-item">
            <router-link to="/rooms" class="header__nav-link">회의실</router-link>
          </li>
          <li v-if="authStore.isAuthenticated" class="header__nav-item">
            <router-link to="/reservations" class="header__nav-link">예약</router-link>
          </li>
          <li v-if="authStore.isAdmin" class="header__nav-item">
            <router-link to="/admin" class="header__nav-link">관리</router-link>
          </li>
        </ul>
      </nav>

      <div class="header__user">
        <div v-if="authStore.isAuthenticated" class="header__user-info">
          <span class="header__user-name">{{ authStore.userName }}</span>
          <BaseButton 
            variant="secondary" 
            size="small" 
            @click="handleLogout"
          >
            로그아웃
          </BaseButton>
        </div>
        <div v-else class="header__auth">
          <BaseButton 
            variant="primary" 
            size="small" 
            @click="handleLogin"
          >
            로그인
          </BaseButton>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import BaseButton from '@/components/base/BaseButton.vue'
import { useAuthStore } from '@/store/authStore.js'

export default {
  name: 'Header',
  components: {
    BaseButton
  },
  computed: {
    authStore() {
      return useAuthStore()
    }
  },
  methods: {
    handleLogin() {
      // 로그인 페이지로 이동
      this.$router.push('/login')
    },
    async handleLogout() {
      try {
        // authStore의 logout 액션 사용
        await this.authStore.logout()
        
        // 홈으로 이동
        this.$router.push('/')
      } catch (error) {
        console.error('Logout failed:', error)
        alert('로그아웃에 실패했습니다.')
      }
    }
  }
}
</script>

<style scoped>
.header {
  background-color: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header__container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 4rem;
}

.header__logo-link {
  text-decoration: none;
  color: inherit;
}

.header__title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.header__nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 2rem;
}

.header__nav-link {
  text-decoration: none;
  color: #6b7280;
  font-weight: 500;
  transition: color 0.2s ease-in-out;
}

.header__nav-link:hover,
.header__nav-link.router-link-active {
  color: #3b82f6;
}

.header__user {
  display: flex;
  align-items: center;
}

.header__user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.header__user-name {
  font-weight: 500;
  color: #374151;
}

.header__auth {
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .header__nav {
    display: none;
  }
  
  .header__container {
    padding: 0 0.5rem;
  }
}
</style> 
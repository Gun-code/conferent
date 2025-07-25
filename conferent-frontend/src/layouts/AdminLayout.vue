<template>
  <div class="admin-layout">
    <!-- 사이드바 -->
    <aside class="admin-sidebar" :class="{ 'admin-sidebar--collapsed': sidebarCollapsed }">
      <div class="admin-sidebar__header">
        <h1 class="admin-sidebar__title">Conferent</h1>
        <button 
          class="admin-sidebar__toggle"
          @click="toggleSidebar"
        >
          <span v-if="sidebarCollapsed">☰</span>
          <span v-else>✕</span>
        </button>
      </div>
      
      <nav class="admin-sidebar__nav">
        <ul class="admin-sidebar__menu">
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">📊</span>
              <span class="admin-sidebar__menu-text">대시보드</span>
            </router-link>
          </li>
          
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin/rooms" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">🏢</span>
              <span class="admin-sidebar__menu-text">회의실 관리</span>
            </router-link>
          </li>
          
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin/reservations" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">📅</span>
              <span class="admin-sidebar__menu-text">예약 관리</span>
            </router-link>
          </li>
          
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin/users" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">👥</span>
              <span class="admin-sidebar__menu-text">사용자 관리</span>
            </router-link>
          </li>
          
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin/reports" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">📈</span>
              <span class="admin-sidebar__menu-text">통계 리포트</span>
            </router-link>
          </li>
          
          <li class="admin-sidebar__menu-item">
            <router-link 
              to="/admin/settings" 
              class="admin-sidebar__menu-link"
              active-class="admin-sidebar__menu-link--active"
            >
              <span class="admin-sidebar__menu-icon">⚙️</span>
              <span class="admin-sidebar__menu-text">시스템 설정</span>
            </router-link>
          </li>
        </ul>
      </nav>
    </aside>

    <!-- 메인 콘텐츠 -->
    <main class="admin-main">
      <!-- 상단 헤더 -->
      <header class="admin-header">
        <div class="admin-header__left">
          <button 
            class="admin-header__menu-toggle"
            @click="toggleSidebar"
          >
            ☰
          </button>
          <h2 class="admin-header__title">{{ pageTitle }}</h2>
        </div>
        
        <div class="admin-header__right">
          <!-- <div class="admin-header__notifications">
            <button class="admin-header__notification-btn">
              🔔
              <span class="admin-header__notification-badge">3</span>
            </button>
          </div>
           -->
          <div class="admin-header__user">
            <div class="admin-header__user-info">
              <span class="admin-header__user-name">{{ userName }}</span>
              <span class="admin-header__user-role">{{ userRole }}</span>
            </div>
            <div class="admin-header__user-avatar">
              👤
            </div>
            <div class="admin-header__user-menu">
              <button 
                class="admin-header__user-menu-btn" 
                @click="toggleUserMenu"
                :class="{ 'admin-header__user-menu-btn--active': isUserMenuOpen }"
              >
                ▼
              </button>
              <div 
                v-show="isUserMenuOpen"
                class="admin-header__user-dropdown"
              >
                <a href="#" class="admin-header__user-dropdown-item">프로필</a>
                <a href="#" class="admin-header__user-dropdown-item">설정</a>
                <a href="#" class="admin-header__user-dropdown-item" @click="handleLogout">로그아웃</a>
              </div>
            </div>
          </div>
        </div>
      </header>

      <!-- 페이지 콘텐츠 -->
      <div class="admin-content">
        <slot></slot>
      </div>
    </main>
  </div>
</template>

<script>
import { useAuthStore } from '@/store/authStore.js'

export default {
  name: 'AdminLayout',
  data() {
    return {
      sidebarCollapsed: false,
      isUserMenuOpen: false
    }
  },
  computed: {
    authStore() {
      return useAuthStore()
    },
    userName() {
      return this.authStore.userName || '관리자'
    },
    userRole() {
      return this.authStore.userRole === 'ADMIN' ? '시스템 관리자' : '사용자'
    },
    pageTitle() {
      const routeNames = {
        '/admin': '대시보드',
        '/admin/rooms': '회의실 관리',
        '/admin/reservations': '예약 관리',
        '/admin/users': '사용자 관리',
        '/admin/reports': '통계 리포트',
        '/admin/settings': '시스템 설정'
      }
      return routeNames[this.$route.path] || '관리자 패널'
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    
    toggleUserMenu() {
      this.isUserMenuOpen = !this.isUserMenuOpen
    },
    
    async handleLogout() {
      try {
        await this.authStore.logout()
        this.isUserMenuOpen = false // 드롭다운 닫기
        this.$router.push('/login')
      } catch (error) {
        console.error('Logout failed:', error)
        alert('로그아웃에 실패했습니다.')
      }
    },
    
    handleOutsideClick(event) {
      // 드롭다운 외부 클릭 시 닫기
      const userMenu = this.$el.querySelector('.admin-header__user-menu')
      if (userMenu && !userMenu.contains(event.target)) {
        this.isUserMenuOpen = false
      }
    }
  },
  
  mounted() {
    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', this.handleOutsideClick)
  },
  
  beforeDestroy() {
    // 이벤트 리스너 제거
    document.removeEventListener('click', this.handleOutsideClick)
  }
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f8fafc;
}

.admin-sidebar {
  width: 280px;
  background-color: #1e293b;
  color: white;
  transition: width 0.3s ease;
  overflow: hidden;
}

.admin-sidebar--collapsed {
  width: 70px;
}

.admin-sidebar__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem;
  border-bottom: 1px solid #334155;
}

.admin-sidebar__title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  white-space: nowrap;
}

.admin-sidebar__toggle {
  background: none;
  border: none;
  color: white;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.admin-sidebar__toggle:hover {
  background-color: #334155;
}

.admin-sidebar__nav {
  padding: 1rem 0;
}

.admin-sidebar__menu {
  list-style: none;
  margin: 0;
  padding: 0;
}

.admin-sidebar__menu-item {
  margin: 0.25rem 0;
}

.admin-sidebar__menu-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: #cbd5e1;
  text-decoration: none;
  transition: all 0.2s;
  white-space: nowrap;
}

.admin-sidebar__menu-link:hover {
  background-color: #334155;
  color: white;
}

.admin-sidebar__menu-link--active {
  background-color: #3b82f6;
  color: white;
}

.admin-sidebar__menu-icon {
  font-size: 1.25rem;
  margin-right: 0.75rem;
  min-width: 1.5rem;
  text-align: center;
}

.admin-sidebar__menu-text {
  font-weight: 500;
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background-color: white;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}

.admin-header__left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.admin-header__menu-toggle {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
  transition: background-color 0.2s;
}

.admin-header__menu-toggle:hover {
  background-color: #f1f5f9;
}

.admin-header__title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.admin-header__right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.admin-header__notifications {
  position: relative;
}

.admin-header__notification-btn {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
  position: relative;
  transition: background-color 0.2s;
}

.admin-header__notification-btn:hover {
  background-color: #f1f5f9;
}

.admin-header__notification-badge {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #ef4444;
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.125rem 0.375rem;
  border-radius: 9999px;
  min-width: 1.25rem;
  text-align: center;
}

.admin-header__user {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  position: relative;
}

.admin-header__user-info {
  text-align: right;
}

.admin-header__user-name {
  display: block;
  font-weight: 600;
  color: #1e293b;
  font-size: 0.875rem;
}

.admin-header__user-role {
  display: block;
  color: #64748b;
  font-size: 0.75rem;
}

.admin-header__user-avatar {
  font-size: 1.5rem;
  width: 2.5rem;
  height: 2.5rem;
  background-color: #e2e8f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.admin-header__user-menu {
  position: relative;
}

.admin-header__user-menu-btn {
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s;
}

.admin-header__user-menu-btn:hover {
  background-color: #f1f5f9;
}

.admin-header__user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  min-width: 150px;
  z-index: 10;
  display: none;
}

.admin-header__user-menu-btn--active {
  background-color: #e5e7eb;
  color: #374151;
}

.admin-header__user-dropdown {
  display: none;
}

.admin-header__user-dropdown[v-show="true"] {
  display: block;
}

.admin-header__user-dropdown-item {
  display: block;
  padding: 0.75rem 1rem;
  color: #374151;
  text-decoration: none;
  transition: background-color 0.2s;
}

.admin-header__user-dropdown-item:hover {
  background-color: #f9fafb;
}

.admin-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .admin-sidebar {
    position: fixed;
    top: 0;
    left: 0;
    height: 100vh;
    z-index: 50;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
  }
  
  .admin-sidebar--collapsed {
    transform: translateX(0);
  }
  
  .admin-header {
    padding: 1rem;
  }
  
  .admin-header__user-info {
    display: none;
  }
  
  .admin-content {
    padding: 1rem;
  }
}
</style> 
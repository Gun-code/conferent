<template>
  <div class="user-layout">
    <!-- 상단 헤더 -->
    <header class="user-header">
      <div class="user-header__container">
        <div class="user-header__left">
          <router-link to="/" class="user-header__logo">
            <h1 class="user-header__title">Conferent</h1>
          </router-link>
        </div>
        
        <nav class="user-header__nav">
          <ul class="user-header__menu">
            <li class="user-header__menu-item">
              <router-link 
                to="/" 
                class="user-header__menu-link"
                active-class="user-header__menu-link--active"
              >
                홈
              </router-link>
            </li>
            <li class="user-header__menu-item">
              <router-link 
                to="/rooms" 
                class="user-header__menu-link"
                active-class="user-header__menu-link--active"
              >
                회의실
              </router-link>
            </li>
            <li class="user-header__menu-item">
              <router-link 
                to="/reservations" 
                class="user-header__menu-link"
                active-class="user-header__menu-link--active"
              >
                내 예약
              </router-link>
            </li>
          </ul>
        </nav>
        
        <div class="user-header__right">
          <div class="user-header__search">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="회의실 검색..."
              class="user-header__search-input"
              @keyup.enter="handleSearch"
            />
            <button 
              class="user-header__search-btn"
              @click="handleSearch"
            >
              🔍
            </button>
          </div>
          
          <div v-if="isAuthenticated" class="user-header__user">
            <div class="user-header__notifications">
              <button class="user-header__notification-btn">
                🔔
                <span v-if="notificationCount > 0" class="user-header__notification-badge">
                  {{ notificationCount }}
                </span>
              </button>
            </div>
            
            <div class="user-header__user-menu">
              <button class="user-header__user-menu-btn">
                <span class="user-header__user-avatar">👤</span>
                <span class="user-header__user-name">{{ userName }}</span>
                <span class="user-header__user-arrow">▼</span>
              </button>
              <div class="user-header__user-dropdown">
                <router-link to="/profile" class="user-header__user-dropdown-item">
                  프로필
                </router-link>
                <router-link to="/settings" class="user-header__user-dropdown-item">
                  설정
                </router-link>
                <a href="#" class="user-header__user-dropdown-item" @click="handleLogout">
                  로그아웃
                </a>
              </div>
            </div>
          </div>
          
          <div v-else class="user-header__auth">
            <router-link 
              to="/login" 
              class="user-header__auth-btn user-header__auth-btn--login"
            >
              로그인
            </router-link>
            <router-link 
              to="/register" 
              class="user-header__auth-btn user-header__auth-btn--register"
            >
              회원가입
            </router-link>
          </div>
        </div>
      </div>
    </header>

    <!-- 메인 콘텐츠 -->
    <main class="user-main">
      <slot></slot>
    </main>

    <!-- 하단 푸터 -->
    <footer class="user-footer">
      <div class="user-footer__container">
        <div class="user-footer__content">
          <div class="user-footer__section">
            <h3 class="user-footer__title">Conferent</h3>
            <p class="user-footer__description">
              효율적인 회의실 예약 관리 시스템으로 더 나은 업무 환경을 제공합니다.
            </p>
          </div>
          
          <div class="user-footer__section">
            <h4 class="user-footer__subtitle">서비스</h4>
            <ul class="user-footer__links">
              <li><router-link to="/rooms" class="user-footer__link">회의실 예약</router-link></li>
              <li><router-link to="/reservations" class="user-footer__link">예약 관리</router-link></li>
              <li><a href="#" class="user-footer__link">사용 가이드</a></li>
              <li><a href="#" class="user-footer__link">FAQ</a></li>
            </ul>
          </div>
          
          <div class="user-footer__section">
            <h4 class="user-footer__subtitle">지원</h4>
            <ul class="user-footer__links">
              <li><a href="#" class="user-footer__link">고객센터</a></li>
              <li><a href="#" class="user-footer__link">문의하기</a></li>
              <li><a href="#" class="user-footer__link">개인정보처리방침</a></li>
              <li><a href="#" class="user-footer__link">이용약관</a></li>
            </ul>
          </div>
          
          <div class="user-footer__section">
            <h4 class="user-footer__subtitle">연락처</h4>
            <div class="user-footer__contact">
              <p>📧 support@conferent.com</p>
              <p>📞 02-1234-5678</p>
              <p>📍 서울특별시 동작구 노량진로 188</p>
            </div>
          </div>
        </div>
        
        <div class="user-footer__bottom">
          <p class="user-footer__copyright">
            © 2024 Conferent. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>

export default {
  name: 'UserLayout',
  data() {
    return {
      searchQuery: '',
      isAuthenticated: false,
      userName: '사용자',
      notificationCount: 2
    }
  },
  mounted() {
    // 인증 상태 확인
    this.checkAuthStatus()
  },
  methods: {
    checkAuthStatus() {
      const token = localStorage.getItem('token')
      this.isAuthenticated = !!token
      
      if (this.isAuthenticated) {
        // 실제로는 API에서 사용자 정보를 가져와야 함
        this.userName = '홍길동'
      }
    },
    
    handleSearch() {
      if (this.searchQuery.trim()) {
        this.$router.push({
          path: '/rooms',
          query: { search: this.searchQuery }
        })
      }
    },
    
    handleLogout() {
      localStorage.removeItem('token')
      this.isAuthenticated = false
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.user-header {
  background-color: white;
  border-bottom: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 40;
}

.user-header__container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 4rem;
}

.user-header__left {
  display: flex;
  align-items: center;
}

.user-header__logo {
  text-decoration: none;
}

.user-header__title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #3b82f6;
  margin: 0;
}

.user-header__nav {
  display: flex;
  align-items: center;
}

.user-header__menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 2rem;
}

.user-header__menu-link {
  color: #374151;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 0;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.user-header__menu-link:hover {
  color: #3b82f6;
}

.user-header__menu-link--active {
  color: #3b82f6;
  border-bottom-color: #3b82f6;
}

.user-header__right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-header__search {
  display: flex;
  align-items: center;
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  overflow: hidden;
}

.user-header__search-input {
  border: none;
  background: none;
  padding: 0.5rem 0.75rem;
  width: 200px;
  font-size: 0.875rem;
}

.user-header__search-input:focus {
  outline: none;
}

.user-header__search-btn {
  background: none;
  border: none;
  padding: 0.5rem 0.75rem;
  cursor: pointer;
  color: #6b7280;
  transition: color 0.2s;
}

.user-header__search-btn:hover {
  color: #3b82f6;
}

.user-header__notifications {
  position: relative;
}

.user-header__notification-btn {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.375rem;
  position: relative;
  transition: background-color 0.2s;
}

.user-header__notification-btn:hover {
  background-color: #f1f5f9;
}

.user-header__notification-badge {
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

.user-header__user-menu {
  position: relative;
}

.user-header__user-menu-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: none;
  border: none;
  padding: 0.5rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.user-header__user-menu-btn:hover {
  background-color: #f1f5f9;
}

.user-header__user-avatar {
  font-size: 1.25rem;
  width: 2rem;
  height: 2rem;
  background-color: #e2e8f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-header__user-name {
  font-weight: 500;
  color: #374151;
}

.user-header__user-arrow {
  color: #6b7280;
  font-size: 0.75rem;
}

.user-header__user-dropdown {
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

.user-header__user-menu:hover .user-header__user-dropdown {
  display: block;
}

.user-header__user-dropdown-item {
  display: block;
  padding: 0.75rem 1rem;
  color: #374151;
  text-decoration: none;
  transition: background-color 0.2s;
}

.user-header__user-dropdown-item:hover {
  background-color: #f9fafb;
}

.user-header__auth {
  display: flex;
  gap: 0.5rem;
}

.user-header__auth-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.2s;
}

.user-header__auth-btn--login {
  color: #3b82f6;
  border: 1px solid #3b82f6;
}

.user-header__auth-btn--login:hover {
  background-color: #3b82f6;
  color: white;
}

.user-header__auth-btn--register {
  background-color: #3b82f6;
  color: white;
  border: 1px solid #3b82f6;
}

.user-header__auth-btn--register:hover {
  background-color: #2563eb;
  border-color: #2563eb;
}

.user-main {
  flex: 1;
}

.user-footer {
  background-color: #1f2937;
  color: white;
  margin-top: auto;
}

.user-footer__container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 1rem 1rem;
}

.user-footer__content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.user-footer__title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  color: #3b82f6;
}

.user-footer__description {
  color: #d1d5db;
  line-height: 1.6;
  margin: 0;
}

.user-footer__subtitle {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0 0 1rem 0;
  color: #f9fafb;
}

.user-footer__links {
  list-style: none;
  margin: 0;
  padding: 0;
}

.user-footer__links li {
  margin-bottom: 0.5rem;
}

.user-footer__link {
  color: #d1d5db;
  text-decoration: none;
  transition: color 0.2s;
}

.user-footer__link:hover {
  color: #3b82f6;
}

.user-footer__contact p {
  color: #d1d5db;
  margin: 0.5rem 0;
}

.user-footer__bottom {
  border-top: 1px solid #374151;
  padding-top: 1rem;
  text-align: center;
}

.user-footer__copyright {
  color: #9ca3af;
  margin: 0;
  font-size: 0.875rem;
}

@media (max-width: 768px) {
  .user-header__container {
    flex-wrap: wrap;
    height: auto;
    padding: 1rem;
    gap: 1rem;
  }
  
  .user-header__nav {
    order: 3;
    width: 100%;
  }
  
  .user-header__menu {
    justify-content: center;
    gap: 1rem;
  }
  
  .user-header__search {
    order: 2;
  }
  
  .user-header__search-input {
    width: 150px;
  }
  
  .user-footer__content {
    grid-template-columns: 1fr;
    text-align: center;
  }
}
</style> 
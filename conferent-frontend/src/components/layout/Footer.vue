<template>
  <footer class="footer">
    <div class="footer__container">
      <div class="footer__content">
        <div class="footer__section">
          <h3 class="footer__title">Conferent</h3>
          <p class="footer__description">
            효율적인 회의실 예약 관리 시스템
          </p>
        </div>
        
        <div class="footer__section">
          <h4 class="footer__subtitle">서비스</h4>
          <ul class="footer__list">
            <li><router-link to="/rooms" class="footer__link">회의실 예약</router-link></li>
            <li><router-link to="/reservations" class="footer__link">예약 관리</router-link></li>
            <li><router-link to="/admin" class="footer__link">관리자</router-link></li>
          </ul>
        </div>
        
        <div class="footer__section">
          <h4 class="footer__subtitle">지원</h4>
          <ul class="footer__list">
            <li><a href="#" class="footer__link">도움말</a></li>
            <li><a href="#" class="footer__link">문의하기</a></li>
            <li><a href="#" class="footer__link">FAQ</a></li>
          </ul>
        </div>
        
        <div class="footer__section">
          <h4 class="footer__subtitle">연락처</h4>
          <ul class="footer__list">
            <li>이메일: support@conferent.com</li>
            <li>전화: 02-1234-5678</li>
            <li>주소: 서울시 강남구</li>
          </ul>
        </div>
      </div>
      
      <div class="footer__bottom">
        <p class="footer__copyright">
          © {{ currentYear }} Conferent. All rights reserved.
        </p>
        <div class="footer__links">
          <a href="#" class="footer__link">개인정보처리방침</a>
          <a href="#" class="footer__link">이용약관</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
  computed: {
    currentYear() {
      return new Date().getFullYear()
    }
  }
}
</script>

<style scoped>
.footer {
  background-color: #1f2937;
  color: #f9fafb;
  margin-top: auto;
}

.footer__container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 1rem 1rem;
}

.footer__content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer__title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  color: #ffffff;
}

.footer__description {
  color: #d1d5db;
  margin: 0;
  line-height: 1.6;
}

.footer__subtitle {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 0 0 1rem 0;
  color: #ffffff;
}

.footer__list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.footer__list li {
  margin-bottom: 0.5rem;
}

.footer__link {
  color: #d1d5db;
  text-decoration: none;
  transition: color 0.2s ease-in-out;
}

.footer__link:hover {
  color: #ffffff;
}

.footer__bottom {
  border-top: 1px solid #374151;
  padding-top: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.footer__copyright {
  color: #9ca3af;
  margin: 0;
}

.footer__links {
  display: flex;
  gap: 1rem;
}

@media (max-width: 768px) {
  .footer__content {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .footer__bottom {
    flex-direction: column;
    text-align: center;
  }
  
  .footer__container {
    padding: 2rem 0.5rem 1rem;
  }
}
</style> 
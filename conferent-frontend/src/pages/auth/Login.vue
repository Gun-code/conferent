<template>
  <UserLayout>
    <div class="login">
      <div class="login__container">
        <div class="login__card">
          <div class="login__header">
            <h1 class="login__title">로그인</h1>
            <p class="login__subtitle">계정에 로그인하여 회의실을 예약하세요</p>
          </div>

          <form class="login__form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="email" class="form-label">이메일</label>
              <input
                id="email"
                v-model="form.email"
                type="email"
                class="form-input"
                required
                placeholder="이메일을 입력하세요"
              />
            </div>

            <div class="form-group">
              <label for="password" class="form-label">비밀번호</label>
              <input
                id="password"
                v-model="form.password"
                type="password"
                class="form-input"
                required
                placeholder="비밀번호를 입력하세요"
              />
            </div>

            <div class="form-group form-group--checkbox">
              <label class="checkbox-label">
                <input
                  v-model="form.remember"
                  type="checkbox"
                  class="checkbox-input"
                />
                <span class="checkbox-text">로그인 상태 유지</span>
              </label>
            </div>

            <BaseButton 
              type="submit"
              variant="primary" 
              size="large"
              :disabled="loading"
              class="login__submit"
            >
              {{ loading ? '로그인 중...' : '로그인' }}
            </BaseButton>
          </form>

          <div class="login__footer">
            <p class="login__footer-text">
              아직 계정이 없으신가요?
              <router-link to="/register" class="login__footer-link">
                회원가입
              </router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'

export default {
  name: 'Login',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      loading: false,
      form: {
        email: '',
        password: '',
        remember: false
      }
    }
  },
  methods: {
    async handleSubmit() {
      this.loading = true
      
      try {
        // 실제로는 API 호출
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 토큰 저장 (실제로는 API 응답에서 받아옴)
        localStorage.setItem('token', 'mock-token')
        localStorage.setItem('userRole', 'USER')
        
        // 홈으로 이동
        this.$router.push('/')
      } catch (err) {
        alert('로그인에 실패했습니다.')
        console.error('Login failed:', err)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/auth.css';
</style> 
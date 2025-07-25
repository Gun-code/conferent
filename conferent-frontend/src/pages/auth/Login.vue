<template>
  <div class="login">
      <div class="login__container">
        <div class="login__card">
          <div class="login__header">
            <h1 class="login__title">로그인</h1>
            <p class="login__subtitle">계정에 로그인하여 회의실을 예약하세요</p>
          </div>

          <!-- 에러 알림 -->
          <BaseAlert
            v-if="showError"
            :show="showError"
            type="error"
            :message="errorMessage"
            @close="clearError"
            class="mb-4"
          />

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
</template>

<script>
import BaseButton from '@/components/base/BaseButton.vue'
import BaseAlert from '@/components/base/BaseAlert.vue'
import { useAuthStore } from '@/store/authStore.js'

export default {
  name: 'Login',
  components: {
    BaseButton,
    BaseAlert
  },
  data() {
    return {
      form: {
        email: '',
        password: '',
        remember: false
      },
      showError: false,
      errorMessage: ''
    }
  },
  computed: {
    authStore() {
      return useAuthStore()
    },
    loading() {
      return this.authStore.loading
    }
  },
  watch: {
    'authStore.error'(newError) {
      if (newError) {
        this.showError = true
        this.errorMessage = newError
      }
    }
  },
  methods: {
    async handleSubmit() {
      // 에러 초기화
      this.showError = false
      this.errorMessage = ''
      this.authStore.clearError()
      
      try {
        // authStore의 login 액션 사용
        await this.authStore.login({
          email: this.form.email,
          password: this.form.password
        })
        
        // 로그인 성공 시 홈으로 이동
        this.$router.push('/')
      } catch (err) {
        // 에러는 authStore에서 처리되므로 여기서는 추가 처리하지 않음
        console.error('Login failed:', err)
      }
    },
    
    clearError() {
      this.showError = false
      this.errorMessage = ''
      this.authStore.clearError()
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/auth.css';
</style> 
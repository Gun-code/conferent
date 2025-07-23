<template>
  <UserLayout>
    <div class="register">
      <div class="register__container">
        <div class="register__card">
          <div class="register__header">
            <h1 class="register__title">회원가입</h1>
            <p class="register__subtitle">새 계정을 만들어 회의실 예약을 시작하세요</p>
          </div>

          <form class="register__form" @submit.prevent="handleSubmit">
            <div class="form-group">
              <label for="name" class="form-label">이름</label>
              <input
                id="name"
                v-model="form.name"
                type="text"
                class="form-input"
                required
                placeholder="이름을 입력하세요"
              />
            </div>

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

            <div class="form-group">
              <label for="confirmPassword" class="form-label">비밀번호 확인</label>
              <input
                id="confirmPassword"
                v-model="form.confirmPassword"
                type="password"
                class="form-input"
                required
                placeholder="비밀번호를 다시 입력하세요"
              />
            </div>

            <div class="form-group form-group--checkbox">
              <label class="checkbox-label">
                <input
                  v-model="form.terms"
                  type="checkbox"
                  class="checkbox-input"
                  required
                />
                <span class="checkbox-text">
                  <a href="#" class="terms-link">이용약관</a> 및 
                  <a href="#" class="terms-link">개인정보처리방침</a>에 동의합니다
                </span>
              </label>
            </div>

            <BaseButton 
              type="submit"
              variant="primary" 
              size="large"
              :disabled="loading"
              class="register__submit"
            >
              {{ loading ? '계정 생성 중...' : '회원가입' }}
            </BaseButton>
          </form>

          <div class="register__footer">
            <p class="register__footer-text">
              이미 계정이 있으신가요?
              <router-link to="/login" class="register__footer-link">
                로그인
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
  name: 'Register',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      loading: false,
      form: {
        name: '',
        email: '',
        password: '',
        confirmPassword: '',
        terms: false
      }
    }
  },
  methods: {
    async handleSubmit() {
      if (this.form.password !== this.form.confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.')
        return
      }

      this.loading = true
      
      try {
        // 실제로는 API 호출
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        // 회원가입 성공 후 로그인 페이지로 이동
        alert('회원가입이 완료되었습니다. 로그인해주세요.')
        this.$router.push('/login')
      } catch (err) {
        alert('회원가입에 실패했습니다.')
        console.error('Register failed:', err)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/auth.css';

.terms-link {
  color: var(--color-primary);
  text-decoration: none;
}

.terms-link:hover {
  text-decoration: underline;
}
</style> 
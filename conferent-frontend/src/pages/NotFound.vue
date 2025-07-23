<template>
  <UserLayout>
    <div class="not-found">
      <div class="not-found__content">
        <h1 class="not-found__title">404</h1>
        <h2 class="not-found__subtitle">페이지를 찾을 수 없습니다</h2>
        <p class="not-found__description">
          요청하신 페이지가 존재하지 않거나 이동되었을 수 있습니다.
        </p>
        <div class="not-found__actions">
          <BaseButton 
            variant="primary" 
            @click="handleGoHome"
          >
            홈으로 돌아가기
          </BaseButton>
          <BaseButton 
            variant="secondary" 
            @click="handleGoBack"
          >
            이전 페이지
          </BaseButton>
        </div>
      </div>
    </div>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'

export default {
  name: 'NotFound',
  components: {
    UserLayout,
    BaseButton
  },
  methods: {
    handleGoHome() {
      this.$router.push('/')
    },
    
    handleGoBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  text-align: center;
}

.not-found__content {
  max-width: 500px;
  padding: 2rem;
}

.not-found__title {
  font-size: 6rem;
  font-weight: 700;
  color: #3b82f6;
  margin: 0 0 1rem 0;
  line-height: 1;
}

.not-found__subtitle {
  font-size: 2rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 1rem 0;
}

.not-found__description {
  font-size: 1.125rem;
  color: #6b7280;
  margin: 0 0 2rem 0;
  line-height: 1.6;
}

.not-found__actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .not-found__title {
    font-size: 4rem;
  }
  
  .not-found__subtitle {
    font-size: 1.5rem;
  }
  
  .not-found__actions {
    flex-direction: column;
    align-items: center;
  }
}
</style> 
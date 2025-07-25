<template>
  <DefaultLayout>
    <div class="room-create">
      <div class="room-create__header">
        <h1 class="room-create__title">회의실 추가</h1>
      </div>

      <div class="room-create__form">
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="name" class="form-label">회의실 이름 *</label>
            <input
              id="name"
              v-model="form.name"
              type="text"
              class="form-input"
              required
              placeholder="회의실 이름을 입력하세요"
            />
          </div>

          <div class="form-group">
            <label for="location" class="form-label">위치 *</label>
            <input
              id="location"
              v-model="form.location"
              type="text"
              class="form-input"
              required
              placeholder="예: 3층, 4층"
            />
          </div>

          <div class="form-group">
            <label for="capacity" class="form-label">수용 인원 *</label>
            <input
              id="capacity"
              v-model.number="form.capacity"
              type="number"
              class="form-input"
              required
              min="1"
              placeholder="수용 인원을 입력하세요"
            />
          </div>

          <div class="form-group">
            <label for="description" class="form-label">설명</label>
            <textarea
              id="description"
              v-model="form.description"
              class="form-textarea"
              rows="4"
              placeholder="회의실에 대한 설명을 입력하세요"
            ></textarea>
          </div>

          <div class="form-actions">
            <BaseButton 
              type="button"
              variant="secondary" 
              @click="handleCancel"
            >
              취소
            </BaseButton>
            <BaseButton 
              type="submit"
              variant="primary" 
              :disabled="loading"
            >
              {{ loading ? '생성 중...' : '회의실 추가' }}
            </BaseButton>
          </div>
        </form>
      </div>
    </div>
  </DefaultLayout>
</template>

<script>
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import { roomApiClient } from '@/api'

export default {
  name: 'RoomCreate',
  components: {
    DefaultLayout,
    BaseButton
  },
  data() {
    return {
      loading: false,
      form: {
        name: '',
        location: '',
        capacity: '',
        description: ''
      }
    }
  },
  methods: {
    async handleSubmit() {
      this.loading = true
      
      try {
        await roomApiClient.create(this.form)
        this.$router.push('/rooms')
      } catch (err) {
        alert('회의실 생성에 실패했습니다.')
        console.error('Failed to create room:', err)
      } finally {
        this.loading = false
      }
    },
    
    handleCancel() {
      this.$router.push('/rooms')
    }
  }
}
</script>

<style scoped>
.room-create {
  max-width: 600px;
  margin: 0 auto;
}

.room-create__header {
  margin-bottom: 2rem;
}

.room-create__title {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.room-create__form {
  background: white;
  padding: 2rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  font-weight: 500;
  color: #374151;
  margin-bottom: 0.5rem;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 1rem;
  transition: border-color 0.2s ease-in-out;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .room-create {
    padding: 0 1rem;
  }
  
  .room-create__form {
    padding: 1.5rem;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style> 
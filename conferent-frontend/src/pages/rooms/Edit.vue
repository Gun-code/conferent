<template>
  <UserLayout>
    <div class="room-edit">
      <div class="room-edit__header">
        <div class="room-edit__breadcrumb">
          <router-link to="/rooms" class="breadcrumb-link">회의실 목록</router-link>
          <span class="breadcrumb-separator">></span>
          <router-link :to="`/rooms/${roomId}`" class="breadcrumb-link">{{ room?.name || '회의실' }}</router-link>
          <span class="breadcrumb-separator">></span>
          <span class="breadcrumb-current">수정</span>
        </div>
        <h1 class="room-edit__title">회의실 수정</h1>
      </div>

      <div v-if="loading" class="room-edit__loading">
        <p>회의실 정보를 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="room-edit__error">
        <p>{{ error }}</p>
        <BaseButton variant="primary" @click="loadRoom">
          다시 시도
        </BaseButton>
      </div>

      <div v-else class="room-edit__form">
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
              :disabled="saving"
            >
              {{ saving ? '저장 중...' : '수정 완료' }}
            </BaseButton>
          </div>
        </form>
      </div>
    </div>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import { roomApiClient } from '@/api'

export default {
  name: 'RoomEdit',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      room: null,
      loading: false,
      saving: false,
      error: null,
      form: {
        name: '',
        location: '',
        capacity: '',
        description: ''
      }
    }
  },
  computed: {
    roomId() {
      return this.$route.params.id
    }
  },
  mounted() {
    this.loadRoom()
  },
  methods: {
    async loadRoom() {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.getById(this.roomId)
        this.room = response.data
        
        // 폼에 기존 데이터 설정
        this.form = {
          name: this.room.name,
          location: this.room.location,
          capacity: this.room.capacity,
          description: this.room.description || ''
        }
      } catch (err) {
        this.error = '회의실 정보를 불러오는데 실패했습니다.'
        console.error('Failed to load room:', err)
      } finally {
        this.loading = false
      }
    },
    
    async handleSubmit() {
      this.saving = true
      
      try {
        await roomApiClient.update(this.roomId, this.form)
        this.$router.push(`/rooms/${this.roomId}`)
      } catch (err) {
        alert('회의실 수정에 실패했습니다.')
        console.error('Failed to update room:', err)
      } finally {
        this.saving = false
      }
    },
    
    handleCancel() {
      this.$router.push(`/rooms/${this.roomId}`)
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/rooms.css';
</style> 
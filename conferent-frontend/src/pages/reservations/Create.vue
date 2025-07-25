<template>
    <div class="reservation-create">
      <div class="reservation-create__header">
        <div class="reservation-create__breadcrumb">
          <router-link to="/reservations" class="breadcrumb-link">내 예약</router-link>
          <span class="breadcrumb-separator">></span>
          <span class="breadcrumb-current">새 예약</span>
        </div>
        <h1 class="reservation-create__title">회의실 예약</h1>
      </div>

      <div class="reservation-create__form">
        <form @submit.prevent="handleSubmit">
          <div class="form-row">
            <div class="form-group">
              <label for="roomId" class="form-label">회의실 *</label>
              <select
                id="roomId"
                v-model="form.roomId"
                class="form-select"
                required
                @change="handleRoomChange"
              >
                <option value="">회의실을 선택하세요</option>
                <option 
                  v-for="room in rooms" 
                  :key="room.id" 
                  :value="room.id"
                >
                  {{ room.name }} ({{ room.location }}, 최대 {{ room.capacity }}명)
                </option>
              </select>
            </div>

            <div class="form-group">
              <label for="date" class="form-label">날짜 *</label>
              <input
                id="date"
                v-model="form.date"
                type="date"
                class="form-input"
                required
                :min="minDate"
                @change="handleDateChange"
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="startTime" class="form-label">시작 시간 *</label>
              <input
                id="startTime"
                v-model="form.startTime"
                type="time"
                class="form-input"
                required
                @change="handleTimeChange"
              />
            </div>

            <div class="form-group">
              <label for="endTime" class="form-label">종료 시간 *</label>
              <input
                id="endTime"
                v-model="form.endTime"
                type="time"
                class="form-input"
                required
                @change="handleTimeChange"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="purpose" class="form-label">회의 목적 *</label>
            <input
              id="purpose"
              v-model="form.purpose"
              type="text"
              class="form-input"
              required
              placeholder="회의 목적을 입력하세요"
            />
          </div>

          <div class="form-group">
            <label for="attendees" class="form-label">참석 인원</label>
            <input
              id="attendees"
              v-model.number="form.attendees"
              type="number"
              class="form-input"
              min="1"
              :max="selectedRoom?.capacity || 50"
              placeholder="참석 예정 인원수"
            />
            <div v-if="selectedRoom" class="form-help">
              최대 {{ selectedRoom.capacity }}명까지 가능합니다.
            </div>
          </div>

          <div class="form-group">
            <label for="description" class="form-label">상세 내용</label>
            <textarea
              id="description"
              v-model="form.description"
              class="form-textarea"
              rows="4"
              placeholder="회의에 대한 추가 정보를 입력하세요"
            ></textarea>
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

          <!-- 시간 충돌 경고 -->
          <div v-if="timeConflict" class="form-warning">
            ⚠️ 선택한 시간에 다른 예약이 있습니다. 다른 시간을 선택해주세요.
          </div>

          <!-- 선택된 회의실 정보 -->
          <div v-if="selectedRoom" class="selected-room-info">
            <h3 class="selected-room-info__title">선택된 회의실</h3>
            <div class="selected-room-info__content">
              <div class="room-info-item">
                <span class="room-info-label">이름:</span>
                <span class="room-info-value">{{ selectedRoom.name }}</span>
              </div>
              <div class="room-info-item">
                <span class="room-info-label">위치:</span>
                <span class="room-info-value">{{ selectedRoom.location }}</span>
              </div>
              <div class="room-info-item">
                <span class="room-info-label">수용인원:</span>
                <span class="room-info-value">최대 {{ selectedRoom.capacity }}명</span>
              </div>
              <div v-if="selectedRoom.description" class="room-info-item">
                <span class="room-info-label">설명:</span>
                <span class="room-info-value">{{ selectedRoom.description }}</span>
              </div>
            </div>
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
              :disabled="loading || timeConflict"
            >
              {{ loading ? '예약 중...' : '예약하기' }}
            </BaseButton>
          </div>
        </form>
      </div>
    </div>
</template>

<script>

import BaseButton from '@/components/base/BaseButton.vue'
import BaseAlert from '@/components/base/BaseAlert.vue'
import { roomApiClient, rentApiClient } from '@/api'
import { extractErrorMessage } from '@/utils/errorHandler.js'

export default {
  name: 'ReservationCreate',
  components: {
    BaseButton,
    BaseAlert
  },
  data() {
    return {
      loading: false,
      rooms: [],
      selectedRoom: null,
      timeConflict: false,
      form: {
        roomId: '',
        date: '',
        startTime: '',
        endTime: '',
        purpose: '',
        attendees: '',
        description: ''
      },
      showError: false,
      errorMessage: ''
    }
  },
  computed: {
    minDate() {
      return new Date().toISOString().split('T')[0]
    }
  },
  mounted() {
    this.loadRooms()
    this.initializeForm()
  },
  methods: {
    async loadRooms() {
      try {
        const response = await roomApiClient.getAll()
        this.rooms = response.data
      } catch (err) {
        console.error('Failed to load rooms:', err)
      }
    },
    
    initializeForm() {
      // URL 쿼리에서 roomId가 있으면 미리 선택
      const roomId = this.$route.query.roomId
      if (roomId) {
        this.form.roomId = roomId
        this.handleRoomChange()
      }
      
      // 기본 날짜를 오늘로 설정
      this.form.date = this.minDate
    },
    
    handleRoomChange() {
      this.selectedRoom = this.rooms.find(room => room.id == this.form.roomId)
      this.checkTimeConflict()
    },
    
    handleDateChange() {
      this.checkTimeConflict()
    },
    
    handleTimeChange() {
      this.checkTimeConflict()
    },
    
    async checkTimeConflict() {
      if (!this.form.roomId || !this.form.date || !this.form.startTime || !this.form.endTime) {
        this.timeConflict = false
        return
      }
      
      try {
        const startDateTime = `${this.form.date}T${this.form.startTime}`
        const endDateTime = `${this.form.date}T${this.form.endTime}`
        
        const response = await rentApiClient.checkTimeConflict(
          [this.form.roomId],
          startDateTime,
          endDateTime
        )
        this.timeConflict = response.data.hasConflict
      } catch (err) {
        console.error('Failed to check time conflict:', err)
      }
    },
    
    async handleSubmit() {
      // 에러 초기화
      this.showError = false
      this.errorMessage = ''
      this.loading = true
      
      try {
        const reservationData = {
          startTime: `${this.form.date}T${this.form.startTime}`,
          endTime: `${this.form.date}T${this.form.endTime}`,
          purpose: this.form.purpose,
          description: this.form.description,
          creatorId: parseInt(this.$store?.state?.user?.id || localStorage.getItem('userId')),
          roomIds: [this.form.roomId],
          inviteeIds: [] // 참석 인원은 별도 필드이므로 빈 배열로 설정
        }
        
        await rentApiClient.create(reservationData)
        this.$router.push('/reservations')
      } catch (err) {
        this.showError = true
        this.errorMessage = extractErrorMessage(err)
        console.error('Failed to create reservation:', err)
      } finally {
        this.loading = false
      }
    },
    
    handleCancel() {
      this.$router.push('/reservations')
    },
    
    clearError() {
      this.showError = false
      this.errorMessage = ''
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/reservations.css';
</style> 
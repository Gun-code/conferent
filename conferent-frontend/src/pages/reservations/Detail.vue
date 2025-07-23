<template>
  <UserLayout>
    <div class="reservation-detail">
      <div v-if="loading" class="reservation-detail__loading">
        <p>예약 정보를 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="reservation-detail__error">
        <p>{{ error }}</p>
        <BaseButton variant="primary" @click="loadReservation">
          다시 시도
        </BaseButton>
      </div>

      <div v-else-if="reservation" class="reservation-detail__content">
        <!-- 헤더 -->
        <div class="reservation-detail__header">
          <div class="reservation-detail__breadcrumb">
            <router-link to="/reservations" class="breadcrumb-link">내 예약</router-link>
            <span class="breadcrumb-separator">></span>
            <span class="breadcrumb-current">{{ reservation.purpose }}</span>
          </div>
          
          <div class="reservation-detail__actions">
            <BaseButton 
              v-if="canEdit"
              variant="secondary" 
              @click="handleEdit"
            >
              수정
            </BaseButton>
            <BaseButton 
              v-if="canCancel"
              variant="danger" 
              @click="handleCancel"
            >
              취소
            </BaseButton>
          </div>
        </div>

        <!-- 예약 정보 -->
        <div class="reservation-detail__info">
          <div class="reservation-detail__main">
            <h1 class="reservation-detail__title">{{ reservation.purpose }}</h1>
            <div class="reservation-detail__status">
              <span :class="getStatusClass(reservation.status)">
                {{ getStatusText(reservation.status) }}
              </span>
            </div>
            
            <div class="reservation-detail__meta">
              <div class="meta-item">
                <span class="meta-label">📅 날짜</span>
                <span class="meta-value">{{ formatDate(reservation.startTime) }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">⏰ 시간</span>
                <span class="meta-value">
                  {{ formatTime(reservation.startTime) }} - {{ formatTime(reservation.endTime) }}
                </span>
              </div>
              <div class="meta-item">
                <span class="meta-label">🏢 회의실</span>
                <span class="meta-value">
                  <router-link :to="`/rooms/${reservation.roomId}`" class="room-link">
                    {{ reservation.roomName }}
                  </router-link>
                </span>
              </div>
              <div class="meta-item">
                <span class="meta-label">📍 위치</span>
                <span class="meta-value">{{ reservation.roomLocation }}</span>
              </div>
              <div v-if="reservation.attendees" class="meta-item">
                <span class="meta-label">👥 참석 인원</span>
                <span class="meta-value">{{ reservation.attendees }}명</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">👤 예약자</span>
                <span class="meta-value">{{ reservation.creatorName }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">📅 예약일</span>
                <span class="meta-value">{{ formatDateTime(reservation.createdAt) }}</span>
              </div>
            </div>

            <div v-if="reservation.description" class="reservation-detail__description">
              <h3 class="description-title">상세 내용</h3>
              <p class="description-content">{{ reservation.description }}</p>
            </div>
          </div>

          <div class="reservation-detail__sidebar">
            <!-- 회의실 정보 -->
            <div class="room-card">
              <h3 class="room-card__title">회의실 정보</h3>
              <div class="room-card__content">
                <div class="room-info">
                  <h4 class="room-name">{{ reservation.roomName }}</h4>
                  <p class="room-location">📍 {{ reservation.roomLocation }}</p>
                  <p class="room-capacity">👥 최대 {{ reservation.roomCapacity }}명</p>
                  <p v-if="reservation.roomDescription" class="room-description">
                    {{ reservation.roomDescription }}
                  </p>
                </div>
                <BaseButton 
                  variant="outline" 
                  size="small"
                  @click="viewRoom"
                >
                  회의실 보기
                </BaseButton>
              </div>
            </div>

            <!-- 예약 히스토리 -->
            <div v-if="reservationHistory.length > 0" class="history-card">
              <h3 class="history-card__title">변경 이력</h3>
              <div class="history-list">
                <div 
                  v-for="history in reservationHistory" 
                  :key="history.id"
                  class="history-item"
                >
                  <div class="history-action">{{ history.action }}</div>
                  <div class="history-time">{{ formatDateTime(history.createdAt) }}</div>
                  <div v-if="history.note" class="history-note">{{ history.note }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import { rentApiClient } from '@/api'
import { formatDate } from '@/utils/date'

export default {
  name: 'ReservationDetail',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      reservation: null,
      reservationHistory: [],
      loading: false,
      error: null
    }
  },
  computed: {
    reservationId() {
      return this.$route.params.id
    },
    canEdit() {
      if (!this.reservation) return false
      return this.reservation.status === 'PENDING' || this.reservation.status === 'CONFIRMED'
    },
    canCancel() {
      if (!this.reservation) return false
      return this.reservation.status === 'PENDING' || this.reservation.status === 'CONFIRMED'
    }
  },
  mounted() {
    this.loadReservation()
    this.loadReservationHistory()
  },
  methods: {
    async loadReservation() {
      this.loading = true
      this.error = null
      
      try {
        const response = await rentApiClient.getRent(this.reservationId)
        this.reservation = response.data
      } catch (err) {
        this.error = '예약 정보를 불러오는데 실패했습니다.'
        console.error('Failed to load reservation:', err)
      } finally {
        this.loading = false
      }
    },
    
    async loadReservationHistory() {
      try {
        const response = await rentApiClient.getRentHistory(this.reservationId)
        this.reservationHistory = response.data
      } catch (err) {
        console.error('Failed to load reservation history:', err)
      }
    },
    
    formatDate(dateString) {
      return formatDate(dateString, 'date')
    },
    
    formatTime(dateString) {
      return formatDate(dateString, 'time')
    },
    
    formatDateTime(dateString) {
      return formatDate(dateString, 'datetime')
    },
    
    getStatusClass(status) {
      const classes = {
        PENDING: 'status-pending',
        CONFIRMED: 'status-confirmed',
        CANCELLED: 'status-cancelled',
        COMPLETED: 'status-completed'
      }
      return classes[status] || ''
    },
    
    getStatusText(status) {
      const texts = {
        PENDING: '대기중',
        CONFIRMED: '확정',
        CANCELLED: '취소',
        COMPLETED: '완료'
      }
      return texts[status] || status
    },
    
    handleEdit() {
      this.$router.push(`/reservations/${this.reservationId}/edit`)
    },
    
    async handleCancel() {
      if (confirm('정말로 이 예약을 취소하시겠습니까?')) {
        try {
          await rentApiClient.cancelRent(this.reservationId)
          this.loadReservation() // 상태 새로고침
        } catch (err) {
          alert('예약 취소에 실패했습니다.')
          console.error('Failed to cancel reservation:', err)
        }
      }
    },
    
    viewRoom() {
      this.$router.push(`/rooms/${this.reservation.roomId}`)
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/reservations.css';
</style> 
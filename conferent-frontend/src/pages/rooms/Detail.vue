<template>
  <div class="room-detail">
      <div v-if="loading" class="room-detail__loading">
        <p>회의실 정보를 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="room-detail__error">
        <p>{{ error }}</p>
        <BaseButton variant="primary" @click="loadRoom">
          다시 시도
        </BaseButton>
      </div>

      <div v-else-if="room" class="room-detail__content">
        <!-- 헤더 -->
        <div class="room-detail__header">
          <div class="room-detail__breadcrumb">
            <router-link to="/rooms" class="breadcrumb-link">회의실 목록</router-link>
            <span class="breadcrumb-separator">></span>
            <span class="breadcrumb-current">{{ room.name }}</span>
          </div>
          
          <div class="room-detail__actions">
            <BaseButton 
              variant="secondary" 
              @click="handleEditRoom"
            >
              수정
            </BaseButton>
            <BaseButton 
              variant="primary" 
              @click="handleReserveRoom"
            >
              예약하기
            </BaseButton>
          </div>
        </div>

        <!-- 회의실 정보 -->
        <div class="room-detail__info">
          <div class="room-detail__main">
            <h1 class="room-detail__title">{{ room.name }}</h1>
            <div class="room-detail__meta">
              <span class="room-detail__location">📍 {{ room.location }}</span>
              <span class="room-detail__capacity">👥 최대 {{ room.capacity }}명</span>
            </div>
            <p v-if="room.description" class="room-detail__description">
              {{ room.description }}
            </p>
          </div>

          <div class="room-detail__image">
            <div class="room-image-placeholder">
              🏢
            </div>
          </div>
        </div>

        <!-- 시설 및 장비 -->
        <div class="room-detail__facilities">
          <h2 class="section-title">시설 및 장비</h2>
          <div class="facilities-grid">
            <div class="facility-item">
              <span class="facility-icon">📺</span>
              <span class="facility-name">프로젝터</span>
            </div>
            <div class="facility-item">
              <span class="facility-icon">💻</span>
              <span class="facility-name">화상회의 시설</span>
            </div>
            <div class="facility-item">
              <span class="facility-icon">📠</span>
              <span class="facility-name">화이트보드</span>
            </div>
            <div class="facility-item">
              <span class="facility-icon">☕</span>
              <span class="facility-name">커피머신</span>
            </div>
          </div>
        </div>

        <!-- 예약 현황 -->
        <div class="room-detail__reservations">
          <h2 class="section-title">오늘의 예약 현황</h2>
          <div v-if="todayReservations.length === 0" class="no-reservations">
            <p>오늘 예약이 없습니다.</p>
          </div>
          <div v-else class="reservations-list">
            <div 
              v-for="reservation in todayReservations" 
              :key="reservation.id"
              class="reservation-item"
            >
              <div class="reservation-time">
                {{ formatTime(reservation.startTime) }} - {{ formatTime(reservation.endTime) }}
              </div>
              <div class="reservation-info">
                <h4 class="reservation-title">{{ reservation.purpose }}</h4>
                <p class="reservation-organizer">{{ reservation.creatorName }}</p>
              </div>
              <div class="reservation-status">
                <span :class="getStatusClass(reservation.status)">
                  {{ getStatusText(reservation.status) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
import BaseButton from '@/components/base/BaseButton.vue'
import { roomApiClient, rentApiClient } from '@/api'
import { formatDate } from '@/utils/date'

export default {
  name: 'RoomDetail',
  components: {
    BaseButton
  },
  data() {
    return {
      room: null,
      todayReservations: [],
      loading: false,
      error: null
    }
  },
  mounted() {
    this.loadRoom()
    this.loadTodayReservations()
  },
  methods: {
    async loadRoom() {
      this.loading = true
      this.error = null
      
      try {
        const roomId = this.$route.params.id
        const response = await roomApiClient.getById(roomId)
        this.room = response.data
      } catch (err) {
        this.error = '회의실 정보를 불러오는데 실패했습니다.'
        console.error('Failed to load room:', err)
      } finally {
        this.loading = false
      }
    },
    
    async loadTodayReservations() {
      try {
        const roomId = this.$route.params.id
        const today = new Date().toISOString().split('T')[0]
        const response = await rentApiClient.getByRoom(roomId)
        this.todayReservations = response.data
      } catch (err) {
        console.error('Failed to load reservations:', err)
      }
    },
    
    formatTime(dateString) {
      return formatDate(dateString, 'time')
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
    
    handleEditRoom() {
      this.$router.push(`/rooms/${this.room.id}/edit`)
    },
    
    handleReserveRoom() {
      this.$router.push({
        path: '/reservations/create',
        query: { roomId: this.room.id }
      })
    }
  }
}
</script>

<style scoped>
.room-detail {
  max-width: 1000px;
  margin: 0 auto;
  padding: var(--spacing-6);
}

.room-detail__loading,
.room-detail__error {
  text-align: center;
  padding: var(--spacing-16) 0;
  color: var(--color-text-secondary);
}

.room-detail__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-8);
}

.room-detail__breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  font-size: var(--font-size-sm);
}

.breadcrumb-link {
  color: var(--color-primary);
  text-decoration: none;
}

.breadcrumb-link:hover {
  text-decoration: underline;
}

.breadcrumb-separator {
  color: var(--color-text-light);
}

.breadcrumb-current {
  color: var(--color-text-secondary);
}

.room-detail__actions {
  display: flex;
  gap: var(--spacing-3);
}

.room-detail__info {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: var(--spacing-8);
  margin-bottom: var(--spacing-8);
}

.room-detail__title {
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-4) 0;
}

.room-detail__meta {
  display: flex;
  gap: var(--spacing-6);
  margin-bottom: var(--spacing-4);
}

.room-detail__location,
.room-detail__capacity {
  display: flex;
  align-items: center;
  font-size: var(--font-size-lg);
  color: var(--color-text-secondary);
}

.room-detail__description {
  font-size: var(--font-size-lg);
  line-height: var(--leading-relaxed);
  color: var(--color-text-secondary);
  margin: 0;
}

.room-detail__image {
  display: flex;
  align-items: center;
  justify-content: center;
}

.room-image-placeholder {
  width: 100%;
  height: 200px;
  background-color: var(--color-gray-100);
  border-radius: var(--border-radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 4rem;
}

.section-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  margin: 0 0 var(--spacing-6) 0;
}

.room-detail__facilities {
  margin-bottom: var(--spacing-8);
}

.facilities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-4);
}

.facility-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-4);
  background-color: var(--color-gray-50);
  border-radius: var(--border-radius-lg);
}

.facility-icon {
  font-size: var(--font-size-xl);
}

.facility-name {
  font-weight: var(--font-weight-medium);
  color: var(--color-text-primary);
}

.room-detail__reservations {
  margin-bottom: var(--spacing-8);
}

.no-reservations {
  text-align: center;
  padding: var(--spacing-8) 0;
  color: var(--color-text-light);
}

.reservations-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.reservation-item {
  display: flex;
  align-items: center;
  padding: var(--spacing-4);
  background-color: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
}

.reservation-time {
  min-width: 120px;
  font-weight: var(--font-weight-medium);
  color: var(--color-text-primary);
}

.reservation-info {
  flex: 1;
  margin-left: var(--spacing-6);
}

.reservation-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  margin: 0 0 var(--spacing-1) 0;
}

.reservation-organizer {
  color: var(--color-text-secondary);
  margin: 0;
}

.reservation-status {
  margin-left: var(--spacing-6);
}

.status-pending {
  background-color: #fef3c7;
  color: #92400e;
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--border-radius);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

.status-confirmed {
  background-color: #d1fae5;
  color: #065f46;
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--border-radius);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

.status-cancelled {
  background-color: #fee2e2;
  color: #991b1b;
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--border-radius);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

.status-completed {
  background-color: #e0e7ff;
  color: #3730a3;
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--border-radius);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-medium);
}

@media (max-width: 768px) {
  .room-detail {
    padding: var(--spacing-4);
  }
  
  .room-detail__header {
    flex-direction: column;
    gap: var(--spacing-4);
    align-items: stretch;
  }
  
  .room-detail__info {
    grid-template-columns: 1fr;
  }
  
  .room-detail__meta {
    flex-direction: column;
    gap: var(--spacing-2);
  }
  
  .facilities-grid {
    grid-template-columns: 1fr;
  }
  
  .reservation-item {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-3);
  }
  
  .reservation-info,
  .reservation-status {
    margin-left: 0;
  }
}
</style> 
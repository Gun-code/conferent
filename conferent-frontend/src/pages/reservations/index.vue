<template>
    <div class="reservation-list">
      <div class="reservation-list__header">
        <h1 class="reservation-list__title">내 예약</h1>
        <BaseButton 
          variant="primary" 
          @click="handleCreateReservation"
        >
          예약하기
        </BaseButton>
      </div>

      <div class="reservation-list__filters">
        <div class="reservation-list__filter-group">
          <label for="status-filter" class="reservation-list__filter-label">상태</label>
          <select 
            id="status-filter" 
            v-model="filters.status"
            class="reservation-list__filter-select"
            @change="handleFilterChange"
          >
            <option value="">전체</option>
            <option value="PENDING">대기중</option>
            <option value="CONFIRMED">확정</option>
            <option value="CANCELLED">취소</option>
            <option value="COMPLETED">완료</option>
          </select>
        </div>

        <div class="reservation-list__filter-group">
          <label for="date-filter" class="reservation-list__filter-label">날짜</label>
          <input
            id="date-filter"
            v-model="filters.date"
            type="date"
            class="reservation-list__filter-input"
            @change="handleFilterChange"
          />
        </div>
      </div>

      <div v-if="loading" class="reservation-list__loading">
        <p>예약 목록을 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="reservation-list__error">
        <p>{{ error }}</p>
        <BaseButton 
          variant="primary" 
          @click="loadReservations"
        >
          다시 시도
        </BaseButton>
      </div>

      <div v-else-if="filteredReservations.length === 0" class="reservation-list__empty">
        <p v-if="filters.status || filters.date">조건에 맞는 예약이 없습니다.</p>
        <p v-else>아직 예약한 회의가 없습니다.</p>
        <BaseButton 
          v-if="!filters.status && !filters.date"
          variant="primary" 
          @click="handleCreateReservation"
          class="mt-4"
        >
          첫 예약 만들기
        </BaseButton>
      </div>

      <div v-else class="reservation-list__grid">
        <div 
          v-for="reservation in filteredReservations" 
          :key="reservation.id"
          class="reservation-card"
        >
          <div class="reservation-card__header">
            <h3 class="reservation-card__title">{{ reservation.purpose }}</h3>
            <span :class="getStatusClass(reservation.status)">
              {{ getStatusText(reservation.status) }}
            </span>
          </div>
          
          <div class="reservation-card__content">
            <div class="reservation-card__info">
              <p class="reservation-card__room">
                <strong>회의실:</strong> {{ reservation.roomName }}
              </p>
              <p class="reservation-card__time">
                <strong>시간:</strong> {{ formatDateTime(reservation.startTime) }} - {{ formatTime(reservation.endTime) }}
              </p>
              <p class="reservation-card__creator">
                <strong>예약자:</strong> {{ reservation.creatorName }}
              </p>
            </div>
          </div>
          
          <div class="reservation-card__actions">
            <BaseButton 
              variant="primary" 
              size="small"
              @click="handleViewReservation(reservation)"
            >
              상세보기
            </BaseButton>
            <BaseButton 
              v-if="canEdit(reservation)"
              variant="secondary" 
              size="small"
              @click="handleEditReservation(reservation)"
            >
              수정
            </BaseButton>
            <BaseButton 
              v-if="canCancel(reservation)"
              variant="danger" 
              size="small"
              @click="handleCancelReservation(reservation)"
            >
              취소
            </BaseButton>
          </div>
        </div>
      </div>
    </div>
</template>

<script>

import BaseButton from '@/components/base/BaseButton.vue'
import { rentApiClient } from '@/api'
import { formatDate } from '@/utils/date'
import { useAuthStore } from '@/store/authStore.js'

export default {
  name: 'ReservationList',
  components: {

    BaseButton
  },
  data() {
    return {
      reservations: [],
      loading: false,
      error: null,
      filters: {
        status: '',
        date: ''
      }
    }
  },
  computed: {
    authStore() {
      return useAuthStore()
    },
    currentUserId() {
      const userId = this.authStore.userId
      console.log('Current user ID:', userId)
      return userId
    },
    filteredReservations() {
      let filtered = [...this.reservations]
      
      if (this.filters.status) {
        filtered = filtered.filter(reservation => 
          reservation.status === this.filters.status
        )
      }
      
      if (this.filters.date) {
        filtered = filtered.filter(reservation => 
          reservation.startTime.startsWith(this.filters.date)
        )
      }
      
      return filtered.sort((a, b) => new Date(b.startTime) - new Date(a.startTime))
    }
  },
  mounted() {
    this.loadReservations()
  },
  watch: {
    currentUserId: {
      handler(newUserId) {
        if (newUserId) {
          this.loadReservations()
        }
      },
      immediate: true
    }
  },
  methods: {
    async loadReservations() {
      // 사용자 ID가 없으면 로딩하지 않음
      if (!this.currentUserId) {
        console.warn('User ID not available, skipping reservation load')
        return
      }
      
      console.log('Loading reservations for user ID:', this.currentUserId)
      
      this.loading = true
      this.error = null
      
      try {
        // 현재 사용자의 예약만 가져오기
        const response = await rentApiClient.getByCreator(this.currentUserId)
        console.log('Reservations response:', response.data)
        this.reservations = response.data
      } catch (err) {
        this.error = '예약 목록을 불러오는데 실패했습니다.'
        console.error('Failed to load reservations:', err)
      } finally {
        this.loading = false
      }
    },
    
    handleFilterChange() {
      // 필터 변경 시 추가 로직이 필요한 경우 여기에 구현
    },
    
    formatDateTime(dateString) {
      return formatDate(dateString, 'datetime')
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
      return `reservation-card__status ${classes[status] || ''}`
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
    
    canEdit(reservation) {
      // 예약자 본인인 경우에만 수정 가능 (내 예약 페이지이므로)
      return reservation.creatorId === this.currentUserId && 
             (reservation.status === 'PENDING' || reservation.status === 'CONFIRMED')
    },
    
    canCancel(reservation) {
      // 예약자 본인인 경우에만 취소 가능 (내 예약 페이지이므로)
      return reservation.creatorId === this.currentUserId && 
             (reservation.status === 'PENDING' || reservation.status === 'CONFIRMED')
    },
    
    handleCreateReservation() {
      this.$router.push('/reservations/create')
    },
    
    handleViewReservation(reservation) {
      this.$router.push(`/reservations/${reservation.id}`)
    },
    
    handleEditReservation(reservation) {
      this.$router.push(`/reservations/${reservation.id}/edit`)
    },
    
    async handleCancelReservation(reservation) {
      if (confirm(`정말로 이 예약을 취소하시겠습니까?`)) {
        try {
          await rentApiClient.delete(reservation.id)
          this.loadReservations() // 목록 새로고침
        } catch (err) {
          alert('예약 취소에 실패했습니다.')
          console.error('Failed to cancel reservation:', err)
        }
      }
    }
  }
}
</script>

<style scoped>
.reservation-list__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.reservation-list__title {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.reservation-list__filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.reservation-list__filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.reservation-list__filter-label {
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
}

.reservation-list__filter-select,
.reservation-list__filter-input {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  background-color: white;
  min-width: 150px;
}

.reservation-list__loading,
.reservation-list__error,
.reservation-list__empty {
  text-align: center;
  padding: 3rem 0;
  color: #6b7280;
}

.reservation-list__grid {
  display: grid;
  gap: 1.5rem;
}

.reservation-card {
  background-color: white;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s ease-in-out;
}

.reservation-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.reservation-card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.reservation-card__title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.reservation-card__status {
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-pending {
  background-color: #fef3c7;
  color: #92400e;
}

.status-confirmed {
  background-color: #d1fae5;
  color: #065f46;
}

.status-cancelled {
  background-color: #fee2e2;
  color: #991b1b;
}

.status-completed {
  background-color: #e0e7ff;
  color: #3730a3;
}

.reservation-card__content {
  margin-bottom: 1.5rem;
}

.reservation-card__info p {
  margin: 0.5rem 0;
  color: #374151;
}

.reservation-card__actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .reservation-list__header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .reservation-list__title {
    font-size: 1.5rem;
    text-align: center;
  }
  
  .reservation-list__filters {
    flex-direction: column;
  }
}
</style> 
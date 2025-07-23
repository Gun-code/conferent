<template>
  <UserLayout>
    <!-- Hero Section - 미니멀한 디자인 -->
    <section class="hero">
      <div class="hero__container">
        <div class="hero__content">
          <h1 class="hero__title">
            스마트한 회의실 예약
          </h1>
          <p class="hero__subtitle">
            간단하고 빠른 회의실 예약으로 더 효율적인 업무 환경을 만들어보세요
          </p>
          <div class="hero__actions">
            <router-link to="/rooms" class="hero__cta hero__cta--primary">
              회의실 둘러보기
            </router-link>
            <router-link to="/reservations/create" class="hero__cta hero__cta--secondary">
              바로 예약하기
            </router-link>
          </div>
        </div>
        <div class="hero__visual">
          <div class="hero__card">
            <div class="card-icon">🏢</div>
            <h3>회의실 A</h3>
            <p>8명 · 3층 · 프로젝터</p>
            <div class="card-status available">예약 가능</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Quick Stats - 간결한 통계 -->
    <section class="quick-stats">
      <div class="quick-stats__container">
        <div class="stat-item">
          <div class="stat-item__value">{{ stats.totalRooms }}</div>
          <div class="stat-item__label">회의실</div>
        </div>
        <div class="stat-item">
          <div class="stat-item__value">{{ stats.todayReservations }}</div>
          <div class="stat-item__label">오늘 예약</div>
        </div>
        <div class="stat-item">
          <div class="stat-item__value">{{ stats.availableNow }}</div>
          <div class="stat-item__label">현재 이용 가능</div>
        </div>
      </div>
    </section>

    <!-- How It Works - 3단계 설명 -->
    <section class="how-it-works">
      <div class="how-it-works__container">
        <h2 class="section-title">간단한 3단계</h2>
        <div class="steps">
          <div class="step">
            <div class="step__number">1</div>
            <h3 class="step__title">회의실 선택</h3>
            <p class="step__description">
              필요한 인원과 시설에 맞는<br>회의실을 선택하세요
            </p>
          </div>
          <div class="step">
            <div class="step__number">2</div>
            <h3 class="step__title">시간 예약</h3>
            <p class="step__description">
              원하는 날짜와 시간을<br>선택하여 예약하세요
            </p>
          </div>
          <div class="step">
            <div class="step__number">3</div>
            <h3 class="step__title">회의 시작</h3>
            <p class="step__description">
              예약된 시간에 회의실을<br>이용하세요
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- Available Rooms - 현재 이용 가능한 회의실 -->
    <section class="available-rooms" v-if="availableRooms.length > 0">
      <div class="available-rooms__container">
        <div class="section-header">
          <h2 class="section-title">지금 이용 가능한 회의실</h2>
          <router-link to="/rooms" class="section-link">
            전체 보기 →
          </router-link>
        </div>
        <div class="rooms-grid">
          <div 
            v-for="room in availableRooms.slice(0, 3)" 
            :key="room.id"
            class="room-card"
            @click="$router.push(`/rooms/${room.id}`)"
          >
            <div class="room-card__header">
              <h3 class="room-card__name">{{ room.name }}</h3>
              <div class="room-card__status available">이용 가능</div>
            </div>
            <div class="room-card__details">
              <span class="detail-item">
                <span class="detail-icon">👥</span>
                {{ room.capacity }}명
              </span>
              <span class="detail-item">
                <span class="detail-icon">📍</span>
                {{ room.location }}
              </span>
            </div>
            <button class="room-card__cta" @click.stop="handleQuickReserve(room)">
              빠른 예약
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Recent Reservations - 내 최근 예약 -->
    <section class="recent-reservations" v-if="recentReservations.length > 0">
      <div class="recent-reservations__container">
        <div class="section-header">
          <h2 class="section-title">내 예약</h2>
          <router-link to="/reservations" class="section-link">
            전체 보기 →
          </router-link>
        </div>
        <div class="reservations-list">
          <div 
            v-for="reservation in recentReservations.slice(0, 3)" 
            :key="reservation.id"
            class="reservation-item"
            @click="$router.push(`/reservations/${reservation.id}`)"
          >
            <div class="reservation-item__date">
              <div class="date-day">{{ formatDay(reservation.startTime) }}</div>
              <div class="date-month">{{ formatMonth(reservation.startTime) }}</div>
            </div>
            <div class="reservation-item__content">
              <h4 class="reservation-title">{{ reservation.purpose }}</h4>
              <p class="reservation-details">
                {{ reservation.roomName }} · 
                {{ formatTime(reservation.startTime) }} - {{ formatTime(reservation.endTime) }}
              </p>
            </div>
            <div class="reservation-item__status">
              <span :class="['status-badge', getStatusClass(reservation.status)]">
                {{ getStatusText(reservation.status) }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section - 간단한 액션 -->
    <section class="cta-section">
      <div class="cta-section__container">
        <h2 class="cta-section__title">회의실 예약을 시작하세요</h2>
        <p class="cta-section__description">
          더 효율적인 업무를 위한 첫 걸음
        </p>
        <router-link to="/reservations/create" class="cta-section__button">
          지금 예약하기
        </router-link>
      </div>
    </section>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import { roomApiClient, rentApiClient, roomRentApiClient } from '@/api'

export default {
  name: 'Home',
  components: {
    UserLayout
  },
  data() {
    return {
      stats: {
        totalRooms: 0,
        todayReservations: 0,
        availableNow: 0
      },
      availableRooms: [],
      recentReservations: [],
      loading: false
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      
      try {
        await Promise.all([
          this.loadStats(),
          this.loadAvailableRooms(),
          this.loadRecentReservations()
        ])
      } catch (err) {
        console.error('Failed to load home data:', err)
      } finally {
        this.loading = false
      }
    },
    
    async loadStats() {
      try {
        // 실제로는 별도 API 엔드포인트에서 통계 데이터를 가져옴
        const roomsResponse = await roomApiClient.getAllRooms()
        this.stats.totalRooms = roomsResponse.data.length
        
        const today = new Date().toISOString().split('T')[0]
        const tomorrow = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0]
        const reservationsResponse = await rentApiClient.getRentsByDateRange(today, tomorrow)
        this.stats.todayReservations = reservationsResponse.data.length
        
        // 현재 시간 기준으로 이용 가능한 회의실 계산
        this.stats.availableNow = Math.max(0, this.stats.totalRooms - this.stats.todayReservations)
      } catch (err) {
        console.error('Failed to load stats:', err)
        // 기본값 설정
        this.stats = {
          totalRooms: 12,
          todayReservations: 8,
          availableNow: 4
        }
      }
    },
    
    async loadAvailableRooms() {
      try {
        const response = await roomRentApiClient.getAvailableRooms()
        this.availableRooms = response.data || []
      } catch (err) {
        console.error('Failed to load available rooms:', err)
        // 더미 데이터
        this.availableRooms = [
          { id: 1, name: '회의실 A', capacity: 8, location: '3층' },
          { id: 2, name: '회의실 B', capacity: 12, location: '4층' },
          { id: 3, name: '회의실 C', capacity: 6, location: '2층' }
        ]
      }
    },
    
    async loadRecentReservations() {
      try {
        // 로그인한 사용자만 최근 예약을 로드
        const token = localStorage.getItem('token');
        if (!token) {
          this.recentReservations = [];
          return;
        }
        
        // 사용자 ID 가져오기
        let userId;
        try {
          userId = this.$store?.state?.user?.id || localStorage.getItem('userId');
        } catch {
          userId = null;
        }
        
        if (!userId) {
          this.recentReservations = [];
          return;
        }
        
        const response = await rentApiClient.getRecentRents(userId)
        this.recentReservations = response.data || []
      } catch (err) {
        console.error('Failed to load recent reservations:', err)
        this.recentReservations = []
      }
    },
    
    formatDay(dateString) {
      return new Date(dateString).getDate()
    },
    
    formatMonth(dateString) {
      const months = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월']
      return months[new Date(dateString).getMonth()]
    },
    
    formatTime(dateString) {
      return new Date(dateString).toLocaleTimeString('ko-KR', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    },
    
    getStatusClass(status) {
      const classes = {
        PENDING: 'pending',
        CONFIRMED: 'confirmed',
        CANCELLED: 'cancelled',
        COMPLETED: 'completed'
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
    
    handleQuickReserve(room) {
      this.$router.push({
        path: '/reservations/create',
        query: { roomId: room.id }
      })
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/home.css';
</style> 
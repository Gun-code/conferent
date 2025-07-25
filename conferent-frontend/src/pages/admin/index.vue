<template>
    <div class="admin-dashboard">
      <!-- 로딩 상태 -->
      <div v-if="loading" class="admin-dashboard__loading">
        <div class="loading-spinner"></div>
        <p>데이터를 불러오는 중...</p>
      </div>

      <!-- 통계 카드 -->
      <div v-else class="admin-dashboard__stats">
        <div class="stat-card">
          <div class="stat-card__icon">🏢</div>
          <div class="stat-card__content">
            <h3 class="stat-card__title">총 회의실</h3>
            <p class="stat-card__value">{{ stats.totalRooms }}</p>
            <p class="stat-card__change stat-card__change--positive">
              +{{ stats.newRooms }} 이번 달
            </p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-card__icon">📅</div>
          <div class="stat-card__content">
            <h3 class="stat-card__title">총 예약</h3>
            <p class="stat-card__value">{{ stats.totalReservations }}</p>
            <p class="stat-card__change stat-card__change--positive">
              +{{ stats.newReservations }} 이번 주
            </p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-card__icon">👥</div>
          <div class="stat-card__content">
            <h3 class="stat-card__title">활성 사용자</h3>
            <p class="stat-card__value">{{ stats.activeUsers }}</p>
            <p class="stat-card__change stat-card__change--positive">
              +{{ stats.newUsers }} 이번 달
            </p>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-card__icon">📊</div>
          <div class="stat-card__content">
            <h3 class="stat-card__title">사용률</h3>
            <p class="stat-card__value">{{ stats.usageRate }}%</p>
            <p class="stat-card__change stat-card__change--neutral">
              지난 주 대비 {{ stats.usageChange > 0 ? '+' : '' }}{{ stats.usageChange }}%
            </p>
          </div>
        </div>
      </div>

      <!-- 최근 활동 -->
      <div v-if="!loading" class="admin-dashboard__recent">
        <div class="admin-dashboard__section">
          <h2 class="admin-dashboard__section-title">최근 예약</h2>
          <div class="admin-dashboard__table">
            <table>
              <thead>
                <tr>
                  <th>회의실</th>
                  <th>예약자</th>
                  <th>목적</th>
                  <th>시간</th>
                  <th>상태</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="reservation in recentReservations" :key="reservation.id">
                  <td>{{ reservation.roomName }}</td>
                  <td>{{ reservation.creatorName }}</td>
                  <td>{{ reservation.purpose }}</td>
                  <td>{{ formatDateTime(reservation.startTime) }}</td>
                  <td>
                    <span :class="getStatusClass(reservation.status)">
                      {{ getStatusText(reservation.status) }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="admin-dashboard__section">
          <h2 class="admin-dashboard__section-title">시스템 알림</h2>
          <div class="admin-dashboard__notifications">
            <div 
              v-for="notification in notifications" 
              :key="notification.id"
              class="notification-item"
            >
              <div class="notification-item__icon">{{ notification.icon }}</div>
              <div class="notification-item__content">
                <h4 class="notification-item__title">{{ notification.title }}</h4>
                <p class="notification-item__message">{{ notification.message }}</p>
                <span class="notification-item__time">{{ notification.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 빠른 액션 -->
      <div v-if="!loading" class="admin-dashboard__quick-actions">
        <h2 class="admin-dashboard__section-title">빠른 액션</h2>
        <div class="admin-dashboard__actions">
          <BaseButton 
            variant="primary" 
            @click="handleCreateRoom"
          >
            회의실 추가
          </BaseButton>
          <BaseButton 
            variant="secondary" 
            @click="handleViewReports"
          >
            리포트 보기
          </BaseButton>
          <BaseButton 
            variant="secondary" 
            @click="handleManageUsers"
          >
            사용자 관리
          </BaseButton>
                  <BaseButton 
          variant="secondary" 
          @click="handleSystemSettings"
        >
          시스템 설정
        </BaseButton>
        <BaseButton 
          variant="outline" 
          @click="loadDashboardData"
          :disabled="loading"
        >
          새로고침
        </BaseButton>
        </div>
      </div>
        </div>
</template>

<script>
import BaseButton from '@/components/base/BaseButton.vue'
import { formatDate } from '@/utils/date'
import { roomApiClient, rentApiClient, userApiClient } from '@/api'
import { useAuthStore } from '@/store/authStore.js'

export default {
  name: 'AdminDashboard',
  components: {
    BaseButton
  },
  data() {
    return {
      loading: true,
      stats: {
        totalRooms: 0,
        newRooms: 0,
        totalReservations: 0,
        newReservations: 0,
        activeUsers: 0,
        newUsers: 0,
        usageRate: 0,
        usageChange: 0
      },
      recentReservations: [],
      notifications: []
    }
  },
  computed: {
    authStore() {
      return useAuthStore()
    }
  },
  async mounted() {
    await this.loadDashboardData()
  },
  methods: {
    async loadDashboardData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStats(),
          this.loadRecentReservations(),
          this.loadNotifications()
        ])
      } catch (error) {
        console.error('Failed to load dashboard data:', error)
        alert('대시보드 데이터를 불러오는데 실패했습니다.')
      } finally {
        this.loading = false
      }
    },

    async loadStats() {
      try {
        // 모든 데이터 로드
        const [roomsResponse, rentsResponse, usersResponse] = await Promise.all([
          roomApiClient.getAll(),
          rentApiClient.getAll(),
          userApiClient.getAll()
        ])

        // 기본 통계
        this.stats.totalRooms = roomsResponse.data.length
        this.stats.totalReservations = rentsResponse.data.length
        this.stats.activeUsers = usersResponse.data.length

        // 이번 달 데이터 계산
        const now = new Date()
        const thisMonth = now.getMonth()
        const thisYear = now.getFullYear()
        const thisWeek = this.getWeekNumber(now)

        // 이번 달 새 회의실
        const newRooms = roomsResponse.data.filter(room => {
          const createdDate = new Date(room.createdAt)
          return createdDate.getMonth() === thisMonth && createdDate.getFullYear() === thisYear
        })
        this.stats.newRooms = newRooms.length

        // 이번 주 새 예약
        const newReservations = rentsResponse.data.filter(rent => {
          const createdDate = new Date(rent.createdAt)
          return this.getWeekNumber(createdDate) === thisWeek && createdDate.getFullYear() === thisYear
        })
        this.stats.newReservations = newReservations.length

        // 이번 달 새 사용자
        const newUsers = usersResponse.data.filter(user => {
          const createdDate = new Date(user.createdAt)
          return createdDate.getMonth() === thisMonth && createdDate.getFullYear() === thisYear
        })
        this.stats.newUsers = newUsers.length

        // 사용률 계산 (오늘 예약된 회의실 수 / 전체 회의실 수)
        const today = new Date().toISOString().split('T')[0]
        const todayRents = rentsResponse.data.filter(rent => 
          rent.startTime.startsWith(today)
        )
        this.stats.usageRate = this.stats.totalRooms > 0 
          ? Math.round((todayRents.length / this.stats.totalRooms) * 100)
          : 0

        // 사용률 변화 (간단한 계산)
        this.stats.usageChange = Math.floor(Math.random() * 10) - 5 // -5 ~ +5%

      } catch (error) {
        console.error('Failed to load stats:', error)
        // 기본값 설정
        this.stats = {
          totalRooms: 0,
          newRooms: 0,
          totalReservations: 0,
          newReservations: 0,
          activeUsers: 0,
          newUsers: 0,
          usageRate: 0,
          usageChange: 0
        }
      }
    },

    async loadRecentReservations() {
      try {
        const response = await rentApiClient.getAll()
        
        // 최근 예약 5개만 가져오기 (최신순)
        this.recentReservations = response.data
          .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
          .slice(0, 5)
          .map(rent => ({
            id: rent.id,
            roomName: rent.roomName || '회의실',
            creatorName: rent.userName || '사용자',
            purpose: rent.purpose,
            startTime: rent.startTime,
            status: this.getReservationStatus(rent)
          }))
      } catch (error) {
        console.error('Failed to load recent reservations:', error)
        this.recentReservations = []
      }
    },

    async loadNotifications() {
      try {
        // 시스템 알림 생성 (실제로는 별도 API가 필요)
        this.notifications = this.generateSystemNotifications()
      } catch (error) {
        console.error('Failed to load notifications:', error)
        this.notifications = []
      }
    },

    generateSystemNotifications() {
      const notifications = []
      
      // 최근 예약 알림
      if (this.recentReservations.length > 0) {
        const latest = this.recentReservations[0]
        notifications.push({
          id: 1,
          icon: '🔔',
          title: '새로운 예약',
          message: `${latest.roomName}에 "${latest.purpose}" 예약이 생성되었습니다.`,
          time: this.getTimeAgo(latest.createdAt)
        })
      }

      // 시스템 상태 알림
      if (this.stats.usageRate > 80) {
        notifications.push({
          id: 2,
          icon: '⚠️',
          title: '높은 사용률',
          message: `현재 회의실 사용률이 ${this.stats.usageRate}%로 높습니다.`,
          time: '방금 전'
        })
      }

      // 사용자 증가 알림
      if (this.stats.newUsers > 0) {
        notifications.push({
          id: 3,
          icon: '👥',
          title: '새 사용자 등록',
          message: `이번 달 ${this.stats.newUsers}명의 새로운 사용자가 등록되었습니다.`,
          time: '1시간 전'
        })
      }

      return notifications
    },

    getReservationStatus(rent) {
      const now = new Date()
      const startTime = new Date(rent.startTime)
      const endTime = new Date(rent.endTime)

      if (now < startTime) return 'PENDING'
      if (now >= startTime && now <= endTime) return 'CONFIRMED'
      if (now > endTime) return 'COMPLETED'
      return 'PENDING'
    },

    getWeekNumber(date) {
      const firstDayOfYear = new Date(date.getFullYear(), 0, 1)
      const pastDaysOfYear = (date - firstDayOfYear) / 86400000
      return Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7)
    },

    getTimeAgo(dateString) {
      const now = new Date()
      const date = new Date(dateString)
      const diffInMinutes = Math.floor((now - date) / (1000 * 60))

      if (diffInMinutes < 1) return '방금 전'
      if (diffInMinutes < 60) return `${diffInMinutes}분 전`
      if (diffInMinutes < 1440) return `${Math.floor(diffInMinutes / 60)}시간 전`
      return `${Math.floor(diffInMinutes / 1440)}일 전`
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
    
    handleCreateRoom() {
      this.$router.push('/rooms/create')
    },
    
    handleViewReports() {
      this.$router.push('/admin/reports')
    },
    
    handleManageUsers() {
      this.$router.push('/users')
    },
    
    handleSystemSettings() {
      this.$router.push('/admin/settings')
    }
  }
}
</script>

<style scoped>
.admin-dashboard__stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-card__icon {
  font-size: 2rem;
  width: 3rem;
  height: 3rem;
  background-color: #f3f4f6;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card__content {
  flex: 1;
}

.stat-card__title {
  font-size: 0.875rem;
  font-weight: 500;
  color: #6b7280;
  margin: 0 0 0.5rem 0;
}

.stat-card__value {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 0.25rem 0;
}

.stat-card__change {
  font-size: 0.875rem;
  font-weight: 500;
  margin: 0;
}

.stat-card__change--positive {
  color: #059669;
}

.stat-card__change--negative {
  color: #dc2626;
}

.stat-card__change--neutral {
  color: #6b7280;
}

.admin-dashboard__recent {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.admin-dashboard__section {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}

.admin-dashboard__section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 1.5rem 0;
}

.admin-dashboard__table table {
  width: 100%;
  border-collapse: collapse;
}

.admin-dashboard__table th,
.admin-dashboard__table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #e5e7eb;
}

.admin-dashboard__table th {
  font-weight: 600;
  color: #374151;
  background-color: #f9fafb;
}

.status-pending {
  background-color: #fef3c7;
  color: #92400e;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-confirmed {
  background-color: #d1fae5;
  color: #065f46;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-cancelled {
  background-color: #fee2e2;
  color: #991b1b;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.status-completed {
  background-color: #e0e7ff;
  color: #3730a3;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.notification-item {
  display: flex;
  gap: 1rem;
  padding: 1rem 0;
  border-bottom: 1px solid #e5e7eb;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-item__icon {
  font-size: 1.5rem;
  width: 2.5rem;
  height: 2.5rem;
  background-color: #f3f4f6;
  border-radius: 0.375rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-item__content {
  flex: 1;
}

.notification-item__title {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 0.25rem 0;
}

.notification-item__message {
  font-size: 0.875rem;
  color: #6b7280;
  margin: 0 0 0.25rem 0;
  line-height: 1.4;
}

.notification-item__time {
  font-size: 0.75rem;
  color: #9ca3af;
}

.admin-dashboard__quick-actions {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
}

.admin-dashboard__actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

@media (max-width: 1024px) {
  .admin-dashboard__recent {
    grid-template-columns: 1fr;
  }
}

.admin-dashboard__loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 1rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f4f6;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .admin-dashboard__stats {
    grid-template-columns: 1fr;
  }
  
  .admin-dashboard__actions {
    flex-direction: column;
  }
  
  .stat-card {
    flex-direction: column;
    text-align: center;
  }
}
</style> 
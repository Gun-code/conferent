<template>
  <AdminLayout>
    <div class="admin-dashboard">
      <!-- 통계 카드 -->
      <div class="admin-dashboard__stats">
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
      <div class="admin-dashboard__recent">
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
      <div class="admin-dashboard__quick-actions">
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
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import AdminLayout from '@/layouts/AdminLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import { formatDate } from '@/utils/date'

export default {
  name: 'AdminDashboard',
  components: {
    AdminLayout,
    BaseButton
  },
  data() {
    return {
      stats: {
        totalRooms: 12,
        newRooms: 2,
        totalReservations: 156,
        newReservations: 23,
        activeUsers: 45,
        newUsers: 8,
        usageRate: 78,
        usageChange: 5
      },
      recentReservations: [
        {
          id: 1,
          roomName: '회의실 A',
          creatorName: '홍길동',
          purpose: '팀 미팅',
          startTime: '2024-01-15T10:00:00',
          status: 'CONFIRMED'
        },
        {
          id: 2,
          roomName: '회의실 B',
          creatorName: '김철수',
          purpose: '고객 미팅',
          startTime: '2024-01-15T14:00:00',
          status: 'PENDING'
        }
      ],
      notifications: [
        {
          id: 1,
          icon: '🔔',
          title: '새로운 예약 요청',
          message: '회의실 C에 대한 예약 요청이 있습니다.',
          time: '5분 전'
        },
        {
          id: 2,
          icon: '⚠️',
          title: '시스템 점검',
          message: '정기 시스템 점검이 예정되어 있습니다.',
          time: '1시간 전'
        }
      ]
    }
  },
  methods: {
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
      this.$router.push('/admin/rooms/create')
    },
    
    handleViewReports() {
      this.$router.push('/admin/reports')
    },
    
    handleManageUsers() {
      this.$router.push('/admin/users')
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
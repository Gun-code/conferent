<template>
  <UserLayout>
    <div class="room-list">
      <div class="room-list__header">
        <h1 class="room-list__title">회의실 목록</h1>
        <BaseButton 
          variant="primary" 
          @click="handleCreateRoom"
        >
          회의실 추가
        </BaseButton>
      </div>

      <div class="room-list__filters">
        <div class="room-list__filter-group">
          <label for="location-filter" class="room-list__filter-label">위치</label>
          <select 
            id="location-filter" 
            v-model="filters.location"
            class="room-list__filter-select"
            @change="handleFilterChange"
          >
            <option value="">전체</option>
            <option value="3층">3층</option>
            <option value="4층">4층</option>
            <option value="5층">5층</option>
          </select>
        </div>

        <div class="room-list__filter-group">
          <label for="capacity-filter" class="room-list__filter-label">최소 수용 인원</label>
          <select 
            id="capacity-filter" 
            v-model="filters.minCapacity"
            class="room-list__filter-select"
            @change="handleFilterChange"
          >
            <option value="">전체</option>
            <option value="2">2명 이상</option>
            <option value="5">5명 이상</option>
            <option value="10">10명 이상</option>
            <option value="20">20명 이상</option>
          </select>
        </div>
      </div>

      <div v-if="loading" class="room-list__loading">
        <p>회의실 목록을 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="room-list__error">
        <p>{{ error }}</p>
        <BaseButton 
          variant="primary" 
          @click="loadRooms"
        >
          다시 시도
        </BaseButton>
      </div>

      <div v-else-if="filteredRooms.length === 0" class="room-list__empty">
        <p>조건에 맞는 회의실이 없습니다.</p>
      </div>

      <div v-else class="room-list__grid">
        <div 
          v-for="room in filteredRooms" 
          :key="room.id"
          class="room-card"
        >
          <div class="room-card__header">
            <h3 class="room-card__title">{{ room.name }}</h3>
            <span class="room-card__location">{{ room.location }}</span>
          </div>
          
          <div class="room-card__content">
            <p class="room-card__capacity">
              수용 인원: {{ room.capacity }}명
            </p>
            <p v-if="room.description" class="room-card__description">
              {{ room.description }}
            </p>
          </div>
          
          <div class="room-card__actions">
            <BaseButton 
              variant="primary" 
              size="small"
              @click="handleViewRoom(room)"
            >
              상세보기
            </BaseButton>
            <BaseButton 
              variant="secondary" 
              size="small"
              @click="handleEditRoom(room)"
            >
              수정
            </BaseButton>
            <BaseButton 
              variant="danger" 
              size="small"
              @click="handleDeleteRoom(room)"
            >
              삭제
            </BaseButton>
          </div>
        </div>
      </div>
    </div>
  </UserLayout>
</template>

<script>
import UserLayout from '@/layouts/UserLayout.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import { roomApiClient } from '@/api'

export default {
  name: 'RoomList',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      rooms: [],
      loading: false,
      error: null,
      filters: {
        location: '',
        minCapacity: ''
      }
    }
  },
  computed: {
    filteredRooms() {
      let filtered = [...this.rooms]
      
      if (this.filters.location) {
        filtered = filtered.filter(room => 
          room.location === this.filters.location
        )
      }
      
      if (this.filters.minCapacity) {
        const minCapacity = parseInt(this.filters.minCapacity)
        filtered = filtered.filter(room => 
          room.capacity >= minCapacity
        )
      }
      
      return filtered
    }
  },
  mounted() {
    this.loadRooms()
  },
  methods: {
    async loadRooms() {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.getAllRooms()
        this.rooms = response.data
      } catch (err) {
        this.error = '회의실 목록을 불러오는데 실패했습니다.'
        console.error('Failed to load rooms:', err)
      } finally {
        this.loading = false
      }
    },
    
    handleFilterChange() {
      // 필터 변경 시 추가 로직이 필요한 경우 여기에 구현
    },
    
    handleCreateRoom() {
      this.$router.push('/rooms/create')
    },
    
    handleViewRoom(room) {
      this.$router.push(`/rooms/${room.id}`)
    },
    
    handleEditRoom(room) {
      this.$router.push(`/rooms/${room.id}/edit`)
    },
    
    async handleDeleteRoom(room) {
      if (confirm(`정말로 "${room.name}" 회의실을 삭제하시겠습니까?`)) {
        try {
          await roomApiClient.deleteRoom(room.id)
          this.rooms = this.rooms.filter(r => r.id !== room.id)
        } catch (err) {
          alert('회의실 삭제에 실패했습니다.')
          console.error('Failed to delete room:', err)
        }
      }
    }
  }
}
</script>

<style scoped>
.room-list__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.room-list__title {
  font-size: 2rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.room-list__filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.room-list__filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.room-list__filter-label {
  font-weight: 500;
  color: #374151;
  font-size: 0.875rem;
}

.room-list__filter-select {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  background-color: white;
  min-width: 150px;
}

.room-list__loading,
.room-list__error,
.room-list__empty {
  text-align: center;
  padding: 3rem 0;
  color: #6b7280;
}

.room-list__grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.room-card {
  background-color: white;
  border: 1px solid #e5e7eb;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s ease-in-out;
}

.room-card:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.room-card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.room-card__title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.room-card__location {
  background-color: #f3f4f6;
  color: #6b7280;
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.room-card__content {
  margin-bottom: 1.5rem;
}

.room-card__capacity {
  font-weight: 500;
  color: #374151;
  margin: 0 0 0.5rem 0;
}

.room-card__description {
  color: #6b7280;
  margin: 0;
  line-height: 1.5;
}

.room-card__actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

@media (max-width: 768px) {
  .room-list__header {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .room-list__title {
    font-size: 1.5rem;
    text-align: center;
  }
  
  .room-list__filters {
    flex-direction: column;
  }
  
  .room-list__grid {
    grid-template-columns: 1fr;
  }
}
</style> 
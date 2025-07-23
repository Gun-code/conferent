<template>
  <UserLayout>
    <div class="user-list">
      <div class="user-list__header">
        <h1 class="user-list__title">사용자 목록</h1>
        <BaseButton 
          variant="primary" 
          @click="handleCreateUser"
        >
          사용자 추가
        </BaseButton>
      </div>

      <div class="user-list__filters">
        <div class="user-list__filter-group">
          <label for="role-filter" class="user-list__filter-label">역할</label>
          <select 
            id="role-filter" 
            v-model="filters.role"
            class="user-list__filter-select"
            @change="handleFilterChange"
          >
            <option value="">전체</option>
            <option value="USER">일반 사용자</option>
            <option value="ADMIN">관리자</option>
          </select>
        </div>

        <div class="user-list__filter-group">
          <label for="search-filter" class="user-list__filter-label">검색</label>
          <input
            id="search-filter"
            v-model="filters.search"
            type="text"
            class="user-list__filter-input"
            placeholder="이름 또는 이메일 검색"
            @input="handleFilterChange"
          />
        </div>
      </div>

      <div v-if="loading" class="user-list__loading">
        <p>사용자 목록을 불러오는 중...</p>
      </div>

      <div v-else-if="error" class="user-list__error">
        <p>{{ error }}</p>
        <BaseButton 
          variant="primary" 
          @click="loadUsers"
        >
          다시 시도
        </BaseButton>
      </div>

      <div v-else-if="filteredUsers.length === 0" class="user-list__empty">
        <p>조건에 맞는 사용자가 없습니다.</p>
      </div>

      <div v-else class="user-list__grid">
        <div 
          v-for="user in filteredUsers" 
          :key="user.id"
          class="user-card"
        >
          <div class="user-card__avatar">
            {{ getInitials(user.name) }}
          </div>
          
          <div class="user-card__content">
            <div class="user-card__header">
              <h3 class="user-card__name">{{ user.name }}</h3>
              <span :class="getRoleClass(user.role)">
                {{ getRoleText(user.role) }}
              </span>
            </div>
            
            <div class="user-card__info">
              <p class="user-card__email">{{ user.email }}</p>
              <p v-if="user.department" class="user-card__department">
                {{ user.department }}
              </p>
              <p class="user-card__joined">
                가입일: {{ formatDate(user.createdAt) }}
              </p>
            </div>
          </div>
          
          <div class="user-card__actions">
            <BaseButton 
              variant="primary" 
              size="small"
              @click="handleViewUser(user)"
            >
              상세보기
            </BaseButton>
            <BaseButton 
              variant="secondary" 
              size="small"
              @click="handleEditUser(user)"
            >
              수정
            </BaseButton>
            <BaseButton 
              variant="danger" 
              size="small"
              @click="handleDeleteUser(user)"
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
import { userApiClient } from '@/api'
import { formatDate } from '@/utils/date'

export default {
  name: 'UserList',
  components: {
    UserLayout,
    BaseButton
  },
  data() {
    return {
      users: [],
      loading: false,
      error: null,
      filters: {
        role: '',
        search: ''
      }
    }
  },
  computed: {
    filteredUsers() {
      let filtered = [...this.users]
      
      if (this.filters.role) {
        filtered = filtered.filter(user => 
          user.role === this.filters.role
        )
      }
      
      if (this.filters.search) {
        const search = this.filters.search.toLowerCase()
        filtered = filtered.filter(user => 
          user.name.toLowerCase().includes(search) ||
          user.email.toLowerCase().includes(search)
        )
      }
      
      return filtered.sort((a, b) => a.name.localeCompare(b.name))
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      this.loading = true
      this.error = null
      
      try {
        const response = await userApiClient.getAllUsers()
        this.users = response.data
      } catch (err) {
        this.error = '사용자 목록을 불러오는데 실패했습니다.'
        console.error('Failed to load users:', err)
      } finally {
        this.loading = false
      }
    },
    
    handleFilterChange() {
      // 필터 변경 시 추가 로직이 필요한 경우 여기에 구현
    },
    
    formatDate(dateString) {
      return formatDate(dateString, 'date')
    },
    
    getInitials(name) {
      if (!name) return '?'
      return name.split(' ').map(n => n[0]).join('').toUpperCase()
    },
    
    getRoleClass(role) {
      const classes = {
        USER: 'role-user',
        ADMIN: 'role-admin'
      }
      return `user-card__role ${classes[role] || ''}`
    },
    
    getRoleText(role) {
      const texts = {
        USER: '일반 사용자',
        ADMIN: '관리자'
      }
      return texts[role] || role
    },
    
    handleCreateUser() {
      this.$router.push('/users/create')
    },
    
    handleViewUser(user) {
      this.$router.push(`/users/${user.id}`)
    },
    
    handleEditUser(user) {
      this.$router.push(`/users/${user.id}/edit`)
    },
    
    async handleDeleteUser(user) {
      if (confirm(`정말로 "${user.name}" 사용자를 삭제하시겠습니까?`)) {
        try {
          await userApiClient.deleteUser(user.id)
          this.users = this.users.filter(u => u.id !== user.id)
        } catch (err) {
          alert('사용자 삭제에 실패했습니다.')
          console.error('Failed to delete user:', err)
        }
      }
    }
  }
}
</script>

<style scoped>
@import '@/styles/pages/users.css';
</style> 
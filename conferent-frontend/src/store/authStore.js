import { defineStore } from 'pinia'
import { authApi } from '@/api'
import { extractErrorMessage, formatErrorForUser } from '@/utils/errorHandler.js'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // 사용자 정보 (User.java 기반)
    user: null,
    
    // 로그인 상태
    isAuthenticated: false,
    
    // JWT 토큰
    token: localStorage.getItem('token') || null,
    
    // 로딩 및 에러 상태
    loading: false,
    error: null,
    
    // 사용자 초대 목록 (userInvites)
    userInvites: []
  }),

  getters: {
    // 현재 사용자 ID
    userId: (state) => state.user?.id || null,
    
    // 현재 사용자 이름
    userName: (state) => state.user?.name || '',
    
    // 현재 사용자 이메일
    userEmail: (state) => state.user?.email || '',
    
    // 현재 사용자 역할
    userRole: (state) => state.user?.role || null,
    
    // 관리자 여부
    isAdmin: (state) => state.user?.role === 'ADMIN',
    
    // 일반 사용자 여부
    isUser: (state) => state.user?.role === 'USER',
    
    // 대기 중인 초대 수
    pendingInvitesCount: (state) => {
      return state.userInvites.filter(invite => invite.status === 'PENDING').length
    },
    
    // 수락된 초대 수
    acceptedInvitesCount: (state) => {
      return state.userInvites.filter(invite => invite.status === 'ACCEPTED').length
    },
    
    // 상태별 초대 그룹
    invitesByStatus: (state) => {
      return state.userInvites.reduce((groups, invite) => {
        const status = invite.status
        if (!groups[status]) {
          groups[status] = []
        }
        groups[status].push(invite)
        return groups
      }, {})
    }
  },

  actions: {
    // 로그인
    async login(credentials) {
      this.loading = true
      this.error = null
      
      try {
        console.log('로그인 시도:', credentials)
        const response = await authApi.login(credentials)
        console.log('백엔드 응답:', response)
        
        const data = response.data
        console.log('응답 데이터:', data)
        
        // 응답 데이터 유효성 검사
        if (!data) {
          throw new Error('응답 데이터가 없습니다.')
        }
        
        if (!data.accessToken) {
          throw new Error('accessToken이 응답에 없습니다.')
        }
        
        if (data.userId === undefined || data.userId === null) {
          throw new Error('userId가 응답에 없습니다.')
        }
        
        if (!data.userName) {
          throw new Error('userName이 응답에 없습니다.')
        }
        
        if (!data.userEmail) {
          throw new Error('userEmail이 응답에 없습니다.')
        }
        
        if (!data.userRole) {
          throw new Error('userRole이 응답에 없습니다.')
        }
        
        // 토큰과 사용자 정보 저장
        this.token = data.accessToken
        this.user = {
          id: data.userId,
          name: data.userName,
          email: data.userEmail,
          role: data.userRole
        }
        this.isAuthenticated = true
        
        // localStorage에 저장
        localStorage.setItem('token', data.accessToken)
        localStorage.setItem('userId', data.userId.toString())
        localStorage.setItem('userName', data.userName)
        localStorage.setItem('userEmail', data.userEmail)
        localStorage.setItem('userRole', data.userRole)
        
        console.log('로그인 성공, 사용자 정보:', this.user)
        
        // 사용자 초대 목록 불러오기 (에러가 발생해도 로그인은 성공으로 처리)
        try {
          await this.fetchUserInvites()
        } catch (inviteError) {
          console.warn('초대 목록 불러오기 실패:', inviteError)
        }
        
        return data
      } catch (err) {
        const errorInfo = formatErrorForUser(err)
        this.error = errorInfo.message
        console.error('Login failed:', errorInfo)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 회원가입
    async register(userData) {
      this.loading = true
      this.error = null
      
      try {
        const response = await authApi.register(userData)
        return response.data
      } catch (err) {
        this.error = '회원가입에 실패했습니다.'
        console.error('Registration failed:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 로그아웃
    async logout() {
      try {
        // 서버에 로그아웃 요청 (선택사항)
        await authApi.logout()
      } catch (err) {
        console.warn('Logout request failed:', err)
      } finally {
        // 클라이언트 상태 초기화
        this.token = null
        this.user = null
        this.isAuthenticated = false
        this.userInvites = []
        
        // localStorage 정리
        localStorage.removeItem('token')
        localStorage.removeItem('userId')
        localStorage.removeItem('userName')
        localStorage.removeItem('userEmail')
        localStorage.removeItem('userRole')
      }
    },

    // 토큰으로 사용자 정보 복원 (페이지 새로고침 시)
    async restoreUserFromToken() {
      const token = localStorage.getItem('token')
      
      if (!token) {
        return false
      }
      
      try {
        // 토큰 유효성 검증
        const response = await authApi.validateToken()
        
        if (response.data.valid) {
          // 토큰이 유효하면 사용자 정보 복원
          const userId = localStorage.getItem('userId')
          const userName = localStorage.getItem('userName')
          const userEmail = localStorage.getItem('userEmail')
          const userRole = localStorage.getItem('userRole')
          
          if (userId && userName && userRole) {
            this.token = token
            this.user = {
              id: parseInt(userId),
              name: userName,
              email: userEmail || '',
              role: userRole
            }
            this.isAuthenticated = true
            
            // 사용자 초대 목록 불러오기 (선택사항)
            try {
              await this.fetchUserInvites()
            } catch (error) {
              console.warn('Failed to fetch user invites during token restoration:', error)
            }
            
            return true
          }
        } else {
          // 토큰이 유효하지 않으면 로그아웃 처리
          console.warn('Token validation failed, logging out')
          await this.logout()
          return false
        }
      } catch (error) {
        console.error('Token validation error:', error)
        // 토큰 검증 실패 시 로그아웃 처리
        await this.logout()
        return false
      }
      
      return false
    },

    // 사용자 초대 목록 조회
    async fetchUserInvites() {
      if (!this.isAuthenticated) return
      
      this.loading = true
      this.error = null
      
      try {
        // 실제 API 엔드포인트에 맞게 수정 필요
        const response = await fetch('/api/user-invites', {
          headers: {
            'Authorization': `Bearer ${this.token}`
          }
        })
        
        if (response.ok) {
          this.userInvites = await response.json()
        }
      } catch (err) {
        console.error('Failed to fetch user invites:', err)
      } finally {
        this.loading = false
      }
    },

    // 초대 상태 업데이트
    async updateInviteStatus(inviteId, status) {
      this.loading = true
      this.error = null
      
      try {
        // 실제 API 엔드포인트에 맞게 수정 필요
        const response = await fetch(`/api/user-invites/${inviteId}`, {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${this.token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ status })
        })
        
        if (response.ok) {
          // 로컬 상태 업데이트
          const inviteIndex = this.userInvites.findIndex(invite => invite.id === inviteId)
          if (inviteIndex !== -1) {
            this.userInvites[inviteIndex].status = status
          }
        }
        
        return response.ok
      } catch (err) {
        this.error = '초대 상태 업데이트에 실패했습니다.'
        console.error('Failed to update invite status:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 사용자 정보 업데이트
    updateUser(userData) {
      if (this.user) {
        this.user = { ...this.user, ...userData }
        
        // localStorage 동기화
        if (userData.name) localStorage.setItem('userName', userData.name)
        if (userData.email) localStorage.setItem('userEmail', userData.email)
        if (userData.role) localStorage.setItem('userRole', userData.role)
      }
    },

    // 초대 추가 (새 초대 받았을 때)
    addInvite(invite) {
      this.userInvites.push(invite)
    },

    // 초대 제거
    removeInvite(inviteId) {
      this.userInvites = this.userInvites.filter(invite => invite.id !== inviteId)
    },

    // 에러 초기화
    clearError() {
      this.error = null
    },

    // 상태 초기화
    reset() {
      this.user = null
      this.isAuthenticated = false
      this.token = null
      this.loading = false
      this.error = null
      this.userInvites = []
      
      // localStorage 정리
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('userName')
      localStorage.removeItem('userEmail')
      localStorage.removeItem('userRole')
    }
  }
}) 
import { defineStore } from 'pinia'
import { roomApiClient } from '@/api'

export const useRoomStore = defineStore('room', {
  state: () => ({
    rooms: [],
    currentRoom: null,
    loading: false,
    error: null,
    filters: {
      location: '',
      minCapacity: ''
    }
  }),

  getters: {
    // 필터링된 회의실 목록
    filteredRooms: (state) => {
      let filtered = [...state.rooms]
      
      if (state.filters.location) {
        filtered = filtered.filter(room => 
          room.location === state.filters.location
        )
      }
      
      if (state.filters.minCapacity) {
        const minCapacity = parseInt(state.filters.minCapacity)
        filtered = filtered.filter(room => 
          room.capacity >= minCapacity
        )
      }
      
      return filtered
    },

    // 위치별 회의실 그룹
    roomsByLocation: (state) => {
      return state.rooms.reduce((groups, room) => {
        const location = room.location
        if (!groups[location]) {
          groups[location] = []
        }
        groups[location].push(room)
        return groups
      }, {})
    },

    // 용량별 회의실 그룹
    roomsByCapacity: (state) => {
      return state.rooms.reduce((groups, room) => {
        const capacity = room.capacity
        if (!groups[capacity]) {
          groups[capacity] = []
        }
        groups[capacity].push(room)
        return groups
      }, {})
    },

    // 총 회의실 수
    totalRooms: (state) => state.rooms.length,

    // 평균 수용 인원
    averageCapacity: (state) => {
      if (state.rooms.length === 0) return 0
      const totalCapacity = state.rooms.reduce((sum, room) => sum + room.capacity, 0)
      return Math.round(totalCapacity / state.rooms.length)
    }
  },

  actions: {
    // 모든 회의실 조회
    async fetchRooms() {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.getAllRooms()
        this.rooms = response.data
      } catch (err) {
        this.error = '회의실 목록을 불러오는데 실패했습니다.'
        console.error('Failed to fetch rooms:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // ID로 회의실 조회
    async fetchRoomById(id) {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.getRoomById(id)
        this.currentRoom = response.data
        return response.data
      } catch (err) {
        this.error = '회의실 정보를 불러오는데 실패했습니다.'
        console.error('Failed to fetch room:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 회의실 생성
    async createRoom(roomData) {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.createRoom(roomData)
        this.rooms.push(response.data)
        return response.data
      } catch (err) {
        this.error = '회의실 생성에 실패했습니다.'
        console.error('Failed to create room:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 회의실 수정
    async updateRoom(id, roomData) {
      this.loading = true
      this.error = null
      
      try {
        const response = await roomApiClient.updateRoom(id, roomData)
        const index = this.rooms.findIndex(room => room.id === id)
        if (index !== -1) {
          this.rooms[index] = response.data
        }
        if (this.currentRoom && this.currentRoom.id === id) {
          this.currentRoom = response.data
        }
        return response.data
      } catch (err) {
        this.error = '회의실 수정에 실패했습니다.'
        console.error('Failed to update room:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 회의실 삭제
    async deleteRoom(id) {
      this.loading = true
      this.error = null
      
      try {
        await roomApiClient.deleteRoom(id)
        this.rooms = this.rooms.filter(room => room.id !== id)
        if (this.currentRoom && this.currentRoom.id === id) {
          this.currentRoom = null
        }
      } catch (err) {
        this.error = '회의실 삭제에 실패했습니다.'
        console.error('Failed to delete room:', err)
        throw err
      } finally {
        this.loading = false
      }
    },

    // 필터 설정
    setFilters(filters) {
      this.filters = { ...this.filters, ...filters }
    },

    // 필터 초기화
    clearFilters() {
      this.filters = {
        location: '',
        minCapacity: ''
      }
    },

    // 현재 회의실 설정
    setCurrentRoom(room) {
      this.currentRoom = room
    },

    // 현재 회의실 초기화
    clearCurrentRoom() {
      this.currentRoom = null
    },

    // 에러 초기화
    clearError() {
      this.error = null
    },

    // 상태 초기화
    reset() {
      this.rooms = []
      this.currentRoom = null
      this.loading = false
      this.error = null
      this.filters = {
        location: '',
        minCapacity: ''
      }
    }
  }
}) 
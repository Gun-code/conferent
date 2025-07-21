import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { RoomEntity } from '../models/RoomModel'
import { RoomBusinessService } from '../services/RoomBusinessService'
import { ApiRoomDataRepository } from '../../data/repositories/RoomDataRepository'

/**
 * 회의실 비즈니스 스토어 (Pinia)
 * @returns {Object} 회의실 스토어 객체
 */
export const useRoomStore = defineStore('room', () => {
  // 의존성 주입
  const roomDataRepo = new ApiRoomDataRepository()
  const roomService = new RoomBusinessService(roomDataRepo)

  // State
  /** @type {import('vue').Ref<RoomEntity[]>} 회의실 목록 */
  const rooms = ref([])
  
  /** @type {import('vue').Ref<RoomEntity|null>} 선택된 회의실 */
  const selectedRoom = ref(null)
  
  /** @type {import('vue').Ref<boolean>} 로딩 상태 */
  const loading = ref(false)
  
  /** @type {import('vue').Ref<string|null>} 에러 메시지 */
  const error = ref(null)

  // Getters (계산된 속성)
  /**
   * 사용 가능한 회의실 목록
   * @type {import('vue').ComputedRef<RoomEntity[]>}
   */
  const availableRooms = computed(() => rooms.value)
  
  /**
   * 최소 수용 인원으로 필터링된 회의실 목록을 반환하는 함수
   * @type {import('vue').ComputedRef<function(number): RoomEntity[]>}
   */
  const roomsByCapacity = computed(() => 
    /**
     * @param {number} minCapacity - 최소 수용 인원
     * @returns {RoomEntity[]} 조건에 맞는 회의실 목록
     */
    (minCapacity) => rooms.value.filter(room => room.canAccommodate(minCapacity))
  )

  // Actions (액션 메서드들)
  /**
   * 회의실 목록 조회
   * @param {number} [minCapacity] - 최소 수용 인원 (선택사항)
   * @returns {Promise<void>}
   * @throws {Error} 회의실 조회 실패 시
   */
  async function fetchRooms(minCapacity) {
    loading.value = true
    error.value = null
    
    try {
      const roomList = await roomService.getRooms(minCapacity)
      rooms.value = roomList
    } catch (err) {
      const errorMessage = err?.message || '회의실 목록 조회에 실패했습니다.'
      error.value = errorMessage
      rooms.value = []
      console.error('회의실 조회 실패:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * 특정 회의실 정보 조회
   * @param {number} id - 회의실 ID
   * @returns {Promise<void>}
   * @throws {Error} 회의실 조회 실패 시
   */
  async function fetchRoomById(id) {
    loading.value = true
    error.value = null
    
    try {
      const room = await roomService.getRoomById(id)
      selectedRoom.value = room
    } catch (err) {
      const errorMessage = err?.message || '회의실 정보 조회에 실패했습니다.'
      error.value = errorMessage
      selectedRoom.value = null
      console.error('회의실 정보 조회 실패:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 새 회의실 생성
   * @param {CreateRoomRequest} request - 회의실 생성 요청 데이터
   * @returns {Promise<void>}
   * @throws {Error} 회의실 생성 실패 시
   */
  async function createRoom(request) {
    loading.value = true
    error.value = null
    
    try {
      const newRoom = await roomService.createRoom(request)
      rooms.value.push(newRoom)
    } catch (err) {
      const errorMessage = err?.message || '회의실 생성에 실패했습니다.'
      error.value = errorMessage
      console.error('회의실 생성 실패:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 선택된 회의실 설정
   * @param {RoomEntity|null} room - 설정할 회의실 엔티티
   * @returns {void}
   */
  function setSelectedRoom(room) {
    selectedRoom.value = room
  }

  /**
   * 에러 상태 초기화
   * @returns {void}
   */
  function clearError() {
    error.value = null
  }

  /**
   * 회의실 목록 및 선택 상태 초기화
   * @returns {void}
   */
  function clearRooms() {
    rooms.value = []
    selectedRoom.value = null
  }

  return {
    // State
    rooms,
    selectedRoom,
    loading,
    error,
    
    // Getters
    availableRooms,
    roomsByCapacity,
    
    // Actions
    fetchRooms,
    fetchRoomById,
    createRoom,
    setSelectedRoom,
    clearError,
    clearRooms
  }
}) 
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ReservationEntity } from '../models/ReservationModel'
import { ReservationBusinessService } from '../services/ReservationBusinessService'
import { ApiReservationDataRepository } from '../../data/repositories/ReservationDataRepository'

/**
 * 예약 비즈니스 스토어 (Pinia)
 * @returns {Object} 예약 스토어 객체
 */
export const useReservationStore = defineStore('reservation', () => {
  // 의존성 주입
  const reservationDataRepo = new ApiReservationDataRepository()
  const reservationService = new ReservationBusinessService(reservationDataRepo)

  // State
  /** @type {import('vue').Ref<ReservationEntity[]>} 예약 목록 */
  const reservations = ref([])
  
  /** @type {import('vue').Ref<boolean>} 로딩 상태 */
  const loading = ref(false)
  
  /** @type {import('vue').Ref<string|null>} 에러 메시지 */
  const error = ref(null)

  // Getters (계산된 속성)
  /**
   * 사용자 예약 목록
   * @type {import('vue').ComputedRef<ReservationEntity[]>}
   */
  const userReservations = computed(() => reservations.value)
  
  /**
   * 대기 중인 예약 목록
   * @type {import('vue').ComputedRef<ReservationEntity[]>}
   */
  const pendingReservations = computed(() => 
    reservations.value.filter(r => r.status === 'PENDING')
  )
  
  /**
   * 확정된 예약 목록
   * @type {import('vue').ComputedRef<ReservationEntity[]>}
   */
  const confirmedReservations = computed(() => 
    reservations.value.filter(r => r.status === 'CONFIRMED')
  )

  // Actions (액션 메서드들)
  /**
   * 새 예약 생성
   * @param {CreateReservationRequest} request - 예약 생성 요청 데이터
   * @returns {Promise<void>}
   * @throws {Error} 예약 생성 실패 시
   */
  async function createReservation(request) {
    loading.value = true
    error.value = null
    
    try {
      const newReservation = await reservationService.createReservation(request)
      reservations.value.push(newReservation)
    } catch (err) {
      const errorMessage = err?.message || '예약 생성에 실패했습니다.'
      error.value = errorMessage
      console.error('예약 생성 실패:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 예약 취소
   * @param {number} reservationId - 예약 ID
   * @param {number} userId - 사용자 ID
   * @returns {Promise<void>}
   * @throws {Error} 예약 취소 실패 시
   */
  async function cancelReservation(reservationId, userId) {
    loading.value = true
    error.value = null
    
    try {
      const reservation = reservations.value.find(r => r.id === reservationId)
      if (!reservation) {
        throw new Error('예약을 찾을 수 없습니다.')
      }

      await reservationService.cancelReservation(reservationId, userId, reservation)
      
      // 로컬 상태 업데이트
      const index = reservations.value.findIndex(r => r.id === reservationId)
      if (index !== -1) {
        reservations.value.splice(index, 1)
      }
    } catch (err) {
      const errorMessage = err?.message || '예약 취소에 실패했습니다.'
      error.value = errorMessage
      console.error('예약 취소 실패:', err)
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 사용자의 예약 목록 조회
   * @param {number} userId - 사용자 ID
   * @returns {Promise<void>}
   * @throws {Error} 예약 조회 실패 시
   */
  async function fetchUserReservations(userId) {
    loading.value = true
    error.value = null
    
    try {
      const userReservationList = await reservationService.getUserReservations(userId)
      reservations.value = userReservationList
    } catch (err) {
      const errorMessage = err?.message || '예약 목록 조회에 실패했습니다.'
      error.value = errorMessage
      reservations.value = []
      console.error('예약 조회 실패:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * 에러 상태 초기화
   * @returns {void}
   */
  function clearError() {
    error.value = null
  }

  /**
   * 예약 목록 초기화
   * @returns {void}
   */
  function clearReservations() {
    reservations.value = []
  }

  return {
    // State
    reservations,
    loading,
    error,
    
    // Getters
    userReservations,
    pendingReservations,
    confirmedReservations,
    
    // Actions
    createReservation,
    cancelReservation,
    fetchUserReservations,
    clearError,
    clearReservations
  }
}) 
import apiClient from '../api/ApiClient.js'
import { ReservationEntity, ReservationStatus } from '../../business/models/ReservationModel.js'

/**
 * 예약 생성 요청 데이터
 * @typedef {Object} CreateReservationRequest
 * @property {number} userId - 사용자 ID
 * @property {number} roomId - 회의실 ID  
 * @property {string} startTime - 시작 시간 (ISO 8601)
 * @property {string} endTime - 종료 시간 (ISO 8601)
 * @property {string} purpose - 예약 목적
 */

/**
 * 예약 응답 데이터
 * @typedef {Object} ReservationResponse
 * @property {number} id - 예약 ID
 * @property {number} userId - 사용자 ID
 * @property {number} roomId - 회의실 ID
 * @property {string} roomName - 회의실 이름
 * @property {string} startTime - 시작 시간 (ISO 8601)
 * @property {string} endTime - 종료 시간 (ISO 8601)
 * @property {string} purpose - 예약 목적
 * @property {string} status - 예약 상태
 */

/**
 * 예약 데이터 저장소 인터페이스
 */
export class ReservationDataRepository {
  
  /**
   * 새 예약 생성
   * @param {CreateReservationRequest} request - 예약 생성 요청 데이터
   * @returns {Promise<ReservationEntity>} 생성된 예약 엔티티
   * @throws {Error} API 오류 또는 데이터 변환 오류
   */
  async createReservation(request) {
    try {
      // 입력 데이터 검증
      if (!request || typeof request !== 'object') {
        throw new Error('잘못된 예약 요청 데이터입니다.')
      }
      
      const { userId, roomId, startTime, endTime, purpose } = request
      
      if (!userId || !roomId || !startTime || !endTime || !purpose) {
        throw new Error('필수 예약 정보가 누락되었습니다.')
      }

      /** @type {ReservationResponse} */
      const response = await apiClient.post('/reservations', {
        userId,
        roomId,
        startTime,
        endTime,
        purpose
      })

      return this._mapToEntity(response.data)
    } catch (error) {
      console.error('예약 생성 실패:', error)
      throw error
    }
  }

  /**
   * 사용자의 예약 목록 조회
   * @param {number} userId - 사용자 ID
   * @returns {Promise<ReservationEntity[]>} 예약 엔티티 배열
   * @throws {Error} API 오류 또는 데이터 변환 오류
   */
  async getUserReservations(userId) {
    try {
      if (!userId || typeof userId !== 'number') {
        throw new Error('유효하지 않은 사용자 ID입니다.')
      }

      /** @type {ReservationResponse[]} */
      const response = await apiClient.get(`/reservations/user/${userId}`)
      
      return (response.data || []).map(item => this._mapToEntity(item))
    } catch (error) {
      console.error('사용자 예약 조회 실패:', error)
      throw error
    }
  }

  /**
   * 예약 취소
   * @param {number} reservationId - 예약 ID
   * @returns {Promise<void>}
   * @throws {Error} API 오류
   */
  async cancelReservation(reservationId) {
    try {
      if (!reservationId || typeof reservationId !== 'number') {
        throw new Error('유효하지 않은 예약 ID입니다.')
      }

      await apiClient.delete(`/reservations/${reservationId}`)
    } catch (error) {
      console.error('예약 취소 실패:', error)
      throw error
    }
  }

  /**
   * API 응답을 ReservationEntity로 변환
   * @private
   * @param {ReservationResponse} data - API 응답 데이터
   * @returns {ReservationEntity} 예약 엔티티
   * @throws {Error} 데이터 변환 오류
   */
  _mapToEntity(data) {
    try {
      if (!data || typeof data !== 'object') {
        throw new Error('변환할 예약 데이터가 없습니다.')
      }

      return new ReservationEntity({
        id: Number(data.id),
        userId: Number(data.userId),
        roomId: Number(data.roomId),
        roomName: String(data.roomName || ''),
        startTime: new Date(data.startTime),
        endTime: new Date(data.endTime),
        purpose: String(data.purpose || ''),
        status: data.status || ReservationStatus.PENDING
      })
    } catch (error) {
      console.error('예약 데이터 변환 실패:', error, data)
      throw new Error('예약 데이터 변환 중 오류가 발생했습니다.')
    }
  }
} 
import apiClient from '@/api/ApiClient'

/**
 * 예약 API 클라이언트
 */
export const rentApiClient = {
  /**
   * 모든 예약 목록 조회
   * GET /api/rents
   * @returns {Promise} 예약 목록
   */
  async getAll() {
    const response = await apiClient.get('/rents')
    return response
  },

  /**
   * 예약 상세 조회
   * GET /api/rents/{id}
   * @param {number} id - 예약 ID
   * @returns {Promise} 예약 상세 정보
   */
  async getById(id) {
    const response = await apiClient.get(`/rents/${id}`)
    return response
  },

  /**
   * 사용자별 예약 조회
   * GET /api/rents/creator/{creatorId}
   * @param {number} creatorId - 예약 생성자 ID
   * @returns {Promise} 사용자별 예약 목록
   */
  async getByCreator(creatorId) {
    const response = await apiClient.get(`/rents/creator/${creatorId}`)
    return response
  },

  /**
   * 날짜 범위별 예약 조회
   * GET /api/rents/date-range
   * @param {string} startDate - 시작 날짜 (ISO 형식)
   * @param {string} endDate - 종료 날짜 (ISO 형식)
   * @returns {Promise} 날짜 범위별 예약 목록
   */
  async getByDateRange(startDate, endDate) {
    const params = new URLSearchParams({
      startDate: startDate,
      endDate: endDate
    })
    const response = await apiClient.get(`/rents/date-range?${params}`)
    return response
  },

  /**
   * 회의실별 예약 조회
   * GET /api/rents/room/{roomId}
   * @param {number} roomId - 회의실 ID
   * @returns {Promise} 회의실별 예약 목록
   */
  async getByRoom(roomId) {
    const response = await apiClient.get(`/rents/room/${roomId}`)
    return response
  },

  /**
   * 최근 예약 조회
   * GET /api/rents/recent?userId=
   * @param {number} userId - 사용자 ID
   * @returns {Promise} 최근 예약 목록
   */
  async getRecent(userId) {
    const response = await apiClient.get(`/rents/recent?userId=${userId}`)
    return response
  },

  /**
   * 시간 충돌 확인
   * GET /api/rents/conflicts
   * @param {number[]} roomIds - 회의실 ID 목록
   * @param {string} startTime - 시작 시간 (ISO 형식)
   * @param {string} endTime - 종료 시간 (ISO 형식)
   * @returns {Promise} 충돌 여부
   */
  async checkTimeConflict(roomIds, startTime, endTime) {
    const params = new URLSearchParams({
      startTime: startTime,
      endTime: endTime
    })
    roomIds.forEach(id => params.append('roomIds', id))
    
    const response = await apiClient.get(`/rents/conflicts?${params}`)
    return response
  },

  /**
   * 예약 생성
   * POST /api/rents
   * @param {Object} rentData - 예약 생성 데이터
   * @param {string} rentData.startTime - 시작 시간
   * @param {string} rentData.endTime - 종료 시간
   * @param {string} rentData.purpose - 목적
   * @param {string} rentData.description - 설명
   * @param {number} rentData.userId - 사용자 ID
   * @param {number[]} rentData.roomIds - 회의실 ID 목록
   * @param {number[]} rentData.inviteeIds - 초대할 사용자 ID 목록
   * @returns {Promise} 생성된 예약 정보
   */
  async create(rentData) {
    const response = await apiClient.post('/rents', rentData)
    return response
  },

  /**
   * 예약 수정
   * PUT /api/rents/{id}
   * @param {number} id - 예약 ID
   * @param {Object} rentData - 수정할 예약 데이터
   * @returns {Promise} 수정된 예약 정보
   */
  async update(id, rentData) {
    const response = await apiClient.put(`/rents/${id}`, rentData)
    return response
  },

  /**
   * 예약 삭제
   * DELETE /api/rents/{id}
   * @param {number} id - 예약 ID
   * @returns {Promise} 삭제 응답
   */
  async delete(id) {
    const response = await apiClient.delete(`/rents/${id}`)
    return response
  }
} 
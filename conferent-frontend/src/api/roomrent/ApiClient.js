import apiClient from '@/api/ApiClient'

/**
 * 회의실-예약 연결 API 클라이언트
 */
export const roomRentApiClient = {
  /**
   * 모든 회의실-예약 연결 목록 조회
   * GET /api/room-rents
   * @returns {Promise} 연결 목록
   */
  async getAll() {
    const response = await apiClient.get('/room-rents')
    return response
  },

  /**
   * 회의실-예약 연결 상세 조회
   * GET /api/room-rents/{id}
   * @param {number} id - 연결 ID
   * @returns {Promise} 연결 상세 정보
   */
  async getById(id) {
    const response = await apiClient.get(`/room-rents/${id}`)
    return response
  },

  /**
   * 예약별 회의실 조회
   * GET /api/room-rents/rent/{rentId}
   * @param {number} rentId - 예약 ID
   * @returns {Promise} 예약에 연결된 회의실 목록
   */
  async getByRentId(rentId) {
    const response = await apiClient.get(`/room-rents/rent/${rentId}`)
    return response
  },

  /**
   * 회의실별 예약 조회
   * GET /api/room-rents/room/{roomId}
   * @param {number} roomId - 회의실 ID
   * @returns {Promise} 회의실에 연결된 예약 목록
   */
  async getByRoomId(roomId) {
    const response = await apiClient.get(`/room-rents/room/${roomId}`)
    return response
  },

  /**
   * 이용 가능한 회의실 조회
   * GET /api/room-rents/available
   * @returns {Promise} 이용 가능한 회의실 목록
   */
  async getAvailableRooms() {
    const response = await apiClient.get('/room-rents/available')
    return response
  },

  /**
   * 시간 충돌 확인
   * GET /api/room-rents/conflicts
   * @param {number} roomId - 회의실 ID
   * @param {string} startTime - 시작 시간 (ISO 형식)
   * @param {string} endTime - 종료 시간 (ISO 형식)
   * @returns {Promise} 충돌하는 예약 목록
   */
  async findConflictingReservations(roomId, startTime, endTime) {
    const params = new URLSearchParams({
      roomId: roomId,
      startTime: startTime,
      endTime: endTime
    })
    const response = await apiClient.get(`/room-rents/conflicts?${params}`)
    return response
  },

  /**
   * 연결 존재 여부 확인
   * GET /api/room-rents/exists
   * @param {number} roomId - 회의실 ID
   * @param {number} rentId - 예약 ID
   * @returns {Promise} 연결 존재 여부
   */
  async exists(roomId, rentId) {
    const params = new URLSearchParams({
      roomId: roomId,
      rentId: rentId
    })
    const response = await apiClient.get(`/room-rents/exists?${params}`)
    return response
  },

  /**
   * 연결 조회
   * GET /api/room-rents/find
   * @param {number} rentId - 예약 ID
   * @param {number} roomId - 회의실 ID
   * @returns {Promise} 연결 정보
   */
  async findByRentIdAndRoomId(rentId, roomId) {
    const params = new URLSearchParams({
      rentId: rentId,
      roomId: roomId
    })
    const response = await apiClient.get(`/room-rents/find?${params}`)
    return response
  },

  /**
   * 회의실-예약 연결 생성
   * POST /api/room-rents
   * @param {Object} connectionData - 연결 생성 데이터
   * @param {number} connectionData.roomId - 회의실 ID
   * @param {number} connectionData.rentId - 예약 ID
   * @returns {Promise} 생성된 연결 정보
   */
  async create(connectionData) {
    const response = await apiClient.post('/room-rents', connectionData)
    return response
  },

  /**
   * 회의실-예약 연결 삭제
   * DELETE /api/room-rents/{id}
   * @param {number} id - 연결 ID
   * @returns {Promise} 삭제 응답
   */
  async delete(id) {
    const response = await apiClient.delete(`/room-rents/${id}`)
    return response
  },

  /**
   * 예약별 연결 삭제
   * DELETE /api/room-rents/rent/{rentId}
   * @param {number} rentId - 예약 ID
   * @returns {Promise} 삭제 응답
   */
  async deleteByRentId(rentId) {
    const response = await apiClient.delete(`/room-rents/rent/${rentId}`)
    return response
  },

  /**
   * 회의실별 연결 삭제
   * DELETE /api/room-rents/room/{roomId}
   * @param {number} roomId - 회의실 ID
   * @returns {Promise} 삭제 응답
   */
  async deleteByRoomId(roomId) {
    const response = await apiClient.delete(`/room-rents/room/${roomId}`)
    return response
  }
} 
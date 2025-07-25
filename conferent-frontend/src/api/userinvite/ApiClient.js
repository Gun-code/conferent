import apiClient from '@/api/ApiClient'

/**
 * 사용자 초대 API 클라이언트
 */
export const userInviteApiClient = {
  /**
   * 모든 사용자 초대 목록 조회
   * GET /api/user-invites
   * @returns {Promise} 초대 목록
   */
  async getAll() {
    const response = await apiClient.get('/user-invites')
    return response
  },

  /**
   * 사용자 초대 상세 조회
   * GET /api/user-invites/{id}
   * @param {number} id - 초대 ID
   * @returns {Promise} 초대 상세 정보
   */
  async getById(id) {
    const response = await apiClient.get(`/user-invites/${id}`)
    return response
  },

  /**
   * 사용자별 초대 조회
   * GET /api/user-invites/user/{userId}
   * @param {number} userId - 사용자 ID
   * @returns {Promise} 사용자별 초대 목록
   */
  async getByUserId(userId) {
    const response = await apiClient.get(`/user-invites/user/${userId}`)
    return response
  },

  /**
   * 예약별 초대 조회
   * GET /api/user-invites/rent/{rentId}
   * @param {number} rentId - 예약 ID
   * @returns {Promise} 예약별 초대 목록
   */
  async getByRentId(rentId) {
    const response = await apiClient.get(`/user-invites/rent/${rentId}`)
    return response
  },

  /**
   * 사용자별 상태별 초대 조회
   * GET /api/user-invites/user/{userId}/status/{status}
   * @param {number} userId - 사용자 ID
   * @param {string} status - 초대 상태 (PENDING, ACCEPTED, DECLINED)
   * @returns {Promise} 상태별 초대 목록
   */
  async getByUserIdAndStatus(userId, status) {
    const response = await apiClient.get(`/user-invites/user/${userId}/status/${status.toUpperCase()}`)
    return response
  },

  /**
   * 대기 중인 초대 개수 조회
   * GET /api/user-invites/user/{userId}/pending-count
   * @param {number} userId - 사용자 ID
   * @returns {Promise} 대기 중인 초대 개수
   */
  async getPendingCount(userId) {
    const response = await apiClient.get(`/user-invites/user/${userId}/pending-count`)
    return response
  },

  /**
   * 수락된 초대 개수 조회
   * GET /api/user-invites/rent/{rentId}/accepted-count
   * @param {number} rentId - 예약 ID
   * @returns {Promise} 수락된 초대 개수
   */
  async getAcceptedCountForRent(rentId) {
    const response = await apiClient.get(`/user-invites/rent/${rentId}/accepted-count`)
    return response
  },

  /**
   * 초대 존재 여부 확인
   * GET /api/user-invites/exists
   * @param {number} userId - 사용자 ID
   * @param {number} roomRentId - RoomRent ID
   * @returns {Promise} 초대 존재 여부
   */
  async exists(userId, roomRentId) {
    const params = new URLSearchParams({
      userId: userId,
      roomRentId: roomRentId
    })
    const response = await apiClient.get(`/user-invites/exists?${params}`)
    return response
  },

  /**
   * 초대 조회
   * GET /api/user-invites/find
   * @param {number} userId - 사용자 ID
   * @param {number} roomRentId - RoomRent ID
   * @returns {Promise} 초대 정보
   */
  async findByUserIdAndRoomRentId(userId, roomRentId) {
    const params = new URLSearchParams({
      userId: userId,
      roomRentId: roomRentId
    })
    const response = await apiClient.get(`/user-invites/find?${params}`)
    return response
  },

  /**
   * 사용자 초대 생성
   * POST /api/user-invites
   * @param {number} userId - 사용자 ID
   * @param {number} roomRentId - RoomRent ID
   * @returns {Promise} 생성된 초대 정보
   */
  async create(userId, roomRentId) {
    const params = new URLSearchParams({
      userId: userId,
      roomRentId: roomRentId
    })
    const response = await apiClient.post(`/user-invites?${params}`)
    return response
  },

  /**
   * 초대 상태 업데이트
   * PUT /api/user-invites/{id}/status
   * @param {number} id - 초대 ID
   * @param {string} status - 새로운 상태 (PENDING, ACCEPTED, DECLINED)
   * @returns {Promise} 업데이트된 초대 정보
   */
  async updateStatus(id, status) {
    const params = new URLSearchParams({
      status: status.toUpperCase()
    })
    const response = await apiClient.put(`/user-invites/${id}/status?${params}`)
    return response
  },

  /**
   * 사용자 초대 삭제
   * DELETE /api/user-invites/{id}
   * @param {number} id - 초대 ID
   * @returns {Promise} 삭제 응답
   */
  async delete(id) {
    const response = await apiClient.delete(`/user-invites/${id}`)
    return response
  },

  /**
   * 사용자별 초대 삭제
   * DELETE /api/user-invites/user/{userId}
   * @param {number} userId - 사용자 ID
   * @returns {Promise} 삭제 응답
   */
  async deleteByUserId(userId) {
    const response = await apiClient.delete(`/user-invites/user/${userId}`)
    return response
  },

  /**
   * RoomRent별 초대 삭제
   * DELETE /api/user-invites/room-rent/{roomRentId}
   * @param {number} roomRentId - RoomRent ID
   * @returns {Promise} 삭제 응답
   */
  async deleteByRoomRentId(roomRentId) {
    const response = await apiClient.delete(`/user-invites/room-rent/${roomRentId}`)
    return response
  }
} 
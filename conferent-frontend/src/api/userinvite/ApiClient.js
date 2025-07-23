import apiClient from '@/api/ApiClient';

/**
 * UserInvite API 클라이언트
 * 사용자 초대 관리 API
 */
export const userInviteApiClient = {
  /**
   * 모든 사용자 초대 목록 조회
   * GET /api/user-invites
   * @returns {Promise<Array>} 사용자 초대 목록
   */
  getAllUserInvites: () => apiClient.get('/api/user-invites'),

  /**
   * ID로 사용자 초대 조회
   * GET /api/user-invites/{id}
   * @param {number} id - 사용자 초대 ID
   * @returns {Promise<Object>} 사용자 초대 정보
   */
  getUserInviteById: (id) => apiClient.get(`/api/user-invites/${id}`),

  /**
   * 사용자별 초대 목록 조회
   * GET /api/user-invites/user/{userId}
   * @param {number} userId - 사용자 ID
   * @returns {Promise<Array>} 조건에 맞는 초대 목록
   */
  getUserInvitesByUser: (userId) => apiClient.get(`/api/user-invites/user/${userId}`),

  /**
   * 예약별 초대 목록 조회
   * GET /api/user-invites/rent/{rentId}
   * @param {number} rentId - 예약 ID
   * @returns {Promise<Array>} 조건에 맞는 초대 목록
   */
  getUserInvitesByRent: (rentId) => apiClient.get(`/api/user-invites/rent/${rentId}`),

  /**
   * 사용자별 상태별 초대 조회
   * GET /api/user-invites/user/{userId}/status/{status}
   * @param {number} userId - 사용자 ID
   * @param {string} status - 초대 상태 (PENDING/ACCEPTED/DECLINED)
   * @returns {Promise<Array>} 조건에 맞는 초대 목록
   */
  getUserInvitesByUserAndStatus: (userId, status) => 
    apiClient.get(`/api/user-invites/user/${userId}/status/${status}`),

  /**
   * 사용자 초대 생성
   * POST /api/user-invites
   * @param {Object} inviteData - 초대 생성 데이터
   * @param {number} inviteData.userId - 사용자 ID
   * @param {number} inviteData.roomRentId - 회의실-예약 연결 ID
   * @returns {Promise<Object>} 생성된 초대 정보
   */
  createUserInvite: (inviteData) => apiClient.post('/api/user-invites', inviteData),

  /**
   * 초대 상태 업데이트
   * PUT /api/user-invites/{id}/status
   * @param {number} id - 사용자 초대 ID
   * @param {string} status - 새로운 상태 (PENDING/ACCEPTED/DECLINED)
   * @returns {Promise<Object>} 업데이트된 초대 정보
   */
  updateInviteStatus: (id, status) => 
    apiClient.put(`/api/user-invites/${id}/status`, null, { params: { status } }),

  /**
   * 사용자 초대 삭제
   * DELETE /api/user-invites/{id}
   * @param {number} id - 사용자 초대 ID
   * @returns {Promise<void>}
   */
  deleteUserInvite: (id) => apiClient.delete(`/api/user-invites/${id}`),

  /**
   * 사용자별 모든 초대 삭제
   * DELETE /api/user-invites/user/{userId}
   * @param {number} userId - 사용자 ID
   * @returns {Promise<void>}
   */
  deleteByUser: (userId) => apiClient.delete(`/api/user-invites/user/${userId}`),

  /**
   * RoomRent별 모든 초대 삭제
   * DELETE /api/user-invites/room-rent/{roomRentId}
   * @param {number} roomRentId - 회의실-예약 연결 ID
   * @returns {Promise<void>}
   */
  deleteByRoomRent: (roomRentId) => apiClient.delete(`/api/user-invites/room-rent/${roomRentId}`),

  /**
   * 초대 존재 여부 확인
   * GET /api/user-invites/exists
   * @param {number} userId - 사용자 ID
   * @param {number} roomRentId - 회의실-예약 연결 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsByUserAndRoomRent: (userId, roomRentId) => 
    apiClient.get('/api/user-invites/exists', { params: { userId, roomRentId } }),

  /**
   * 특정 사용자와 RoomRent의 초대 조회
   * GET /api/user-invites/find
   * @param {number} userId - 사용자 ID
   * @param {number} roomRentId - 회의실-예약 연결 ID
   * @returns {Promise<Object>} 초대 정보
   */
  findByUserAndRoomRent: (userId, roomRentId) => 
    apiClient.get('/api/user-invites/find', { params: { userId, roomRentId } }),

  /**
   * 사용자별 대기 중인 초대 개수 조회
   * GET /api/user-invites/user/{userId}/pending-count
   * @param {number} userId - 사용자 ID
   * @returns {Promise<number>} 대기 중인 초대 개수
   */
  countPendingInvitesByUser: (userId) => apiClient.get(`/api/user-invites/user/${userId}/pending-count`),

  /**
   * 예약별 수락된 초대 개수 조회
   * GET /api/user-invites/rent/{rentId}/accepted-count
   * @param {number} rentId - 예약 ID
   * @returns {Promise<number>} 수락된 초대 개수
   */
  countAcceptedInvitesForRent: (rentId) => apiClient.get(`/api/user-invites/rent/${rentId}/accepted-count`)
}; 
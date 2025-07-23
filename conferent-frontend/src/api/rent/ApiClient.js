import apiClient from '@/api/ApiClient';

/**
 * Rent API 클라이언트
 * 예약 관리 API
 */
export const rentApiClient = {
  /**
   * 모든 예약 목록 조회
   * GET /rents
   * @returns {Promise<Array>} 예약 목록
   */
  getAllRents: () => apiClient.get('/rents'),

  /**
   * ID로 예약 조회
   * GET /rents/{id}
   * @param {number} id - 예약 ID
   * @returns {Promise<Object>} 예약 정보
   */
  getRentById: (id) => apiClient.get(`/rents/${id}`),

  /**
   * 예약 생성
   * POST /rents
   * @param {Object} rentData - 예약 생성 데이터
   * @param {string} rentData.startTime - 시작 시간 (ISO 8601)
   * @param {string} rentData.endTime - 종료 시간 (ISO 8601)
   * @param {string} rentData.purpose - 예약 목적
   * @param {string} rentData.description - 설명
   * @param {Array<number>} rentData.roomIds - 회의실 ID 목록
   * @param {Array<number>} rentData.inviteeIds - 초대할 사용자 ID 목록
   * @returns {Promise<Object>} 생성된 예약 정보
   */
  createRent: (rentData) => apiClient.post('/rents', rentData),

  /**
   * 예약 수정
   * PUT /rents/{id}
   * @param {number} id - 예약 ID
   * @param {Object} rentData - 수정할 예약 데이터
   * @returns {Promise<Object>} 수정된 예약 정보
   */
  updateRent: (id, rentData) => apiClient.put(`/rents/${id}`, rentData),

  /**
   * 예약 삭제
   * DELETE /rents/{id}
   * @param {number} id - 예약 ID
   * @returns {Promise<void>}
   */
  deleteRent: (id) => apiClient.delete(`/rents/${id}`),

  /**
   * 생성자별 예약 조회
   * GET /rents/creator/{creatorId}
   * @param {number} creatorId - 생성자 ID
   * @returns {Promise<Array>} 조건에 맞는 예약 목록
   */
  getRentsByCreator: (creatorId) => apiClient.get(`/rents/creator/${creatorId}`),

  /**
   * 날짜 범위별 예약 조회
   * GET /rents/date-range
   * @param {string} startDate - 시작 날짜 (ISO 8601)
   * @param {string} endDate - 종료 날짜 (ISO 8601)
   * @returns {Promise<Array>} 조건에 맞는 예약 목록
   */
  getRentsByDateRange: (startDate, endDate) => 
    apiClient.get('/rents/date-range', { params: { startDate, endDate } }),

  /**
   * 예정된 예약 조회
   * GET /rents/upcoming
   * @param {string} fromTime - 기준 시간 (ISO 8601)
   * @returns {Promise<Array>} 예정된 예약 목록
   */
  getUpcomingRents: (fromTime) => 
    apiClient.get('/rents/upcoming', { params: { fromTime } }),

  /**
   * 목적별 예약 검색
   * GET /rents/search
   * @param {string} purpose - 검색할 목적
   * @returns {Promise<Array>} 조건에 맞는 예약 목록
   */
  searchRentsByPurpose: (purpose) => 
    apiClient.get('/rents/search', { params: { purpose } }),

  /**
   * 회의실별 예약 조회
   * GET /rents/room/{roomId}
   * @param {number} roomId - 회의실 ID
   * @returns {Promise<Array>} 조건에 맞는 예약 목록
   */
  getRentsByRoom: (roomId) => apiClient.get(`/rents/room/${roomId}`),

  /**
   * 예약 존재 여부 확인
   * GET /rents/{id}/exists
   * @param {number} id - 예약 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsRent: (id) => apiClient.get(`/rents/${id}/exists`),

  /**
   * 시간 충돌 확인
   * POST /rents/check-conflict
   * @param {Object} conflictData - 충돌 확인 데이터
   * @param {Array<number>} conflictData.roomIds - 회의실 ID 목록
   * @param {string} conflictData.startTime - 시작 시간
   * @param {string} conflictData.endTime - 종료 시간
   * @param {number} conflictData.excludeRentId - 제외할 예약 ID (선택)
   * @returns {Promise<boolean>} 충돌 여부
   */
  checkTimeConflict: (conflictData) => apiClient.post('/rents/check-conflict', conflictData),

  /**
   * 내 최근 예약 조회
   * GET /rents/recent
   * @returns {Promise<Array>} 최근 예약 목록
   */
  getRecentRents: (userId) => apiClient.get(`/rents/recent?userId=${userId}`)
}; 
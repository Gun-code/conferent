import apiClient from '@/api/ApiClient';

/**
 * RoomRent API 클라이언트
 * 회의실-예약 연결 관리 API
 */
export const roomRentApiClient = {
  /**
   * 모든 회의실-예약 연결 목록 조회
   * GET /api/room-rents
   * @returns {Promise<Array>} 회의실-예약 연결 목록
   */
  getAllRoomRents: () => apiClient.get('/api/room-rents'),

  /**
   * ID로 회의실-예약 연결 조회
   * GET /api/room-rents/{id}
   * @param {number} id - 회의실-예약 연결 ID
   * @returns {Promise<Object>} 회의실-예약 연결 정보
   */
  getRoomRentById: (id) => apiClient.get(`/api/room-rents/${id}`),

  /**
   * 회의실-예약 연결 생성
   * POST /api/room-rents
   * @param {Object} roomRentData - 회의실-예약 연결 생성 데이터
   * @param {number} roomRentData.roomId - 회의실 ID
   * @param {number} roomRentData.rentId - 예약 ID
   * @returns {Promise<Object>} 생성된 회의실-예약 연결 정보
   */
  createRoomRent: (roomRentData) => apiClient.post('/api/room-rents', roomRentData),

  /**
   * 회의실-예약 연결 수정
   * PUT /api/room-rents/{id}
   * @param {number} id - 회의실-예약 연결 ID
   * @param {Object} roomRentData - 수정할 데이터
   * @returns {Promise<Object>} 수정된 회의실-예약 연결 정보
   */
  updateRoomRent: (id, roomRentData) => apiClient.put(`/api/room-rents/${id}`, roomRentData),

  /**
   * 회의실-예약 연결 삭제
   * DELETE /api/room-rents/{id}
   * @param {number} id - 회의실-예약 연결 ID
   * @returns {Promise<void>}
   */
  deleteRoomRent: (id) => apiClient.delete(`/api/room-rents/${id}`),

  /**
   * 예약별 회의실-예약 연결 조회
   * GET /api/room-rents/rent/{rentId}
   * @param {number} rentId - 예약 ID
   * @returns {Promise<Array>} 조건에 맞는 회의실-예약 연결 목록
   */
  getRoomRentsByRent: (rentId) => apiClient.get(`/api/room-rents/rent/${rentId}`),

  /**
   * 회의실별 회의실-예약 연결 조회
   * GET /api/room-rents/room/{roomId}
   * @param {number} roomId - 회의실 ID
   * @returns {Promise<Array>} 조건에 맞는 회의실-예약 연결 목록
   */
  getRoomRentsByRoom: (roomId) => apiClient.get(`/api/room-rents/room/${roomId}`),

  /**
   * 특정 회의실과 예약의 연결 조회
   * GET /api/room-rents/find
   * @param {number} rentId - 예약 ID
   * @param {number} roomId - 회의실 ID
   * @returns {Promise<Object>} 회의실-예약 연결 정보
   */
  findByRentAndRoom: (rentId, roomId) => 
    apiClient.get('/api/room-rents/find', { params: { rentId, roomId } }),

  /**
   * 회의실-예약 연결 존재 여부 확인
   * GET /api/room-rents/{id}/exists
   * @param {number} id - 회의실-예약 연결 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsRoomRent: (id) => apiClient.get(`/api/room-rents/${id}/exists`),

  /**
   * 특정 회의실과 예약의 연결 존재 여부 확인
   * GET /api/room-rents/exists
   * @param {number} rentId - 예약 ID
   * @param {number} roomId - 회의실 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsByRentAndRoom: (rentId, roomId) => 
    apiClient.get('/api/room-rents/exists', { params: { rentId, roomId } })
}; 
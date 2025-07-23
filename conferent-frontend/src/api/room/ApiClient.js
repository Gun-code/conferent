import apiClient from '@/api/ApiClient';

/**
 * Room API 클라이언트
 * 회의실 관리 API
 */
export const roomApiClient = {
  /**
   * 모든 회의실 목록 조회
   * GET /rooms
   * @returns {Promise<Array>} 회의실 목록
   */
  getAllRooms: () => apiClient.get('/rooms'),

  /**
   * ID로 회의실 조회
   * GET /rooms/{id}
   * @param {number} id - 회의실 ID
   * @returns {Promise<Object>} 회의실 정보
   */
  getRoomById: (id) => apiClient.get(`/rooms/${id}`),

  /**
   * 회의실 생성
   * POST /rooms
   * @param {Object} roomData - 회의실 생성 데이터
   * @param {string} roomData.name - 회의실 이름
   * @param {string} roomData.location - 위치
   * @param {number} roomData.capacity - 수용 인원
   * @param {string} roomData.description - 설명
   * @returns {Promise<Object>} 생성된 회의실 정보
   */
  createRoom: (roomData) => apiClient.post('/rooms', roomData),

  /**
   * 회의실 수정
   * PUT /rooms/{id}
   * @param {number} id - 회의실 ID
   * @param {Object} roomData - 수정할 회의실 데이터
   * @returns {Promise<Object>} 수정된 회의실 정보
   */
  updateRoom: (id, roomData) => apiClient.put(`/rooms/${id}`, roomData),

  /**
   * 회의실 삭제
   * DELETE /rooms/{id}
   * @param {number} id - 회의실 ID
   * @returns {Promise<void>}
   */
  deleteRoom: (id) => apiClient.delete(`/rooms/${id}`),

  /**
   * 용량별 회의실 검색
   * GET /rooms/capacity/{minCapacity}
   * @param {number} minCapacity - 최소 수용 인원
   * @returns {Promise<Array>} 조건에 맞는 회의실 목록
   */
  getRoomsByCapacity: (minCapacity) => apiClient.get(`/rooms/capacity/${minCapacity}`),

  /**
   * 위치별 회의실 검색
   * GET /rooms/location/{location}
   * @param {string} location - 위치
   * @returns {Promise<Array>} 조건에 맞는 회의실 목록
   */
  getRoomsByLocation: (location) => apiClient.get(`/rooms/location/${location}`),

  /**
   * 회의실 존재 여부 확인
   * GET /rooms/{id}/exists
   * @param {number} id - 회의실 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsRoom: (id) => apiClient.get(`/rooms/${id}/exists`),

  /**
   * 이용 가능한 회의실 조회
   * GET /rooms/available
   * @returns {Promise<Array>} 이용 가능한 회의실 목록
   */
  getAvailableRooms: () => apiClient.get('/rooms/available')
}; 


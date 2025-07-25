import apiClient from '@/api/ApiClient'

/**
 * 회의실 API 클라이언트
 */
export const roomApiClient = {
  /**
   * 모든 회의실 목록 조회
   * GET /api/rooms
   * @returns {Promise} 회의실 목록
   */
  async getAll() {
    const response = await apiClient.get('/rooms')
    return response
  },

  /**
   * 회의실 상세 조회
   * GET /api/rooms/{id}
   * @param {number} id - 회의실 ID
   * @returns {Promise} 회의실 상세 정보
   */
  async getById(id) {
    const response = await apiClient.get(`/rooms/${id}`)
    return response
  },

  /**
   * 회의실 검색 (이름)
   * GET /api/rooms/search?name=
   * @param {string} name - 검색할 회의실 이름
   * @returns {Promise} 검색된 회의실 목록
   */
  async searchByName(name) {
    const response = await apiClient.get(`/rooms/search?name=${encodeURIComponent(name)}`)
    return response
  },

  /**
   * 회의실 생성
   * POST /api/rooms
   * @param {Object} roomData - 회의실 생성 데이터
   * @param {string} roomData.name - 회의실 이름
   * @param {string} roomData.location - 위치
   * @param {number} roomData.capacity - 수용 인원
   * @param {string} roomData.description - 설명
   * @returns {Promise} 생성된 회의실 정보
   */
  async create(roomData) {
    const response = await apiClient.post('/rooms', roomData)
    return response
  },

  /**
   * 회의실 수정
   * PUT /api/rooms/{id}
   * @param {number} id - 회의실 ID
   * @param {Object} roomData - 수정할 회의실 데이터
   * @returns {Promise} 수정된 회의실 정보
   */
  async update(id, roomData) {
    const response = await apiClient.put(`/rooms/${id}`, roomData)
    return response
  },

  /**
   * 회의실 삭제
   * DELETE /api/rooms/{id}
   * @param {number} id - 회의실 ID
   * @returns {Promise} 삭제 응답
   */
  async delete(id) {
    const response = await apiClient.delete(`/rooms/${id}`)
    return response
  }
} 


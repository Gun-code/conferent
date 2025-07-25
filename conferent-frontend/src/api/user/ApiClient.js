import apiClient from '@/api/ApiClient'

/**
 * 사용자 API 클라이언트
 */
export const userApiClient = {
  /**
   * 모든 사용자 목록 조회
   * GET /api/users
   * @returns {Promise} 사용자 목록
   */
  async getAll() {
    const response = await apiClient.get('/users')
    return response
  },

  /**
   * 사용자 상세 조회
   * GET /api/users/{id}
   * @param {number} id - 사용자 ID
   * @returns {Promise} 사용자 상세 정보
   */
  async getById(id) {
    const response = await apiClient.get(`/users/${id}`)
    return response
  },

  /**
   * 사용자 검색 (이름)
   * GET /api/users/search?name=
   * @param {string} name - 검색할 이름
   * @returns {Promise} 검색된 사용자 목록
   */
  async searchByName(name) {
    const response = await apiClient.get(`/users/search?name=${encodeURIComponent(name)}`)
    return response
  },

  /**
   * 역할별 사용자 조회
   * GET /api/users/role/{role}
   * @param {string} role - 사용자 역할 (USER, ADMIN)
   * @returns {Promise} 역할별 사용자 목록
   */
  async getByRole(role) {
    const response = await apiClient.get(`/users/role/${role.toUpperCase()}`)
    return response
  },

  /**
   * 사용자 생성
   * POST /api/users
   * @param {Object} userData - 사용자 생성 데이터
   * @param {string} userData.name - 이름
   * @param {string} userData.email - 이메일
   * @param {string} userData.password - 비밀번호
   * @param {string} userData.role - 역할 (USER, ADMIN)
   * @returns {Promise} 생성된 사용자 정보
   */
  async create(userData) {
    const response = await apiClient.post('/users', userData)
    return response
  },

  /**
   * 사용자 수정
   * PUT /api/users/{id}
   * @param {number} id - 사용자 ID
   * @param {Object} userData - 수정할 사용자 데이터
   * @returns {Promise} 수정된 사용자 정보
   */
  async update(id, userData) {
    const response = await apiClient.put(`/users/${id}`, userData)
    return response
  },

  /**
   * 사용자 삭제
   * DELETE /api/users/{id}
   * @param {number} id - 사용자 ID
   * @returns {Promise} 삭제 응답
   */
  async delete(id) {
    const response = await apiClient.delete(`/users/${id}`)
    return response
  }
} 
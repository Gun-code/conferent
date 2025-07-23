import apiClient from '@/api/ApiClient';

/**
 * User API 클라이언트
 * 사용자 관리 API
 */
export const userApiClient = {
  /**
   * 모든 사용자 목록 조회
   * GET /api/users
   * @returns {Promise<Array>} 사용자 목록
   */
  getAllUsers: () => apiClient.get('/api/users'),

  /**
   * ID로 사용자 조회
   * GET /api/users/{id}
   * @param {number} id - 사용자 ID
   * @returns {Promise<Object>} 사용자 정보
   */
  getUserById: (id) => apiClient.get(`/api/users/${id}`),

  /**
   * 이메일로 사용자 조회
   * GET /api/users/email/{email}
   * @param {string} email - 사용자 이메일
   * @returns {Promise<Object>} 사용자 정보
   */
  getUserByEmail: (email) => apiClient.get(`/api/users/email/${email}`),

  /**
   * 사용자 생성
   * POST /api/users
   * @param {Object} userData - 사용자 생성 데이터
   * @param {string} userData.name - 사용자 이름
   * @param {string} userData.email - 이메일
   * @param {string} userData.password - 비밀번호
   * @param {string} userData.role - 역할 (USER/ADMIN)
   * @returns {Promise<Object>} 생성된 사용자 정보
   */
  createUser: (userData) => apiClient.post('/api/users', userData),

  /**
   * 사용자 수정
   * PUT /api/users/{id}
   * @param {number} id - 사용자 ID
   * @param {Object} userData - 수정할 사용자 데이터
   * @returns {Promise<Object>} 수정된 사용자 정보
   */
  updateUser: (id, userData) => apiClient.put(`/api/users/${id}`, userData),

  /**
   * 사용자 삭제
   * DELETE /api/users/{id}
   * @param {number} id - 사용자 ID
   * @returns {Promise<void>}
   */
  deleteUser: (id) => apiClient.delete(`/api/users/${id}`),

  /**
   * 역할별 사용자 조회
   * GET /api/users/role/{role}
   * @param {string} role - 역할 (USER/ADMIN)
   * @returns {Promise<Array>} 조건에 맞는 사용자 목록
   */
  getUsersByRole: (role) => apiClient.get(`/api/users/role/${role}`),

  /**
   * 사용자 존재 여부 확인
   * GET /api/users/{id}/exists
   * @param {number} id - 사용자 ID
   * @returns {Promise<boolean>} 존재 여부
   */
  existsUser: (id) => apiClient.get(`/api/users/${id}/exists`),

  /**
   * 이메일 중복 확인
   * GET /api/users/email/{email}/exists
   * @param {string} email - 이메일
   * @returns {Promise<boolean>} 중복 여부
   */
  existsEmail: (email) => apiClient.get(`/api/users/email/${email}/exists`)
}; 
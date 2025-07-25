import axios from 'axios'
import apiConfig from '../config.js'
import { logError, extractErrorMessage } from '@/utils/errorHandler.js'

// 기본 API 클라이언트
const authApiClient = axios.create(apiConfig)

// 요청 인터셉터 - 토큰 자동 추가
authApiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    logError(error, 'Auth API Request')
    return Promise.reject(error)
  }
)

// 응답 인터셉터 - 토큰 만료 처리 및 에러 로깅
authApiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // 에러 로깅
    logError(error, 'Auth API Response')
    
    if (error.response?.status === 401) {
      // 토큰 만료 시 로그아웃 처리
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('userName')
      localStorage.removeItem('userEmail')
      localStorage.removeItem('userRole')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export { authApiClient }

export default {
  /**
   * 로그인
   * @param {Object} credentials - {email, password}
   * @returns {Promise} 로그인 응답 (토큰, 사용자 정보)
   */
  async login(credentials) {
    const response = await authApiClient.post('/auth/login', credentials)
    return response
  },

  /**
   * 회원가입
   * @param {Object} userData - {name, email, password, role?}
   * @returns {Promise} 회원가입 응답
   */
  async register(userData) {
    const response = await authApiClient.post('/auth/register', userData)
    return response
  },

  /**
   * 현재 사용자 정보 조회
   * @returns {Promise} 현재 사용자 정보
   */
  async getCurrentUser() {
    const response = await authApiClient.get('/auth/me')
    return response
  },

  /**
   * 토큰 검증
   * @returns {Promise} 토큰 검증 결과
   */
  async validateToken() {
    const response = await authApiClient.post('/auth/validate')
    return response
  },

  /**
   * 로그아웃 (서버 호출)
   * @returns {Promise} 로그아웃 응답
   */
  async logout() {
    try {
      const response = await authApiClient.post('/auth/logout')
      return response
    } catch (error) {
      // 로그아웃 요청이 실패해도 클라이언트에서는 정상 처리
      console.warn('Server logout failed:', error)
      return { data: { success: true } }
    }
  }
} 
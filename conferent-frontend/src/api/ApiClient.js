import axios from 'axios'
import apiConfig from './config.js'
import { logError, extractErrorMessage } from '@/utils/errorHandler.js'

/**
 * API 클라이언트 인스턴스
 * @type {import('axios').AxiosInstance}
 */
const apiClient = axios.create(apiConfig)

// 요청 인터셉터 - 인증 토큰 및 사용자 ID 헤더 추가
apiClient.interceptors.request.use(
  /**
   * 요청 설정 수정
   * @param {import('axios').AxiosRequestConfig} config - axios 요청 설정
   * @returns {import('axios').AxiosRequestConfig} 수정된 요청 설정
   */
  (config) => {
    // JWT 토큰 추가
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    return config
  },
  /**
   * 요청 오류 처리
   * @param {Error} error - 요청 오류
   * @returns {Promise<never>} 거부된 Promise
   */
  (error) => {
    logError(error, 'API Request')
    return Promise.reject(error)
  }
)

// 응답 인터셉터 - 오류 처리
apiClient.interceptors.response.use(
  /**
   * 성공 응답 처리
   * @param {import('axios').AxiosResponse} response - axios 응답 객체
   * @returns {import('axios').AxiosResponse} 응답 객체
   */
  (response) => response,
  /**
   * 응답 오류 처리
   * @param {import('axios').AxiosError} error - axios 오류 객체
   * @returns {Promise<never>} 거부된 Promise
   */
  (error) => {
    // 에러 로깅
    logError(error, 'API Response')
    
    // 401 에러 시 자동 로그아웃
    if (error.response?.status === 401) {
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

export default apiClient 
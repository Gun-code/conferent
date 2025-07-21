import axios from 'axios'

/**
 * API 클라이언트 인스턴스
 * @type {import('axios').AxiosInstance}
 */
const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 요청 인터셉터 - 사용자 ID 헤더 추가
apiClient.interceptors.request.use(
  /**
   * 요청 설정 수정
   * @param {import('axios').AxiosRequestConfig} config - axios 요청 설정
   * @returns {import('axios').AxiosRequestConfig} 수정된 요청 설정
   */
  (config) => {
    const userId = localStorage.getItem('userId')
    if (userId) {
      config.headers['X-User-Id'] = userId
    }
    return config
  },
  /**
   * 요청 오류 처리
   * @param {Error} error - 요청 오류
   * @returns {Promise<never>} 거부된 Promise
   */
  (error) => {
    console.error('Request interceptor error:', error)
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
    if (error.response) {
      // 서버 응답이 있는 경우
      console.error('API Error:', {
        status: error.response.status,
        data: error.response.data,
        url: error.config?.url
      })
    } else if (error.request) {
      // 요청은 했지만 응답이 없는 경우
      console.error('Network Error:', error.message)
    } else {
      // 요청 설정 중 오류
      console.error('Request Setup Error:', error.message)
    }
    
    return Promise.reject(error)
  }
)

export default apiClient 
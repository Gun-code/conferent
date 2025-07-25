/**
 * API 설정 관리
 * 개발환경과 프로덕션환경에서 다른 설정을 사용
 */

// 개발환경 체크 (Vite 개발 서버에서 실행 중인지 확인)
const isDevelopment = import.meta.env.DEV

// 기본 API URL 설정
const getBaseURL = () => {
  // 개발환경에서는 환경변수 사용, 없으면 localhost
  if (isDevelopment) {
    return import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
  }
  
  // 프로덕션환경에서는 상대 경로 사용 (nginx 프록시)
  return '/api'
}

export const apiConfig = {
  baseURL: getBaseURL(),
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
}

export default apiConfig 
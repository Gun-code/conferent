/**
 * 백엔드 에러 응답을 처리하는 유틸리티
 */

/**
 * 백엔드 에러 응답에서 사용자 친화적인 메시지 추출
 * @param {Object} error - axios 에러 객체
 * @returns {string} 사용자 친화적인 에러 메시지
 */
export function extractErrorMessage(error) {
  // 백엔드에서 보낸 구조화된 에러 응답이 있는 경우
  if (error.response?.data) {
    const errorData = error.response.data
    
    // 백엔드 ErrorResponse 형식인 경우
    if (errorData.message) {
      return errorData.message
    }
    
    // ValidationErrorResponse 형식인 경우
    if (errorData.fieldErrors) {
      const fieldErrors = Object.values(errorData.fieldErrors)
      return fieldErrors.join(', ')
    }
  }
  
  // HTTP 상태 코드별 기본 메시지
  const status = error.response?.status
  switch (status) {
    case 400:
      return '잘못된 요청입니다.'
    case 401:
      return '인증이 필요합니다. 다시 로그인해주세요.'
    case 403:
      return '접근 권한이 없습니다.'
    case 404:
      return '요청한 리소스를 찾을 수 없습니다.'
    case 409:
      return '이미 존재하는 데이터입니다.'
    case 422:
      return '입력값이 올바르지 않습니다.'
    case 500:
      return '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    default:
      return '알 수 없는 오류가 발생했습니다.'
  }
}

/**
 * 에러 타입별로 분류
 * @param {Object} error - axios 에러 객체
 * @returns {string} 에러 타입
 */
export function getErrorType(error) {
  const status = error.response?.status
  const errorData = error.response?.data
  
  if (errorData?.code) {
    return errorData.code
  }
  
  switch (status) {
    case 400:
      return 'BAD_REQUEST'
    case 401:
      return 'UNAUTHORIZED'
    case 403:
      return 'FORBIDDEN'
    case 404:
      return 'NOT_FOUND'
    case 409:
      return 'CONFLICT'
    case 422:
      return 'VALIDATION_ERROR'
    case 500:
      return 'INTERNAL_SERVER_ERROR'
    default:
      return 'UNKNOWN_ERROR'
  }
}

/**
 * 에러를 콘솔에 로깅
 * @param {Object} error - axios 에러 객체
 * @param {string} context - 에러 발생 컨텍스트
 */
export function logError(error, context = '') {
  const message = extractErrorMessage(error)
  const type = getErrorType(error)
  
  // 백엔드 에러 응답이 있는 경우 상세 로깅
  if (error.response?.data) {
    console.error(`[${context}] ${type}: ${message}`, {
      status: error.response.status,
      backendError: error.response.data,
      url: error.config?.url,
      method: error.config?.method
    })
  } else {
    console.error(`[${context}] ${type}: ${message}`, {
      status: error.response?.status,
      data: error.response?.data,
      config: error.config
    })
  }
}

/**
 * 에러를 사용자에게 표시할 수 있는 형태로 변환
 * @param {Object} error - axios 에러 객체
 * @returns {Object} 사용자 표시용 에러 객체
 */
export function formatErrorForUser(error) {
  return {
    message: extractErrorMessage(error),
    type: getErrorType(error),
    status: error.response?.status,
    timestamp: new Date().toISOString()
  }
} 
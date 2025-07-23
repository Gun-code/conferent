/**
 * 포맷팅 관련 유틸리티 함수
 */

/**
 * 숫자를 천 단위로 포맷팅
 * @param {number} num - 포맷팅할 숫자
 * @param {string} locale - 로케일 (기본값: 'ko-KR')
 * @returns {string} 포맷된 문자열
 */
export const formatNumber = (num, locale = 'ko-KR') => {
  if (typeof num !== 'number') return num
  return num.toLocaleString(locale)
}

/**
 * 파일 크기를 읽기 쉬운 형태로 포맷팅
 * @param {number} bytes - 바이트 단위 크기
 * @param {number} decimals - 소수점 자릿수 (기본값: 2)
 * @returns {string} 포맷된 파일 크기
 */
export const formatFileSize = (bytes, decimals = 2) => {
  if (bytes === 0) return '0 Bytes'
  
  const k = 1024
  const dm = decimals < 0 ? 0 : decimals
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i]
}

/**
 * 전화번호 포맷팅
 * @param {string} phone - 전화번호 문자열
 * @returns {string} 포맷된 전화번호
 */
export const formatPhoneNumber = (phone) => {
  if (!phone) return ''
  
  // 숫자만 추출
  const cleaned = phone.replace(/\D/g, '')
  
  // 길이에 따라 포맷팅
  if (cleaned.length === 10) {
    return cleaned.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3')
  } else if (cleaned.length === 11) {
    return cleaned.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
  }
  
  return phone
}

/**
 * 이메일 마스킹
 * @param {string} email - 이메일 주소
 * @returns {string} 마스킹된 이메일
 */
export const maskEmail = (email) => {
  if (!email) return ''
  
  const [localPart, domain] = email.split('@')
  if (!domain) return email
  
  if (localPart.length <= 2) {
    return email
  }
  
  const maskedLocal = localPart.charAt(0) + '*'.repeat(localPart.length - 2) + localPart.charAt(localPart.length - 1)
  return `${maskedLocal}@${domain}`
}

/**
 * 이름 마스킹
 * @param {string} name - 이름
 * @returns {string} 마스킹된 이름
 */
export const maskName = (name) => {
  if (!name) return ''
  
  if (name.length <= 2) {
    return name.charAt(0) + '*'
  }
  
  return name.charAt(0) + '*'.repeat(name.length - 2) + name.charAt(name.length - 1)
}

/**
 * 텍스트 길이 제한
 * @param {string} text - 원본 텍스트
 * @param {number} maxLength - 최대 길이
 * @param {string} suffix - 접미사 (기본값: '...')
 * @returns {string} 제한된 텍스트
 */
export const truncateText = (text, maxLength, suffix = '...') => {
  if (!text || text.length <= maxLength) return text
  return text.substring(0, maxLength) + suffix
}

/**
 * 카드 번호 마스킹
 * @param {string} cardNumber - 카드 번호
 * @returns {string} 마스킹된 카드 번호
 */
export const maskCardNumber = (cardNumber) => {
  if (!cardNumber) return ''
  
  const cleaned = cardNumber.replace(/\D/g, '')
  if (cleaned.length < 4) return cardNumber
  
  return '*'.repeat(cleaned.length - 4) + cleaned.slice(-4)
}

/**
 * 시간을 읽기 쉬운 형태로 포맷팅
 * @param {number} minutes - 분 단위 시간
 * @returns {string} 포맷된 시간
 */
export const formatDuration = (minutes) => {
  if (minutes < 60) {
    return `${minutes}분`
  }
  
  const hours = Math.floor(minutes / 60)
  const remainingMinutes = minutes % 60
  
  if (remainingMinutes === 0) {
    return `${hours}시간`
  }
  
  return `${hours}시간 ${remainingMinutes}분`
}

/**
 * 가격을 통화 형태로 포맷팅
 * @param {number} price - 가격
 * @param {string} currency - 통화 (기본값: 'KRW')
 * @param {string} locale - 로케일 (기본값: 'ko-KR')
 * @returns {string} 포맷된 가격
 */
export const formatCurrency = (price, currency = 'KRW', locale = 'ko-KR') => {
  if (typeof price !== 'number') return price
  
  return new Intl.NumberFormat(locale, {
    style: 'currency',
    currency: currency
  }).format(price)
}

/**
 * 퍼센트 포맷팅
 * @param {number} value - 값
 * @param {number} total - 전체 값
 * @param {number} decimals - 소수점 자릿수 (기본값: 1)
 * @returns {string} 포맷된 퍼센트
 */
export const formatPercentage = (value, total, decimals = 1) => {
  if (total === 0) return '0%'
  
  const percentage = (value / total) * 100
  return `${percentage.toFixed(decimals)}%`
}

/**
 * URL에서 도메인 추출
 * @param {string} url - URL
 * @returns {string} 도메인
 */
export const extractDomain = (url) => {
  if (!url) return ''
  
  try {
    const urlObj = new URL(url.startsWith('http') ? url : `https://${url}`)
    return urlObj.hostname
  } catch {
    return url
  }
}

/**
 * 파일 확장자 추출
 * @param {string} filename - 파일명
 * @returns {string} 확장자
 */
export const getFileExtension = (filename) => {
  if (!filename) return ''
  
  const lastDotIndex = filename.lastIndexOf('.')
  if (lastDotIndex === -1) return ''
  
  return filename.substring(lastDotIndex + 1).toLowerCase()
}

/**
 * 파일명에서 확장자 제거
 * @param {string} filename - 파일명
 * @returns {string} 확장자가 제거된 파일명
 */
export const removeFileExtension = (filename) => {
  if (!filename) return ''
  
  const lastDotIndex = filename.lastIndexOf('.')
  if (lastDotIndex === -1) return filename
  
  return filename.substring(0, lastDotIndex)
}

/**
 * 문자열을 카멜케이스로 변환
 * @param {string} str - 원본 문자열
 * @returns {string} 카멜케이스 문자열
 */
export const toCamelCase = (str) => {
  if (!str) return ''
  
  return str
    .toLowerCase()
    .replace(/[^a-zA-Z0-9]+(.)/g, (match, chr) => chr.toUpperCase())
}

/**
 * 문자열을 파스칼케이스로 변환
 * @param {string} str - 원본 문자열
 * @returns {string} 파스칼케이스 문자열
 */
export const toPascalCase = (str) => {
  if (!str) return ''
  
  const camelCase = toCamelCase(str)
  return camelCase.charAt(0).toUpperCase() + camelCase.slice(1)
}

/**
 * 문자열을 스네이크케이스로 변환
 * @param {string} str - 원본 문자열
 * @returns {string} 스네이크케이스 문자열
 */
export const toSnakeCase = (str) => {
  if (!str) return ''
  
  return str
    .replace(/([A-Z])/g, '_$1')
    .toLowerCase()
    .replace(/^_/, '')
} 
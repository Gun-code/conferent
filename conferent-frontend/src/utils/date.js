/**
 * 날짜 관련 유틸리티 함수
 */

/**
 * 현재 날짜를 ISO 문자열로 반환
 * @returns {string} ISO 8601 형식의 현재 날짜
 */
export const getCurrentDate = () => {
  return new Date().toISOString()
}

/**
 * 날짜를 한국 시간대로 포맷팅
 * @param {string|Date} date - 날짜
 * @param {string} format - 포맷 옵션 ('date', 'time', 'datetime', 'relative')
 * @returns {string} 포맷된 날짜 문자열
 */
export const formatDate = (date, format = 'datetime') => {
  const dateObj = typeof date === 'string' ? new Date(date) : date
  
  if (format === 'date') {
    return dateObj.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }
  
  if (format === 'time') {
    return dateObj.toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit'
    })
  }
  
  if (format === 'datetime') {
    return dateObj.toLocaleString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
  
  if (format === 'relative') {
    return getRelativeTime(dateObj)
  }
  
  return dateObj.toLocaleString('ko-KR')
}

/**
 * 상대적 시간 표현 (예: "3분 전", "1시간 전")
 * @param {Date} date - 날짜
 * @returns {string} 상대적 시간 문자열
 */
export const getRelativeTime = (date) => {
  const now = new Date()
  const diffInSeconds = Math.floor((now - date) / 1000)
  
  if (diffInSeconds < 60) {
    return '방금 전'
  }
  
  const diffInMinutes = Math.floor(diffInSeconds / 60)
  if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`
  }
  
  const diffInHours = Math.floor(diffInMinutes / 60)
  if (diffInHours < 24) {
    return `${diffInHours}시간 전`
  }
  
  const diffInDays = Math.floor(diffInHours / 24)
  if (diffInDays < 7) {
    return `${diffInDays}일 전`
  }
  
  const diffInWeeks = Math.floor(diffInDays / 7)
  if (diffInWeeks < 4) {
    return `${diffInWeeks}주 전`
  }
  
  const diffInMonths = Math.floor(diffInDays / 30)
  if (diffInMonths < 12) {
    return `${diffInMonths}개월 전`
  }
  
  const diffInYears = Math.floor(diffInDays / 365)
  return `${diffInYears}년 전`
}

/**
 * 두 날짜 간의 차이를 계산
 * @param {string|Date} startDate - 시작 날짜
 * @param {string|Date} endDate - 종료 날짜
 * @param {string} unit - 단위 ('minutes', 'hours', 'days')
 * @returns {number} 차이값
 */
export const getDateDifference = (startDate, endDate, unit = 'minutes') => {
  const start = typeof startDate === 'string' ? new Date(startDate) : startDate
  const end = typeof endDate === 'string' ? new Date(endDate) : endDate
  
  const diffInMs = end - start
  
  switch (unit) {
    case 'minutes':
      return Math.floor(diffInMs / (1000 * 60))
    case 'hours':
      return Math.floor(diffInMs / (1000 * 60 * 60))
    case 'days':
      return Math.floor(diffInMs / (1000 * 60 * 60 * 24))
    default:
      return diffInMs
  }
}

/**
 * 날짜가 유효한지 확인
 * @param {string|Date} date - 확인할 날짜
 * @returns {boolean} 유효성 여부
 */
export const isValidDate = (date) => {
  const dateObj = typeof date === 'string' ? new Date(date) : date
  return dateObj instanceof Date && !isNaN(dateObj)
}

/**
 * 날짜 범위 생성
 * @param {string|Date} startDate - 시작 날짜
 * @param {string|Date} endDate - 종료 날짜
 * @param {string} interval - 간격 ('hour', 'day', 'week', 'month')
 * @returns {Date[]} 날짜 배열
 */
export const generateDateRange = (startDate, endDate, interval = 'hour') => {
  const start = typeof startDate === 'string' ? new Date(startDate) : startDate
  const end = typeof endDate === 'string' ? new Date(endDate) : endDate
  const dates = []
  
  let current = new Date(start)
  
  while (current <= end) {
    dates.push(new Date(current))
    
    switch (interval) {
      case 'hour':
        current.setHours(current.getHours() + 1)
        break
      case 'day':
        current.setDate(current.getDate() + 1)
        break
      case 'week':
        current.setDate(current.getDate() + 7)
        break
      case 'month':
        current.setMonth(current.getMonth() + 1)
        break
      default:
        current.setHours(current.getHours() + 1)
    }
  }
  
  return dates
}

/**
 * 오늘 날짜인지 확인
 * @param {string|Date} date - 확인할 날짜
 * @returns {boolean} 오늘 날짜 여부
 */
export const isToday = (date) => {
  const dateObj = typeof date === 'string' ? new Date(date) : date
  const today = new Date()
  
  return dateObj.getDate() === today.getDate() &&
         dateObj.getMonth() === today.getMonth() &&
         dateObj.getFullYear() === today.getFullYear()
}

/**
 * 주말인지 확인
 * @param {string|Date} date - 확인할 날짜
 * @returns {boolean} 주말 여부
 */
export const isWeekend = (date) => {
  const dateObj = typeof date === 'string' ? new Date(date) : date
  const day = dateObj.getDay()
  return day === 0 || day === 6 // 0: 일요일, 6: 토요일
}

/**
 * 시간 충돌 확인
 * @param {string|Date} start1 - 첫 번째 시작 시간
 * @param {string|Date} end1 - 첫 번째 종료 시간
 * @param {string|Date} start2 - 두 번째 시작 시간
 * @param {string|Date} end2 - 두 번째 종료 시간
 * @returns {boolean} 충돌 여부
 */
export const hasTimeConflict = (start1, end1, start2, end2) => {
  const s1 = typeof start1 === 'string' ? new Date(start1) : start1
  const e1 = typeof end1 === 'string' ? new Date(end1) : end1
  const s2 = typeof start2 === 'string' ? new Date(start2) : start2
  const e2 = typeof end2 === 'string' ? new Date(end2) : end2
  
  return s1 < e2 && s2 < e1
} 
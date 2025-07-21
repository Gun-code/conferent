/**
 * 예약 상태 열거형
 * @readonly
 * @enum {string}
 */
export const ReservationStatus = {
  /** 대기중 */
  PENDING: 'PENDING',
  /** 확정됨 */
  CONFIRMED: 'CONFIRMED', 
  /** 취소됨 */
  CANCELLED: 'CANCELLED',
  /** 완료됨 */
  COMPLETED: 'COMPLETED'
}

/**
 * 예약 엔티티 클래스
 * 도메인 모델로서 예약의 핵심 비즈니스 로직을 담당
 */
export class ReservationEntity {
  
  /**
   * ReservationEntity 생성자
   * @param {Object} data - 예약 데이터
   * @param {number} data.id - 예약 ID
   * @param {number} data.userId - 사용자 ID
   * @param {number} data.roomId - 회의실 ID
   * @param {string} data.roomName - 회의실 이름
   * @param {Date} data.startTime - 시작 시간
   * @param {Date} data.endTime - 종료 시간
   * @param {string} data.purpose - 예약 목적
   * @param {string} data.status - 예약 상태
   * @throws {Error} 필수 필드가 누락되거나 유효하지 않은 경우
   */
  constructor(data) {
    // 필수 필드 검증
    this._validateRequiredFields(data)
    
    /** @type {number} 예약 ID */
    this.id = data.id
    
    /** @type {number} 사용자 ID */
    this.userId = data.userId
    
    /** @type {number} 회의실 ID */
    this.roomId = data.roomId
    
    /** @type {string} 회의실 이름 */
    this.roomName = data.roomName
    
    /** @type {Date} 시작 시간 */
    this.startTime = data.startTime
    
    /** @type {Date} 종료 시간 */
    this.endTime = data.endTime
    
    /** @type {string} 예약 목적 */
    this.purpose = data.purpose
    
    /** @type {string} 예약 상태 */
    this.status = data.status || ReservationStatus.PENDING
    
    // 비즈니스 규칙 검증
    this._validateBusinessRules()
  }

  /**
   * 필수 필드 검증
   * @private
   * @param {Object} data - 검증할 데이터
   * @throws {Error} 필수 필드가 누락된 경우
   */
  _validateRequiredFields(data) {
    if (!data || typeof data !== 'object') {
      throw new Error('예약 데이터가 제공되지 않았습니다.')
    }

    const requiredFields = ['id', 'userId', 'roomId', 'roomName', 'startTime', 'endTime', 'purpose']
    
    for (const field of requiredFields) {
      if (data[field] === undefined || data[field] === null) {
        throw new Error(`필수 필드가 누락되었습니다: ${field}`)
      }
    }

    // 타입 검증
    if (typeof data.id !== 'number' || data.id <= 0) {
      throw new Error('예약 ID는 양수여야 합니다.')
    }
    
    if (typeof data.userId !== 'number' || data.userId <= 0) {
      throw new Error('사용자 ID는 양수여야 합니다.')
    }
    
    if (typeof data.roomId !== 'number' || data.roomId <= 0) {
      throw new Error('회의실 ID는 양수여야 합니다.')
    }
    
    if (typeof data.roomName !== 'string' || !data.roomName.trim()) {
      throw new Error('회의실 이름은 빈 문자열일 수 없습니다.')
    }
    
    if (!(data.startTime instanceof Date) || isNaN(data.startTime)) {
      throw new Error('시작 시간은 유효한 Date 객체여야 합니다.')
    }
    
    if (!(data.endTime instanceof Date) || isNaN(data.endTime)) {
      throw new Error('종료 시간은 유효한 Date 객체여야 합니다.')
    }
    
    if (typeof data.purpose !== 'string' || !data.purpose.trim()) {
      throw new Error('예약 목적은 빈 문자열일 수 없습니다.')
    }
  }

  /**
   * 비즈니스 규칙 검증
   * @private
   * @throws {Error} 비즈니스 규칙 위반 시
   */
  _validateBusinessRules() {
    // 시작 시간이 종료 시간보다 앞서야 함
    if (this.startTime >= this.endTime) {
      throw new Error('시작 시간은 종료 시간보다 앞서야 합니다.')
    }

    // 최소 예약 시간: 30분
    const durationMinutes = this.getDurationInMinutes()
    if (durationMinutes < 30) {
      throw new Error('최소 예약 시간은 30분입니다.')
    }

    // 최대 예약 시간: 8시간
    if (durationMinutes > 480) {
      throw new Error('최대 예약 시간은 8시간입니다.')
    }

    // 상태 검증
    if (!Object.values(ReservationStatus).includes(this.status)) {
      throw new Error(`유효하지 않은 예약 상태입니다: ${this.status}`)
    }
  }

  /**
   * 예약 기간을 분 단위로 반환
   * @returns {number} 예약 기간(분)
   */
  getDurationInMinutes() {
    return Math.floor((this.endTime - this.startTime) / (1000 * 60))
  }

  /**
   * 예약이 취소 가능한지 확인
   * @returns {boolean} 취소 가능 여부
   */
  isCancellable() {
    // 이미 취소되었거나 완료된 예약은 취소 불가
    if (this.status === ReservationStatus.CANCELLED || 
        this.status === ReservationStatus.COMPLETED) {
      return false
    }

    // 시작 시간 1시간 전까지만 취소 가능
    const oneHourBefore = new Date(this.startTime.getTime() - 60 * 60 * 1000)
    return new Date() < oneHourBefore
  }

  /**
   * 예약이 현재 진행 중인지 확인
   * @returns {boolean} 진행 중 여부
   */
  isInProgress() {
    const now = new Date()
    return this.status === ReservationStatus.CONFIRMED && 
           now >= this.startTime && 
           now < this.endTime
  }

  /**
   * 예약이 과거인지 확인
   * @returns {boolean} 과거 예약 여부
   */
  isPast() {
    return new Date() > this.endTime
  }

  /**
   * 예약 정보를 문자열로 반환
   * @returns {string} 예약 정보 문자열
   */
  toString() {
    return `예약 #${this.id}: ${this.roomName} (${this.startTime.toLocaleString()} - ${this.endTime.toLocaleString()})`
  }

  /**
   * 예약 정보를 JSON 형태로 반환
   * @returns {Object} JSON 형태의 예약 정보
   */
  toJSON() {
    return {
      id: this.id,
      userId: this.userId,
      roomId: this.roomId,
      roomName: this.roomName,
      startTime: this.startTime.toISOString(),
      endTime: this.endTime.toISOString(),
      purpose: this.purpose,
      status: this.status,
      durationMinutes: this.getDurationInMinutes(),
      isCancellable: this.isCancellable(),
      isInProgress: this.isInProgress(),
      isPast: this.isPast()
    }
  }
} 
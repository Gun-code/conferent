import { ReservationEntity, ReservationModel } from '../models/ReservationModel'
import { ReservationDataRepository, ReservationDto, CreateReservationDto } from '../../data/repositories/ReservationDataRepository'

export interface CreateReservationRequest {
  roomId: number
  startTime: string
  endTime: string
  purpose: string
}

export class ReservationBusinessService {
  constructor(private dataRepository: ReservationDataRepository) {}

  async createReservation(request: CreateReservationRequest): Promise<ReservationEntity> {
    // 비즈니스 검증 로직
    this.validateReservationTime(request.startTime, request.endTime)
    this.validatePurpose(request.purpose)

    const createDto: CreateReservationDto = {
      roomId: request.roomId,
      startTime: request.startTime,
      endTime: request.endTime,
      purpose: request.purpose
    }

    const dto = await this.dataRepository.createReservation(createDto)
    const model = this.dtoToModel(dto)
    return ReservationEntity.fromModel(model)
  }

  async cancelReservation(reservationId: number, userId: number, reservation: ReservationEntity): Promise<void> {
    // 비즈니스 검증 로직
    if (!reservation.isOwnedBy(userId)) {
      throw new Error('본인의 예약만 취소할 수 있습니다.')
    }

    if (!reservation.canBeCancelled()) {
      throw new Error('취소할 수 없는 예약입니다.')
    }

    await this.dataRepository.cancelReservation(reservationId)
  }

  async getUserReservations(userId: number): Promise<ReservationEntity[]> {
    const dtos = await this.dataRepository.getUserReservations(userId)
    const models = dtos.map(dto => this.dtoToModel(dto))
    return models.map(model => ReservationEntity.fromModel(model))
  }

  private validateReservationTime(startTime: string, endTime: string): void {
    const start = new Date(startTime)
    const end = new Date(endTime)
    
    if (start >= end) {
      throw new Error('시작 시간은 종료 시간보다 이전이어야 합니다.')
    }
    
    if (start < new Date()) {
      throw new Error('과거 시간으로는 예약할 수 없습니다.')
    }
  }

  private validatePurpose(purpose: string): void {
    if (!purpose || purpose.trim().length === 0) {
      throw new Error('회의 목적은 필수입니다.')
    }
  }

  private dtoToModel(dto: ReservationDto): ReservationModel {
    return {
      id: dto.id,
      userId: dto.userId,
      roomId: dto.roomId,
      roomName: dto.roomName,
      startTime: new Date(dto.startTime),
      endTime: new Date(dto.endTime),
      purpose: dto.purpose,
      status: dto.status as any,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt)
    }
  }
} 
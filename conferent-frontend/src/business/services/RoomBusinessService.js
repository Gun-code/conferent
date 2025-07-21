import { RoomEntity, RoomModel } from '../models/RoomModel'
import { RoomDataRepository, RoomDto, CreateRoomDto } from '../../data/repositories/RoomDataRepository'

export interface CreateRoomRequest {
  name: string
  location: string
  capacity: number
  description?: string
}

export class RoomBusinessService {
  constructor(private dataRepository: RoomDataRepository) {}

  async getRooms(minCapacity?: number): Promise<RoomEntity[]> {
    const dtos = await this.dataRepository.getRooms(minCapacity)
    const models = dtos.map(dto => this.dtoToModel(dto))
    return models.map(model => RoomEntity.fromModel(model))
  }

  async getRoomById(id: number): Promise<RoomEntity> {
    const dto = await this.dataRepository.getRoomById(id)
    const model = this.dtoToModel(dto)
    return RoomEntity.fromModel(model)
  }

  async createRoom(request: CreateRoomRequest): Promise<RoomEntity> {
    // 비즈니스 검증 로직
    this.validateRoomInfo(request)

    const createDto: CreateRoomDto = {
      name: request.name,
      location: request.location,
      capacity: request.capacity,
      description: request.description
    }

    const dto = await this.dataRepository.createRoom(createDto)
    const model = this.dtoToModel(dto)
    return RoomEntity.fromModel(model)
  }

  private validateRoomInfo(request: CreateRoomRequest): void {
    if (!request.name || request.name.trim().length === 0) {
      throw new Error('회의실 이름은 필수입니다.')
    }
    
    if (!request.location || request.location.trim().length === 0) {
      throw new Error('위치는 필수입니다.')
    }
    
    if (!request.capacity || request.capacity <= 0) {
      throw new Error('수용 인원은 1명 이상이어야 합니다.')
    }
  }

  private dtoToModel(dto: RoomDto): RoomModel {
    return {
      id: dto.id,
      name: dto.name,
      location: dto.location,
      capacity: dto.capacity,
      description: dto.description,
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt)
    }
  }
} 
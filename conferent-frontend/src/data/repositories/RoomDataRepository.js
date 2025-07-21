import { apiClient } from '../api/ApiClient'

export interface RoomDto {
  id: number
  name: string
  location: string
  capacity: number
  description?: string
  createdAt: string
  updatedAt: string
}

export interface CreateRoomDto {
  name: string
  location: string
  capacity: number
  description?: string
}

export interface RoomDataRepository {
  getRooms(minCapacity?: number): Promise<RoomDto[]>
  getRoomById(id: number): Promise<RoomDto>
  createRoom(request: CreateRoomDto): Promise<RoomDto>
}

export class ApiRoomDataRepository implements RoomDataRepository {
  async getRooms(minCapacity?: number): Promise<RoomDto[]> {
    const params = minCapacity ? { minCapacity } : {}
    return await apiClient.get<RoomDto[]>('/rooms', { params })
  }

  async getRoomById(id: number): Promise<RoomDto> {
    return await apiClient.get<RoomDto>(`/rooms/${id}`)
  }

  async createRoom(request: CreateRoomDto): Promise<RoomDto> {
    return await apiClient.post<RoomDto>('/rooms', request)
  }
} 
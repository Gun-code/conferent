export interface RoomModel {
  id: number
  name: string
  location: string
  capacity: number
  description?: string
  createdAt: Date
  updatedAt: Date
}

export class RoomEntity {
  constructor(
    public readonly id: number,
    public readonly name: string,
    public readonly location: string,
    public readonly capacity: number,
    public readonly description: string | undefined,
    public readonly createdAt: Date,
    public readonly updatedAt: Date
  ) {}

  canAccommodate(requiredCapacity: number): boolean {
    return this.capacity >= requiredCapacity
  }

  getDisplayName(): string {
    return `${this.name} (${this.location})`
  }

  getFormattedCapacity(): string {
    return `${this.capacity}명`
  }

  static fromModel(model: RoomModel): RoomEntity {
    return new RoomEntity(
      model.id,
      model.name,
      model.location,
      model.capacity,
      model.description,
      model.createdAt,
      model.updatedAt
    )
  }
} 
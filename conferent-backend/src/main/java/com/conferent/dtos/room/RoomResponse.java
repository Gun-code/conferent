package com.conferent.dtos.room;

import com.conferent.entities.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회의실 응답")
public class RoomResponse {
    
    @Schema(description = "회의실 ID", example = "1")
    private Long id;
    
    @Schema(description = "회의실 이름", example = "대회의실")
    private String name;
    
    @Schema(description = "회의실 위치", example = "3층")
    private String location;
    
    @Schema(description = "수용 인원", example = "20")
    private Integer capacity;
    
    @Schema(description = "회의실 설명", example = "대형 회의용")
    private String description;
    
    @Schema(description = "생성 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime updatedAt;
    
    /**
     * Entity에서 DTO로 변환하는 팩토리 메서드
     */
    public static RoomResponse from(Room room) {
        return new RoomResponse(
            room.getId(),
            room.getName(),
            room.getLocation(),
            room.getCapacity(),
            room.getDescription(),
            room.getCreatedAt(),
            room.getUpdatedAt()
        );
    }
    
    /**
     * DTO에서 Entity로 변환하는 메서드
     */
    public Room toEntity() {
        Room room = new Room();
        room.setId(this.id);
        room.setName(this.name);
        room.setLocation(this.location);
        room.setCapacity(this.capacity);
        room.setDescription(this.description);
        room.setCreatedAt(this.createdAt);
        room.setUpdatedAt(this.updatedAt);
        return room;
    }
} 
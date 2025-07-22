package com.conferent.dtos.roomrent;

import com.conferent.dtos.room.RoomResponse;
import com.conferent.dtos.rent.RentResponse;
import com.conferent.entities.RoomRent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "회의실-예약 연결 응답")
public class RoomRentResponse {
    
    @Schema(description = "연결 ID", example = "1")
    private Long id;
    
    @Schema(description = "회의실 정보")
    private RoomResponse room;
    
    @Schema(description = "예약 정보")
    private RentResponse rent;
    
    @Schema(description = "생성 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime createdAt;
    
    public static RoomRentResponse from(RoomRent roomRent) {
        RoomRentResponse response = new RoomRentResponse();
        response.setId(roomRent.getId());
        response.setRoom(RoomResponse.from(roomRent.getRoom()));
        response.setRent(RentResponse.from(roomRent.getRent()));
        response.setCreatedAt(roomRent.getCreatedAt());
        return response;
    }
} 
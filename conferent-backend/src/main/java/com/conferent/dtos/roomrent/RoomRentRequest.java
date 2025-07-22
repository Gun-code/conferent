package com.conferent.dtos.roomrent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "회의실-예약 연결 생성 요청")
public class RoomRentRequest {
    
    @Schema(description = "회의실 ID", example = "1")
    @NotNull(message = "회의실 ID는 필수입니다")
    private Long roomId;
    
    @Schema(description = "예약 ID", example = "1")
    @NotNull(message = "예약 ID는 필수입니다")
    private Long rentId;
} 
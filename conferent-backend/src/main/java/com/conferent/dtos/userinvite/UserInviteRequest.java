package com.conferent.dtos.userinvite;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "사용자 초대 생성 요청")
public class UserInviteRequest {
    
    @Schema(description = "초대할 사용자 ID", example = "1")
    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;
    
    @Schema(description = "RoomRent ID", example = "1")
    @NotNull(message = "RoomRent ID는 필수입니다")
    private Long roomRentId;
} 
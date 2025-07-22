package com.conferent.dtos.userinvite;

import com.conferent.enums.InviteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "초대 상태 업데이트 요청")
public class UpdateInviteStatusRequest {
    
    @Schema(description = "새로운 초대 상태", example = "ACCEPTED")
    @NotNull(message = "초대 상태는 필수입니다")
    private InviteStatus status;
} 
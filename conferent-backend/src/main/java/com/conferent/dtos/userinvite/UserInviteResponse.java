package com.conferent.dtos.userinvite;

import com.conferent.dtos.user.UserResponse;
import com.conferent.dtos.roomrent.RoomRentResponse;
import com.conferent.entities.UserInvite;
import com.conferent.enums.InviteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "사용자 초대 응답")
public class UserInviteResponse {
    
    @Schema(description = "초대 ID", example = "1")
    private Long id;
    
    @Schema(description = "초대된 사용자 정보")
    private UserResponse user;
    
    @Schema(description = "회의실-예약 연결 정보")
    private RoomRentResponse roomRent;
    
    @Schema(description = "초대 상태", example = "PENDING")
    private InviteStatus status;
    
    @Schema(description = "초대 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime invitedAt;
    
    @Schema(description = "응답 시간", example = "2024-01-15T15:00:00")
    private LocalDateTime respondedAt;
    
    @Schema(description = "생성 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 시간", example = "2024-01-15T15:00:00")
    private LocalDateTime updatedAt;
    
    public static UserInviteResponse from(UserInvite userInvite) {
        UserInviteResponse response = new UserInviteResponse();
        response.setId(userInvite.getId());
        response.setUser(UserResponse.from(userInvite.getUser()));
        response.setRoomRent(RoomRentResponse.from(userInvite.getRoomRent()));
        response.setStatus(userInvite.getStatus());
        response.setInvitedAt(userInvite.getInvitedAt());
        response.setRespondedAt(userInvite.getRespondedAt());
        response.setCreatedAt(userInvite.getCreatedAt());
        response.setUpdatedAt(userInvite.getUpdatedAt());
        return response;
    }
} 
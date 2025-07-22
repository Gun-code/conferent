package com.conferent.dtos.rent;

import com.conferent.dtos.user.UserResponse;
import com.conferent.entities.UserInvite;
import com.conferent.enums.InviteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteeResponse {
    
    private Long inviteId;
    private UserResponse user;
    private InviteStatus status;
    private LocalDateTime invitedAt;
    private LocalDateTime respondedAt;
    
    /**
     * UserInvite Entity에서 DTO로 변환하는 팩토리 메서드
     */
    public static InviteeResponse from(UserInvite userInvite) {
        return new InviteeResponse(
            userInvite.getId(),
            UserResponse.from(userInvite.getUser()),
            userInvite.getStatus(),
            userInvite.getInvitedAt(),
            userInvite.getRespondedAt()
        );
    }
    
    /**
     * 응답 대기 중인지 확인
     */
    public boolean isPending() {
        return status == InviteStatus.PENDING;
    }
    
    /**
     * 수락했는지 확인
     */
    public boolean isAccepted() {
        return status == InviteStatus.ACCEPTED;
    }
    
    /**
     * 거절했는지 확인
     */
    public boolean isDeclined() {
        return status == InviteStatus.DECLINED;
    }
} 
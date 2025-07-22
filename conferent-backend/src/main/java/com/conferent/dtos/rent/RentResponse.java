package com.conferent.dtos.rent;

import com.conferent.dtos.room.RoomResponse;
import com.conferent.dtos.user.UserResponse;
import com.conferent.entities.Rent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentResponse {
    
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String description;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 연관된 회의실들
    private List<RoomResponse> rooms;
    
    // 초대된 사용자들 (초대 상태 포함)
    private List<InviteeResponse> invitees;
    
    /**
     * Entity에서 DTO로 변환하는 팩토리 메서드
     */
    public static RentResponse from(Rent rent) {
        RentResponse response = new RentResponse();
        response.setId(rent.getId());
        response.setStartTime(rent.getStartTime());
        response.setEndTime(rent.getEndTime());
        response.setPurpose(rent.getPurpose());
        response.setDescription(rent.getDescription());
        response.setCreator(UserResponse.from(rent.getCreator()));
        response.setCreatedAt(rent.getCreatedAt());
        response.setUpdatedAt(rent.getUpdatedAt());
        
        // Note: rooms와 invitees는 Service Layer에서 별도로 설정
        return response;
    }
    
    /**
     * 예약 기간 계산 (분 단위)
     */
    public long getDurationInMinutes() {
        if (startTime == null || endTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
    
    /**
     * 예약이 진행 중인지 확인
     */
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return startTime != null && endTime != null && 
               now.isAfter(startTime) && now.isBefore(endTime);
    }
    
    /**
     * 예약이 완료되었는지 확인
     */
    public boolean isCompleted() {
        LocalDateTime now = LocalDateTime.now();
        return endTime != null && now.isAfter(endTime);
    }
} 
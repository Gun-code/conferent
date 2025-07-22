package com.conferent.dtos.rent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회의실 예약 생성 요청")
public class CreateRentRequest {
    
    @Schema(description = "예약 시작 시간", example = "2024-01-15T14:00:00")
    @NotNull(message = "시작 시간은 필수입니다")
    @Future(message = "시작 시간은 현재 시간 이후여야 합니다")
    private LocalDateTime startTime;
    
    @Schema(description = "예약 종료 시간", example = "2024-01-15T16:00:00")
    @NotNull(message = "종료 시간은 필수입니다")
    @Future(message = "종료 시간은 현재 시간 이후여야 합니다")
    private LocalDateTime endTime;
    
    @Schema(description = "예약 목적", example = "프로젝트 킥오프 미팅")
    @Size(max = 200, message = "목적은 200자를 초과할 수 없습니다")
    private String purpose;
    
    @Schema(description = "예약 상세 설명", example = "신규 프로젝트 시작을 위한 팀 미팅")
    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다")
    private String description;
    
    @Schema(description = "예약 생성자 ID", example = "1")
    @NotNull(message = "생성자 ID는 필수입니다")
    private Long creatorId;
    
    @Schema(description = "예약할 회의실 ID 목록", example = "[1, 2]")
    @NotNull(message = "회의실 ID 목록은 필수입니다")
    @Size(min = 1, message = "최소 하나의 회의실을 선택해야 합니다")
    private List<Long> roomIds;
    
    @Schema(description = "초대할 사용자 ID 목록 (선택사항)", example = "[3, 4, 5]")
    private List<Long> inviteeIds; // 초대할 사용자 ID 목록 (선택사항)
    
    /**
     * 시간 유효성 검사
     */
    public boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return false;
        }
        return startTime.isBefore(endTime);
    }
} 
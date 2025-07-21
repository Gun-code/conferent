package com.conferent.business.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Reservation {
    private Long id;
    private Long userId;
    private Long roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder
    public Reservation(Long id, Long userId, Long roomId, LocalDateTime startTime, 
                      LocalDateTime endTime, String purpose, ReservationStatus status,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateTime(startTime, endTime);
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status != null ? status : ReservationStatus.PENDING;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }
    
    private void validateTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("시작 시간과 종료 시간은 필수입니다.");
        }
        
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이전이어야 합니다.");
        }
        
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }
    
    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new IllegalStateException("대기중인 예약만 확정할 수 있습니다.");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        if (!this.status.canBeCancelled()) {
            throw new IllegalStateException("취소할 수 없는 예약입니다.");
        }
        this.status = ReservationStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("확정된 예약만 완료 처리할 수 있습니다.");
        }
        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePurpose(String purpose) {
        if (!this.status.canBeModified()) {
            throw new IllegalStateException("수정할 수 없는 예약입니다.");
        }
        this.purpose = purpose;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isOwnedBy(Long userId) {
        return this.userId.equals(userId);
    }
    
    public boolean overlaps(Reservation other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }
    
    public long getDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
} 
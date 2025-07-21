package com.conferent.persistence.entity;

import com.conferent.business.entity.Reservation;
import com.conferent.business.entity.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class ReservationJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long roomId;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    private String purpose;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    protected ReservationJpaEntity() {
        // JPA 기본 생성자
    }
    
    public ReservationJpaEntity(Long userId, Long roomId, LocalDateTime startTime, 
                               LocalDateTime endTime, String purpose, ReservationStatus status) {
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Reservation toDomainEntity() {
        return Reservation.builder()
            .id(this.id)
            .userId(this.userId)
            .roomId(this.roomId)
            .startTime(this.startTime)
            .endTime(this.endTime)
            .purpose(this.purpose)
            .status(this.status)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
    
    public static ReservationJpaEntity fromDomainEntity(Reservation reservation) {
        ReservationJpaEntity entity = new ReservationJpaEntity(
            reservation.getUserId(),
            reservation.getRoomId(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getPurpose(),
            reservation.getStatus()
        );
        entity.id = reservation.getId();
        entity.createdAt = reservation.getCreatedAt();
        entity.updatedAt = reservation.getUpdatedAt();
        return entity;
    }
    
    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getRoomId() { return roomId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
} 
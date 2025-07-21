package com.conferent.business.entity;

public enum ReservationStatus {
    PENDING("대기중"),
    CONFIRMED("확정"),
    CANCELLED("취소됨"),
    COMPLETED("완료됨");
    
    private final String description;
    
    ReservationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canBeModified() {
        return this == PENDING || this == CONFIRMED;
    }
    
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
} 
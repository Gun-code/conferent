package com.conferent.enums;

public enum InviteStatus {
    PENDING("대기중"),
    ACCEPTED("수락됨"),
    DECLINED("거절됨");
    
    private final String description;
    
    InviteStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 
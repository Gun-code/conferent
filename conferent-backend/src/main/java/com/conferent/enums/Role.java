package com.conferent.enums;

public enum Role {
    USER("사용자"),
    ADMIN("관리자");
    
    private final String description;
    
    Role(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 
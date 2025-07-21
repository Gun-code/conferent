package com.conferent.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateRoomRequest {
    
    @NotBlank(message = "회의실 이름은 필수입니다.")
    private String name;
    
    @NotBlank(message = "위치는 필수입니다.")
    private String location;
    
    @NotNull(message = "수용인원은 필수입니다.")
    @Min(value = 1, message = "수용인원은 최소 1명 이상이어야 합니다.")
    private Integer capacity;
    
    private String description;
    
    // 기본 생성자
    public CreateRoomRequest() {}
    
    // 생성자
    public CreateRoomRequest(String name, String location, Integer capacity, String description) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 
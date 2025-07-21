package com.conferent.persistence.entity;

import com.conferent.business.entity.Room;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class RoomJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private Integer capacity;
    
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    protected RoomJpaEntity() {
        // JPA 기본 생성자
    }
    
    public RoomJpaEntity(String name, String location, Integer capacity, String description) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Room toDomainEntity() {
        return Room.builder()
            .id(this.id)
            .name(this.name)
            .location(this.location)
            .capacity(this.capacity)
            .description(this.description)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
    
    public static RoomJpaEntity fromDomainEntity(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity(
            room.getName(),
            room.getLocation(),
            room.getCapacity(),
            room.getDescription()
        );
        entity.id = room.getId();
        entity.createdAt = room.getCreatedAt();
        entity.updatedAt = room.getUpdatedAt();
        return entity;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public Integer getCapacity() { return capacity; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
} 
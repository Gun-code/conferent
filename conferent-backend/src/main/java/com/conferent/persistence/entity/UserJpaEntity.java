package com.conferent.persistence.entity;

import com.conferent.business.entity.User;
import com.conferent.business.entity.UserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class UserJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    protected UserJpaEntity() {
        // JPA 기본 생성자
    }
    
    public UserJpaEntity(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public User toDomainEntity() {
        return User.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .password(this.password)
            .role(this.role)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }
    
    public static UserJpaEntity fromDomainEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity(
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole()
        );
        entity.id = user.getId();
        entity.createdAt = user.getCreatedAt();
        entity.updatedAt = user.getUpdatedAt();
        return entity;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
} 
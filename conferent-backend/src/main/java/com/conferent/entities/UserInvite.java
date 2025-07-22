package com.conferent.entities;

import com.conferent.enums.InviteStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_invites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInvite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
        
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;
    
    @Column(name = "invited_at", nullable = false)
    private LocalDateTime invitedAt;
    
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 특정 회의실-예약 조합에 대한 초대
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_rent_id", nullable = false)
    private RoomRent roomRent;
} 
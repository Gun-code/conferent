package com.conferent.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_rents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id", nullable = false)
    private Rent rent;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // 회의실별 초대된 사용자들
    @OneToMany(mappedBy = "roomRent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserInvite> userInvites;
} 
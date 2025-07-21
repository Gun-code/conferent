package com.conferent.persistence.repository;

import com.conferent.business.entity.ReservationStatus;
import com.conferent.persistence.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepositorySpring extends JpaRepository<ReservationJpaEntity, Long> {
    List<ReservationJpaEntity> findByUserId(Long userId);
    List<ReservationJpaEntity> findByRoomId(Long roomId);
    List<ReservationJpaEntity> findByStatus(ReservationStatus status);
    
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.roomId = :roomId " +
           "AND ((r.startTime < :endTime AND r.endTime > :startTime))")
    List<ReservationJpaEntity> findByRoomIdAndTimeRange(
        @Param("roomId") Long roomId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
} 
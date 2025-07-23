package com.conferent.repositories.rent;

import com.conferent.entities.Rent;
import com.conferent.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    
    /**
     * 생성자별 예약 조회
     */
    List<Rent> findByCreatorOrderByStartTimeDesc(User creator);
    
    /**
     * 생성자 ID별 예약 조회
     */
    List<Rent> findByCreatorIdOrderByStartTimeDesc(Long creatorId);
    
    /**
     * 기간별 예약 조회
     */
    @Query("SELECT r FROM Rent r WHERE r.startTime >= :startDate AND r.endTime <= :endDate ORDER BY r.startTime")
    List<Rent> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 특정 시간 이후 예약 조회
     */
    List<Rent> findByStartTimeAfterOrderByStartTime(LocalDateTime startTime);
    
    /**
     * 목적으로 검색
     */
    List<Rent> findByPurposeContainingOrderByStartTimeDesc(String purpose);
    
    /**
     * 시간 충돌 검사 (특정 시간대에 겹치는 예약이 있는지)
     */
    @Query("SELECT r FROM Rent r WHERE r.startTime < :endTime AND r.endTime > :startTime")
    List<Rent> findConflictingRents(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 모든 예약을 시작시간 역순으로 조회
     */
    @Query("SELECT r FROM Rent r ORDER BY r.startTime DESC")
    List<Rent> findAllOrderByStartTimeDesc();
    
    /**
     * 특정 회의실의 예약들 조회 (RoomRent를 통해)
     */
    @Query("SELECT r FROM Rent r JOIN r.roomRents rr WHERE rr.room.id = :roomId ORDER BY r.startTime DESC")
    List<Rent> findByRoomId(@Param("roomId") Long roomId);
    
    /**
     * 특정 회의실의 시간 충돌 검사
     */
    @Query("SELECT r FROM Rent r JOIN r.roomRents rr WHERE rr.room.id = :roomId AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Rent> findConflictingRentsByRoomId(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 특정 사용자의 현재 ~ 미래 예약 조회
     */
    @Query("SELECT r FROM Rent r WHERE r.creator.id = :userId AND r.startTime >= :now ORDER BY r.startTime DESC")
    List<Rent> findByCreatorIdAndStartTimeAfterOrderByStartTimeDesc(@Param("userId") Long userId, @Param("now") LocalDateTime now);
} 
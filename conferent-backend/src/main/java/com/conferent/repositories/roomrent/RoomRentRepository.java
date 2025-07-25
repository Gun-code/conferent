package com.conferent.repositories.roomrent;

import com.conferent.entities.RoomRent;
import com.conferent.entities.Room;
import com.conferent.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRentRepository extends JpaRepository<RoomRent, Long> {
    
    /**
     * 특정 예약의 회의실들 조회
     */
    List<RoomRent> findByRent(Rent rent);
    
    /**
     * 특정 회의실의 예약들 조회
     */
    List<RoomRent> findByRoom(Room room);
    
    /**
     * 예약 ID로 회의실들 조회
     */
    List<RoomRent> findByRentId(Long rentId);
    
    /**
     * 회의실 ID로 예약들 조회
     */
    List<RoomRent> findByRoomId(Long roomId);
    
    /**
     * 특정 회의실과 예약의 연결 존재 여부 확인
     */
    boolean existsByRoomIdAndRentId(Long roomId, Long rentId);
    
    /**
     * 특정 회의실과 예약의 연결 삭제
     */
    void deleteByRoomIdAndRentId(Long roomId, Long rentId);
    
    /**
     * 특정 예약의 모든 회의실 연결 삭제
     */
    void deleteByRentId(Long rentId);
    
    /**
     * 특정 회의실의 모든 예약 연결 삭제
     */
    void deleteByRoomId(Long roomId);
    
    /**
     * 특정 예약과 회의실의 연결 조회
     */
    Optional<RoomRent> findByRentIdAndRoomId(Long rentId, Long roomId);
    
    /**
     * 특정 회의실의 특정 시간대 예약 조회 (시간 충돌 체크용)
     */
    @Query("SELECT rr FROM RoomRent rr WHERE rr.room.id = :roomId AND " +
           "rr.rent.startTime < :endTime AND rr.rent.endTime > :startTime")
    List<RoomRent> findConflictingReservations(@Param("roomId") Long roomId, 
                                              @Param("startTime") LocalDateTime startTime, 
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 특정 시간대에 예약된 회의실들 조회 (기존 메서드 유지)
     */
    @Query("SELECT rr FROM RoomRent rr WHERE rr.room.id NOT IN (SELECT rr2.room.id FROM RoomRent rr2 WHERE rr2.rent.startTime <= :endTime AND rr2.rent.endTime >= :startTime)")
    List<RoomRent> findByAvailableRoom(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 특정 시간대에 이용 가능한 회의실들 조회 (Room 엔티티 직접 반환)
     */
    @Query("SELECT DISTINCT r FROM Room r WHERE r.id NOT IN " +
           "(SELECT rr.room.id FROM RoomRent rr " +
           "WHERE rr.rent.startTime < :endTime AND rr.rent.endTime > :startTime)")
    List<Room> findAvailableRooms(@Param("startTime") LocalDateTime startTime, 
                                  @Param("endTime") LocalDateTime endTime);
} 
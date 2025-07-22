package com.conferent.services.roomrent;

import com.conferent.entities.RoomRent;
import com.conferent.entities.Room;
import com.conferent.entities.Rent;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRentService {
    
    /**
     * 모든 회의실-예약 연결 조회
     */
    List<RoomRent> getAllRoomRents();
    
    /**
     * ID로 회의실-예약 연결 조회
     */
    RoomRent getRoomRentById(Long id);
    
    /**
     * 특정 예약의 회의실들 조회
     */
    List<RoomRent> getRoomRentsByRentId(Long rentId);
    
    /**
     * 특정 회의실의 예약들 조회
     */
    List<RoomRent> getRoomRentsByRoomId(Long roomId);
    
    /**
     * 회의실-예약 연결 생성
     */
    RoomRent createRoomRent(Long roomId, Long rentId);
    
    /**
     * 회의실-예약 연결 삭제
     */
    void deleteRoomRent(Long id);
    
    /**
     * 특정 예약의 모든 회의실 연결 삭제
     */
    void deleteByRentId(Long rentId);
    
    /**
     * 특정 회의실의 모든 예약 연결 삭제
     */
    void deleteByRoomId(Long roomId);
    
    /**
     * 특정 회의실의 시간 충돌 검사
     */
    List<RoomRent> findConflictingReservations(Long roomId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 특정 회의실과 예약의 연결 존재 여부 확인
     */
    boolean existsByRoomIdAndRentId(Long roomId, Long rentId);
    
    /**
     * 특정 예약과 회의실의 연결 조회
     */
    RoomRent findByRentIdAndRoomId(Long rentId, Long roomId);
} 
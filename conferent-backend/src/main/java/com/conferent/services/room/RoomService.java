package com.conferent.services.room;

import com.conferent.dtos.room.RoomRequest;
import com.conferent.dtos.room.RoomResponse;

import java.util.List;

public interface RoomService {
    
    /**
     * 모든 회의실 조회
     */
    List<RoomResponse> getAllRooms();
    
    /**
     * ID로 회의실 조회
     */
    RoomResponse getRoomById(Long id);
    
    /**
     * 회의실 이름으로 검색
     */
    List<RoomResponse> searchRoomsByName(String name);
    
    /**
     * 수용 인원으로 필터링
     */
    List<RoomResponse> findRoomsByCapacity(Integer minCapacity);
    
    /**
     * 위치로 필터링
     */
    List<RoomResponse> findRoomsByLocation(String location);
    
    /**
     * 회의실 생성
     */
    RoomResponse createRoom(RoomRequest request);
    
    /**
     * 회의실 수정
     */
    RoomResponse updateRoom(Long id, RoomRequest request);
    
    /**
     * 회의실 삭제
     */
    void deleteRoom(Long id);
    
    /**
     * 회의실 존재 여부 확인
     */
    boolean existsRoom(Long id);
} 
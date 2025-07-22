package com.conferent.repositories.room;

import com.conferent.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * 회의실 이름으로 검색
     */
    List<Room> findByNameContainingOrderByName(String name);
    
    /**
     * 수용 인원으로 필터링
     */
    List<Room> findByCapacityGreaterThanEqualOrderByCapacity(Integer capacity);
    
    /**
     * 위치로 필터링
     */
    List<Room> findByLocationOrderByName(String location);
    
    /**
     * 이름 중복 확인
     */
    boolean existsByName(String name);
    
    /**
     * 모든 회의실을 이름 순으로 조회
     */
    @Query("SELECT r FROM Room r ORDER BY r.name")
    List<Room> findAllOrderByName();
} 
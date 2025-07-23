package com.conferent.services.rent;

import com.conferent.dtos.rent.CreateRentRequest;
import com.conferent.dtos.rent.RentResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RentService {
    
    /**
     * 모든 예약 조회
     */
    List<RentResponse> getAllRents();
    
    /**
     * ID로 예약 조회
     */
    RentResponse getRentById(Long id);
    
    /**
     * 생성자 ID별 예약 조회
     */
    List<RentResponse> getRentsByCreatorId(Long creatorId);
    
    /**
     * 기간별 예약 조회
     */
    List<RentResponse> getRentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 특정 시간 이후 예약 조회
     */
    List<RentResponse> getUpcomingRents(LocalDateTime fromTime);
    
    /**
     * 목적으로 검색
     */
    List<RentResponse> searchRentsByPurpose(String purpose);
    
    /**
     * 특정 회의실의 예약 조회
     */
    List<RentResponse> getRentsByRoomId(Long roomId);
    
    /**
     * 예약 생성
     */
    RentResponse createRent(CreateRentRequest request);
    
    /**
     * 예약 수정
     */
    RentResponse updateRent(Long id, CreateRentRequest request);
    
    /**
     * 예약 삭제
     */
    void deleteRent(Long id);
    
    /**
     * 예약 존재 여부 확인
     */
    boolean existsRent(Long id);
    
    /**
     * 시간 충돌 검사
     */
    boolean hasTimeConflict(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 시간 충돌 검사 (기존 예약 제외)
     */
    boolean hasTimeConflict(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime, Long excludeRentId);

    /**
     * 최근 예약 조회
     */
    List<RentResponse> getRecentRents(Long userId);
} 
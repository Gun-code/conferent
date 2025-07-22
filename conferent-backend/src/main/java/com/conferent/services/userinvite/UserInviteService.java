package com.conferent.services.userinvite;

import com.conferent.entities.UserInvite;
import com.conferent.enums.InviteStatus;

import java.util.List;

public interface UserInviteService {
    
    /**
     * 모든 사용자 초대 조회
     */
    List<UserInvite> getAllUserInvites();
    
    /**
     * ID로 사용자 초대 조회
     */
    UserInvite getUserInviteById(Long id);
    
    /**
     * 특정 사용자의 초대 목록 조회
     */
    List<UserInvite> getUserInvitesByUserId(Long userId);
    
    /**
     * 특정 예약의 초대 목록 조회
     */
    List<UserInvite> getUserInvitesByRentId(Long rentId);
    
    /**
     * 특정 사용자의 특정 상태 초대 목록 조회
     */
    List<UserInvite> getUserInvitesByUserIdAndStatus(Long userId, InviteStatus status);
    
    /**
     * 사용자 초대 생성
     */
    UserInvite createUserInvite(Long userId, Long roomRentId);
    
    /**
     * 사용자 초대 상태 업데이트
     */
    UserInvite updateInviteStatus(Long id, InviteStatus status);
    
    /**
     * 사용자 초대 삭제
     */
    void deleteUserInvite(Long id);
    
    /**
     * 특정 사용자의 모든 초대 삭제
     */
    void deleteByUserId(Long userId);
    
    /**
     * 특정 RoomRent의 모든 초대 삭제
     */
    void deleteByRoomRentId(Long roomRentId);
    
    /**
     * 특정 사용자와 RoomRent의 초대 존재 여부 확인
     */
    boolean existsByUserIdAndRoomRentId(Long userId, Long roomRentId);
    
    /**
     * 특정 사용자와 RoomRent의 초대 조회
     */
    UserInvite findByUserIdAndRoomRentId(Long userId, Long roomRentId);
    
    /**
     * 사용자의 대기 중인 초대 개수 조회
     */
    long countPendingInvitesByUserId(Long userId);
    
    /**
     * 예약의 수락된 초대 개수 조회
     */
    long countAcceptedInvitesForRent(Long rentId);
} 
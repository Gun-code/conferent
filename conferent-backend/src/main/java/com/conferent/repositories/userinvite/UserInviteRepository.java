package com.conferent.repositories.userinvite;

import com.conferent.entities.UserInvite;
import com.conferent.entities.User;
import com.conferent.entities.Rent;
import com.conferent.enums.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInviteRepository extends JpaRepository<UserInvite, Long> {
    
    /**
     * 특정 사용자의 초대 목록 조회
     */
    List<UserInvite> findByUser(User user);
    
    /**
     * 사용자 ID로 초대 목록 조회
     */
    List<UserInvite> findByUserId(Long userId);
    
    /**
     * 특정 사용자의 특정 상태 초대 목록 조회
     */
    List<UserInvite> findByUserIdAndStatus(Long userId, InviteStatus status);
    
    /**
     * 특정 사용자와 RoomRent의 초대 조회
     */
    Optional<UserInvite> findByUserIdAndRoomRentId(Long userId, Long roomRentId);
    
    /**
     * 특정 사용자와 RoomRent의 초대 존재 여부 확인
     */
    boolean existsByUserIdAndRoomRentId(Long userId, Long roomRentId);
    
    /**
     * 특정 사용자와 RoomRent의 초대 삭제
     */
    void deleteByUserIdAndRoomRentId(Long userId, Long roomRentId);
    
    /**
     * 특정 RoomRent의 모든 초대 삭제
     */
    void deleteByRoomRentId(Long roomRentId);
    
    /**
     * 특정 사용자의 모든 초대 삭제
     */
    void deleteByUserId(Long userId);
    
    /**
     * 사용자의 대기 중인 초대 개수 조회
     */
    @Query("SELECT COUNT(ui) FROM UserInvite ui WHERE ui.user.id = :userId AND ui.status = 'PENDING'")
    long countPendingInvitesByUserId(@Param("userId") Long userId);
    
    /**
     * 특정 예약의 수락된 초대 개수 조회 (RoomRent를 통해)
     */
    @Query("SELECT COUNT(ui) FROM UserInvite ui WHERE ui.roomRent.rent.id = :rentId AND ui.status = 'ACCEPTED'")
    long countAcceptedInvitesForRent(@Param("rentId") Long rentId);
    
    /**
     * 특정 예약의 초대 목록 조회 (RoomRent를 통해)
     */
    @Query("SELECT ui FROM UserInvite ui WHERE ui.roomRent.rent.id = :rentId")
    List<UserInvite> findInvitesForRent(@Param("rentId") Long rentId);
} 
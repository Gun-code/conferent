package com.conferent.services.userinvite.impl;

import com.conferent.entities.UserInvite;
import com.conferent.entities.User;
import com.conferent.entities.RoomRent;
import com.conferent.enums.InviteStatus;
import com.conferent.exceptions.NotFoundException;
import com.conferent.repositories.userinvite.UserInviteRepository;
import com.conferent.repositories.user.UserRepository;
import com.conferent.repositories.roomrent.RoomRentRepository;
import com.conferent.services.userinvite.UserInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInviteServiceImpl implements UserInviteService {
    
    private final UserInviteRepository userInviteRepository;
    private final UserRepository userRepository;
    private final RoomRentRepository roomRentRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<UserInvite> getAllUserInvites() {
        return userInviteRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserInvite getUserInviteById(Long id) {
        return userInviteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("사용자 초대를 찾을 수 없습니다. ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserInvite> getUserInvitesByUserId(Long userId) {
        return userInviteRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserInvite> getUserInvitesByRentId(Long rentId) {
        return userInviteRepository.findInvitesForRent(rentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserInvite> getUserInvitesByUserIdAndStatus(Long userId, InviteStatus status) {
        return userInviteRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    public UserInvite createUserInvite(Long userId, Long roomRentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));
        
        RoomRent roomRent = roomRentRepository.findById(roomRentId)
                .orElseThrow(() -> new NotFoundException("회의실-예약 연결을 찾을 수 없습니다. ID: " + roomRentId));
        
        UserInvite userInvite = new UserInvite();
        userInvite.setUser(user);
        userInvite.setRoomRent(roomRent);
        userInvite.setStatus(InviteStatus.PENDING);
        userInvite.setInvitedAt(LocalDateTime.now());
        
        return userInviteRepository.save(userInvite);
    }
    
    @Override
    public UserInvite updateInviteStatus(Long id, InviteStatus status) {
        UserInvite userInvite = getUserInviteById(id);
        userInvite.setStatus(status);
        userInvite.setRespondedAt(LocalDateTime.now());
        
        return userInviteRepository.save(userInvite);
    }
    
    @Override
    public void deleteUserInvite(Long id) {
        if (!userInviteRepository.existsById(id)) {
            throw new NotFoundException("사용자 초대를 찾을 수 없습니다. ID: " + id);
        }
        userInviteRepository.deleteById(id);
    }
    
    @Override
    public void deleteByUserId(Long userId) {
        userInviteRepository.deleteByUserId(userId);
    }
    
    @Override
    public void deleteByRoomRentId(Long roomRentId) {
        userInviteRepository.deleteByRoomRentId(roomRentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndRoomRentId(Long userId, Long roomRentId) {
        return userInviteRepository.existsByUserIdAndRoomRentId(userId, roomRentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserInvite findByUserIdAndRoomRentId(Long userId, Long roomRentId) {
        Optional<UserInvite> userInvite = userInviteRepository.findByUserIdAndRoomRentId(userId, roomRentId);
        return userInvite.orElseThrow(() -> 
            new NotFoundException("사용자 초대를 찾을 수 없습니다. User ID: " + userId + ", RoomRent ID: " + roomRentId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countPendingInvitesByUserId(Long userId) {
        return userInviteRepository.countPendingInvitesByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countAcceptedInvitesForRent(Long rentId) {
        return userInviteRepository.countAcceptedInvitesForRent(rentId);
    }
} 
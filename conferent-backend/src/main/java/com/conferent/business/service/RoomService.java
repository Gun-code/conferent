package com.conferent.business.service;

import com.conferent.business.entity.Room;
import com.conferent.business.entity.User;
import com.conferent.business.exception.BusinessException;
import com.conferent.persistence.repository.RoomRepositoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomService {
    
    private final RoomRepositoryInterface roomRepository;
    private final UserService userService;
    
    public RoomService(RoomRepositoryInterface roomRepository, UserService userService) {
        this.roomRepository = roomRepository;
        this.userService = userService;
    }
    
    public Room createRoom(Long adminUserId, String name, String location, Integer capacity, String description) {
        User admin = userService.getUserById(adminUserId);
        if (!admin.isAdmin()) {
            throw new BusinessException("회의실 생성 권한이 없습니다.");
        }
        
        Room room = Room.builder()
            .name(name)
            .location(location)
            .capacity(capacity)
            .description(description)
            .build();
            
        return roomRepository.save(room);
    }
    
    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new BusinessException("회의실을 찾을 수 없습니다."));
    }
    
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Room> getRoomsByMinCapacity(int minCapacity) {
        return roomRepository.findByCapacityGreaterThanEqual(minCapacity);
    }
    
    public Room updateRoom(Long adminUserId, Long roomId, String name, String location, Integer capacity, String description) {
        User admin = userService.getUserById(adminUserId);
        if (!admin.isAdmin()) {
            throw new BusinessException("회의실 수정 권한이 없습니다.");
        }
        
        Room room = getRoomById(roomId);
        room.updateInfo(name, location, capacity, description);
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long adminUserId, Long roomId) {
        User admin = userService.getUserById(adminUserId);
        if (!admin.isAdmin()) {
            throw new BusinessException("회의실 삭제 권한이 없습니다.");
        }
        
        if (!roomRepository.existsById(roomId)) {
            throw new BusinessException("존재하지 않는 회의실입니다.");
        }
        
        roomRepository.deleteById(roomId);
    }
} 
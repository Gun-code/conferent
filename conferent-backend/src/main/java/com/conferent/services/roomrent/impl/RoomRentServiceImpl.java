package com.conferent.services.roomrent.impl;

import com.conferent.entities.RoomRent;
import com.conferent.entities.Room;
import com.conferent.entities.Rent;
import com.conferent.exceptions.NotFoundException;
import com.conferent.repositories.roomrent.RoomRentRepository;
import com.conferent.repositories.room.RoomRepository;
import com.conferent.repositories.rent.RentRepository;
import com.conferent.services.roomrent.RoomRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomRentServiceImpl implements RoomRentService {
    
    private final RoomRentRepository roomRentRepository;
    private final RoomRepository roomRepository;
    private final RentRepository rentRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomRent> getAllRoomRents() {
        return roomRentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoomRent getRoomRentById(Long id) {
        return roomRentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회의실-예약 연결을 찾을 수 없습니다. ID: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomRent> getRoomRentsByRentId(Long rentId) {
        return roomRentRepository.findByRentId(rentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomRent> getRoomRentsByRoomId(Long roomId) {
        return roomRentRepository.findByRoomId(roomId);
    }
    
    @Override
    public RoomRent createRoomRent(Long roomId, Long rentId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다. ID: " + roomId));
        
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다. ID: " + rentId));
        
        RoomRent roomRent = new RoomRent();
        roomRent.setRoom(room);
        roomRent.setRent(rent);
        
        return roomRentRepository.save(roomRent);
    }
    
    @Override
    public void deleteRoomRent(Long id) {
        if (!roomRentRepository.existsById(id)) {
            throw new NotFoundException("회의실-예약 연결을 찾을 수 없습니다. ID: " + id);
        }
        roomRentRepository.deleteById(id);
    }
    
    @Override
    public void deleteByRentId(Long rentId) {
        roomRentRepository.deleteByRentId(rentId);
    }
    
    @Override
    public void deleteByRoomId(Long roomId) {
        roomRentRepository.deleteByRoomId(roomId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomRent> findConflictingReservations(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return roomRentRepository.findConflictingReservations(roomId, startTime, endTime);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoomIdAndRentId(Long roomId, Long rentId) {
        return roomRentRepository.existsByRoomIdAndRentId(roomId, rentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoomRent findByRentIdAndRoomId(Long rentId, Long roomId) {
        Optional<RoomRent> roomRent = roomRentRepository.findByRentIdAndRoomId(rentId, roomId);
        return roomRent.orElseThrow(() -> 
            new NotFoundException("회의실-예약 연결을 찾을 수 없습니다. Rent ID: " + rentId + ", Room ID: " + roomId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAvailableRooms() {
        // 현재 시간부터 1시간 후까지 사용 가능한 회의실 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        return roomRentRepository.findAvailableRooms(now, oneHourLater);
    }
} 
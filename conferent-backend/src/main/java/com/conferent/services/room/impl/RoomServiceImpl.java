package com.conferent.services.room.impl;

import com.conferent.dtos.room.RoomRequest;
import com.conferent.dtos.room.RoomResponse;
import com.conferent.entities.Room;
import com.conferent.exceptions.NotFoundException;
import com.conferent.repositories.room.RoomRepository;
import com.conferent.services.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository roomRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAllOrderByName()
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다. ID: " + id));
        return RoomResponse.from(room);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> searchRoomsByName(String name) {
        return roomRepository.findByNameContainingOrderByName(name)
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> findRoomsByCapacity(Integer minCapacity) {
        return roomRepository.findByCapacityGreaterThanEqualOrderByCapacity(minCapacity)
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> findRoomsByLocation(String location) {
        return roomRepository.findByLocationOrderByName(location)
                .stream()
                .map(RoomResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    public RoomResponse createRoom(RoomRequest request) {
        // 이름 중복 체크
        if (roomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 회의실 이름입니다: " + request.getName());
        }
        
        Room room = new Room();
        room.setName(request.getName());
        room.setLocation(request.getLocation());
        room.setCapacity(request.getCapacity());
        room.setDescription(request.getDescription());
                
        Room savedRoom = roomRepository.save(room);
        return RoomResponse.from(savedRoom);
    }
    
    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다. ID: " + id));
        
        // 이름이 변경되었고 중복되는지 체크
        if (!existingRoom.getName().equals(request.getName()) && 
            roomRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 회의실 이름입니다: " + request.getName());
        }
        
        existingRoom.setName(request.getName());
        existingRoom.setLocation(request.getLocation());
        existingRoom.setCapacity(request.getCapacity());
        existingRoom.setDescription(request.getDescription());        
        Room updatedRoom = roomRepository.save(existingRoom);
        return RoomResponse.from(updatedRoom);
    }
    
    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException("회의실을 찾을 수 없습니다. ID: " + id);
        }
        roomRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsRoom(Long id) {
        return roomRepository.existsById(id);
    }

} 
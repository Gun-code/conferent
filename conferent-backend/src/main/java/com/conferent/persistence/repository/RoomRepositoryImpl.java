package com.conferent.persistence.repository;

import com.conferent.business.entity.Room;
import com.conferent.persistence.entity.RoomJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RoomRepositoryImpl implements RoomRepositoryInterface {
    
    private final RoomJpaRepositorySpring roomJpaRepository;
    
    public RoomRepositoryImpl(RoomJpaRepositorySpring roomJpaRepository) {
        this.roomJpaRepository = roomJpaRepository;
    }
    
    @Override
    public Room save(Room room) {
        RoomJpaEntity entity = RoomJpaEntity.fromDomainEntity(room);
        RoomJpaEntity savedEntity = roomJpaRepository.save(entity);
        return savedEntity.toDomainEntity();
    }
    
    @Override
    public Optional<Room> findById(Long id) {
        return roomJpaRepository.findById(id)
            .map(RoomJpaEntity::toDomainEntity);
    }
    
    @Override
    public List<Room> findAll() {
        return roomJpaRepository.findAll().stream()
            .map(RoomJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Room> findByCapacityGreaterThanEqual(int capacity) {
        return roomJpaRepository.findByCapacityGreaterThanEqual(capacity).stream()
            .map(RoomJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        roomJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return roomJpaRepository.existsById(id);
    }
} 
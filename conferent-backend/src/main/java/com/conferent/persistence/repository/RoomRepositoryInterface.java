package com.conferent.persistence.repository;

import com.conferent.business.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryInterface {
    Room save(Room room);
    Optional<Room> findById(Long id);
    List<Room> findAll();
    List<Room> findByCapacityGreaterThanEqual(int capacity);
    void deleteById(Long id);
    boolean existsById(Long id);
} 
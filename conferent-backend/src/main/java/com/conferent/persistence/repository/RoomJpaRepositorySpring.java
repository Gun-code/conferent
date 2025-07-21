package com.conferent.persistence.repository;

import com.conferent.persistence.entity.RoomJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomJpaRepositorySpring extends JpaRepository<RoomJpaEntity, Long> {
    List<RoomJpaEntity> findByCapacityGreaterThanEqual(Integer capacity);
} 
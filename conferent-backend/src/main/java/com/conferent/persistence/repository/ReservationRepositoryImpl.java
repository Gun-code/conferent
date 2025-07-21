package com.conferent.persistence.repository;

import com.conferent.business.entity.Reservation;
import com.conferent.business.entity.ReservationStatus;
import com.conferent.persistence.entity.ReservationJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ReservationRepositoryImpl implements ReservationRepositoryInterface {
    
    private final ReservationJpaRepositorySpring reservationJpaRepository;
    
    public ReservationRepositoryImpl(ReservationJpaRepositorySpring reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }
    
    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaEntity entity = ReservationJpaEntity.fromDomainEntity(reservation);
        ReservationJpaEntity savedEntity = reservationJpaRepository.save(entity);
        return savedEntity.toDomainEntity();
    }
    
    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationJpaRepository.findById(id)
            .map(ReservationJpaEntity::toDomainEntity);
    }
    
    @Override
    public List<Reservation> findByUserId(Long userId) {
        return reservationJpaRepository.findByUserId(userId).stream()
            .map(ReservationJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByRoomId(Long roomId) {
        return reservationJpaRepository.findByRoomId(roomId).stream()
            .map(ReservationJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByRoomIdAndTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationJpaRepository.findByRoomIdAndTimeRange(roomId, startTime, endTime).stream()
            .map(ReservationJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationJpaRepository.findByStatus(status).stream()
            .map(ReservationJpaEntity::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        reservationJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return reservationJpaRepository.existsById(id);
    }
} 
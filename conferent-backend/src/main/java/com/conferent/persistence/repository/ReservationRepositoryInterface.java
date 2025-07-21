package com.conferent.persistence.repository;

import com.conferent.business.entity.Reservation;
import com.conferent.business.entity.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryInterface {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByRoomIdAndTimeRange(Long roomId, LocalDateTime startTime, LocalDateTime endTime);
    List<Reservation> findByStatus(ReservationStatus status);
    void deleteById(Long id);
    boolean existsById(Long id);
} 
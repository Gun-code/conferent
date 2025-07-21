package com.conferent.business.service;

import com.conferent.business.entity.Reservation;
import com.conferent.business.entity.ReservationStatus;
import com.conferent.business.entity.Room;
import com.conferent.business.entity.User;
import com.conferent.business.exception.BusinessException;
import com.conferent.persistence.repository.ReservationRepositoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    
    private final ReservationRepositoryInterface reservationRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final NotificationService notificationService;
    
    public ReservationService(ReservationRepositoryInterface reservationRepository,
                             UserService userService,
                             RoomService roomService,
                             NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.notificationService = notificationService;
    }
    
    public Reservation createReservation(Long userId, Long roomId, LocalDateTime startTime, 
                                       LocalDateTime endTime, String purpose) {
        // 사용자 및 회의실 검증
        User user = userService.getUserById(userId);
        Room room = roomService.getRoomById(roomId);
        
        // 예약 시간 중복 검증
        validateReservationTime(roomId, startTime, endTime, null);
        
        Reservation reservation = Reservation.builder()
            .userId(userId)
            .roomId(roomId)
            .startTime(startTime)
            .endTime(endTime)
            .purpose(purpose)
            .status(ReservationStatus.PENDING)
            .build();
            
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // 알림 발송
        notificationService.sendReservationConfirmation(
            userId, 
            room.getName(), 
            startTime.toString(), 
            endTime.toString()
        );
        
        return savedReservation;
    }
    
    public void cancelReservation(Long userId, Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        User user = userService.getUserById(userId);
        
        // 권한 검증 (본인 예약이거나 관리자)
        if (!reservation.isOwnedBy(userId) && !user.isAdmin()) {
            throw new BusinessException("예약을 취소할 권한이 없습니다.");
        }
        
        reservation.cancel();
        reservationRepository.save(reservation);
        
        // 알림 발송
        Room room = roomService.getRoomById(reservation.getRoomId());
        notificationService.sendReservationCancellation(
            reservation.getUserId(),
            room.getName(),
            reservation.getStartTime().toString(),
            reservation.getEndTime().toString()
        );
    }
    
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new BusinessException("예약을 찾을 수 없습니다."));
    }
    
    @Transactional(readOnly = true)
    public List<Reservation> getUserReservations(Long userId) {
        userService.getUserById(userId); // 사용자 존재 검증
        return reservationRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public List<Reservation> getRoomReservations(Long roomId) {
        roomService.getRoomById(roomId); // 회의실 존재 검증
        return reservationRepository.findByRoomId(roomId);
    }
    
    private void validateReservationTime(Long roomId, LocalDateTime startTime, 
                                       LocalDateTime endTime, Long excludeReservationId) {
        List<Reservation> conflictingReservations = reservationRepository
            .findByRoomIdAndTimeRange(roomId, startTime, endTime);
        
        boolean hasConflict = conflictingReservations.stream()
            .filter(reservation -> excludeReservationId == null || 
                                 !reservation.getId().equals(excludeReservationId))
            .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED || 
                                 reservation.getStatus() == ReservationStatus.PENDING)
            .anyMatch(reservation -> {
                Reservation newReservation = Reservation.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
                return reservation.overlaps(newReservation);
            });
        
        if (hasConflict) {
            throw new BusinessException("해당 시간에 이미 다른 예약이 존재합니다.");
        }
    }
} 
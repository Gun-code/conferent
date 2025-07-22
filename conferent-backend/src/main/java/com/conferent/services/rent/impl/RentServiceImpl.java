package com.conferent.services.rent.impl;

import com.conferent.dtos.rent.CreateRentRequest;
import com.conferent.dtos.rent.RentResponse;
import com.conferent.dtos.room.RoomResponse;
import com.conferent.dtos.rent.InviteeResponse;
import com.conferent.entities.Rent;
import com.conferent.entities.User;
import com.conferent.entities.Room;
import com.conferent.entities.RoomRent;
import com.conferent.entities.UserInvite;
import com.conferent.enums.InviteStatus;
import com.conferent.exceptions.NotFoundException;
import com.conferent.exceptions.DuplicateReservationException;
import com.conferent.repositories.rent.RentRepository;
import com.conferent.repositories.roomrent.RoomRentRepository;
import com.conferent.repositories.userinvite.UserInviteRepository;
import com.conferent.repositories.user.UserRepository;
import com.conferent.repositories.room.RoomRepository;
import com.conferent.services.rent.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RentServiceImpl implements RentService {
    
    private final RentRepository rentRepository;
    private final RoomRentRepository roomRentRepository;
    private final UserInviteRepository userInviteRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getAllRents() {
        return rentRepository.findAllOrderByStartTimeDesc()
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public RentResponse getRentById(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다. ID: " + id));
        return convertToRentResponse(rent);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getRentsByCreatorId(Long creatorId) {
        return rentRepository.findByCreatorIdOrderByStartTimeDesc(creatorId)
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getRentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rentRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getUpcomingRents(LocalDateTime fromTime) {
        return rentRepository.findByStartTimeAfterOrderByStartTime(fromTime)
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> searchRentsByPurpose(String purpose) {
        return rentRepository.findByPurposeContainingOrderByStartTimeDesc(purpose)
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getRentsByRoomId(Long roomId) {
        return rentRepository.findByRoomId(roomId)
                .stream()
                .map(this::convertToRentResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public RentResponse createRent(CreateRentRequest request) {
        // 입력 유효성 검증
        if (!request.isValidTimeRange()) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이전이어야 합니다");
        }
        
        // 시간 충돌 검사
        if (hasTimeConflict(request.getRoomIds(), request.getStartTime(), request.getEndTime())) {
            throw new DuplicateReservationException("선택한 시간대에 이미 예약된 회의실이 있습니다");
        }
        
        // 생성자 확인
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. ID: " + request.getCreatorId()));
        
        // 예약 생성
        Rent rent = new Rent();
        // 필드 직접 설정을 위해 리플렉션 대신 빌더 패턴 적용 필요
        // 임시로 생성자 사용
        rent = createRentEntity(request.getStartTime(), request.getEndTime(), 
                               request.getPurpose(), request.getDescription(), creator);
        
        Rent savedRent = rentRepository.save(rent);
        
        // 회의실 연결 생성
        for (Long roomId : request.getRoomIds()) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다. ID: " + roomId));
            
            RoomRent roomRent = new RoomRent();
            roomRent.setRent(savedRent);
            roomRent.setRoom(room);
            roomRentRepository.save(roomRent);
        }
        
        // 사용자 초대 생성
        if (request.getInviteeIds() != null && !request.getInviteeIds().isEmpty()) {
            for (Long inviteeId : request.getInviteeIds()) {
                User invitee = userRepository.findById(inviteeId)
                        .orElseThrow(() -> new NotFoundException("초대할 사용자를 찾을 수 없습니다. ID: " + inviteeId));
                
                // 각 회의실별로 초대 생성
                for (Long roomId : request.getRoomIds()) {
                    RoomRent roomRent = roomRentRepository.findByRentIdAndRoomId(savedRent.getId(), roomId)
                            .orElseThrow(() -> new NotFoundException("회의실-예약 연결을 찾을 수 없습니다"));
                    
                    UserInvite userInvite = new UserInvite();
                    userInvite.setRoomRent(roomRent);
                    userInvite.setUser(invitee);
                    userInvite.setStatus(InviteStatus.PENDING);
                    userInvite.setInvitedAt(LocalDateTime.now());
                    userInviteRepository.save(userInvite);
                }
            }
        }
        
        return convertToRentResponse(savedRent);
    }
    
    @Override
    public RentResponse updateRent(Long id, CreateRentRequest request) {
        Rent existingRent = rentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다. ID: " + id));
        
        // 입력 유효성 검증
        if (!request.isValidTimeRange()) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이전이어야 합니다");
        }
        
        // 시간 충돌 검사 (기존 예약 제외)
        if (hasTimeConflict(request.getRoomIds(), request.getStartTime(), request.getEndTime(), id)) {
            throw new DuplicateReservationException("선택한 시간대에 이미 예약된 회의실이 있습니다");
        }
        
        // 예약 정보 업데이트
        Rent updatedRentData = createRentEntity(request.getStartTime(), request.getEndTime(), 
                                               request.getPurpose(), request.getDescription(), existingRent.getCreator());
        updatedRentData.setId(existingRent.getId());
        updatedRentData.setCreatedAt(existingRent.getCreatedAt());
        
        Rent updatedRent = rentRepository.save(updatedRentData);
        
        // 기존 회의실 연결 삭제 후 새로 생성
        roomRentRepository.deleteByRentId(id);
        for (Long roomId : request.getRoomIds()) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다. ID: " + roomId));
            
            RoomRent roomRent = new RoomRent();
            roomRent.setRent(updatedRent);
            roomRent.setRoom(room);
            roomRentRepository.save(roomRent);
        }
        
        return convertToRentResponse(updatedRent);
    }
    
    @Override
    public void deleteRent(Long id) {
        if (!rentRepository.existsById(id)) {
            throw new NotFoundException("예약을 찾을 수 없습니다. ID: " + id);
        }
        
        // 연관된 데이터 먼저 삭제
        List<RoomRent> roomRents = roomRentRepository.findByRentId(id);
        for (RoomRent roomRent : roomRents) {
            userInviteRepository.deleteByRoomRentId(roomRent.getId());
        }
        roomRentRepository.deleteByRentId(id);
        
        // 예약 삭제
        rentRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsRent(Long id) {
        return rentRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasTimeConflict(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime) {
        return hasTimeConflict(roomIds, startTime, endTime, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasTimeConflict(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime, Long excludeRentId) {
        for (Long roomId : roomIds) {
            List<RoomRent> conflictingReservations = roomRentRepository.findConflictingReservations(roomId, startTime, endTime);
            
            // 제외할 예약이 있다면 필터링
            if (excludeRentId != null) {
                conflictingReservations = conflictingReservations.stream()
                        .filter(rr -> !rr.getRent().getId().equals(excludeRentId))
                        .collect(Collectors.toList());
            }
            
            if (!conflictingReservations.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Rent 엔티티를 RentResponse DTO로 변환
     */
    private RentResponse convertToRentResponse(Rent rent) {
        RentResponse response = RentResponse.from(rent);
        
        // 연관된 회의실들 설정
        List<RoomRent> roomRents = roomRentRepository.findByRentId(rent.getId());
        List<RoomResponse> rooms = roomRents.stream()
                .map(rr -> RoomResponse.from(rr.getRoom()))
                .collect(Collectors.toList());
        response.setRooms(rooms);
        
        // 초대된 사용자들 설정
        List<UserInvite> userInvites = userInviteRepository.findInvitesForRent(rent.getId());
        List<InviteeResponse> invitees = userInvites.stream()
                .map(InviteeResponse::from)
                .collect(Collectors.toList());
        response.setInvitees(invitees);
        
        return response;
    }
    
    /**
     * Rent 엔티티 생성 헬퍼 메서드
     */
    private Rent createRentEntity(LocalDateTime startTime, LocalDateTime endTime, 
                                 String purpose, String description, User creator) {
        Rent rent = new Rent();
        // 리플렉션으로 필드 설정하거나, 실제 setter가 있을 때 사용
        try {
            rent.getClass().getDeclaredMethod("setStartTime", LocalDateTime.class).invoke(rent, startTime);
            rent.getClass().getDeclaredMethod("setEndTime", LocalDateTime.class).invoke(rent, endTime);
            rent.getClass().getDeclaredMethod("setPurpose", String.class).invoke(rent, purpose);
            rent.getClass().getDeclaredMethod("setDescription", String.class).invoke(rent, description);
            rent.getClass().getDeclaredMethod("setCreator", User.class).invoke(rent, creator);
        } catch (Exception e) {
            // 간단한 방법으로 빌더 패턴이나 다른 방법 필요
            // 일단 AllArgsConstructor 사용
            rent = new Rent(null, startTime, endTime, purpose, description, creator, 
                           null, null, null);
        }
        return rent;
    }
} 
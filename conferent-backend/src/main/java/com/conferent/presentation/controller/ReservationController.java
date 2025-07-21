package com.conferent.presentation.controller;

import com.conferent.business.entity.Reservation;
import com.conferent.business.entity.Room;
import com.conferent.business.service.ReservationService;
import com.conferent.business.service.RoomService;
import com.conferent.presentation.dto.CreateReservationRequest;
import com.conferent.presentation.dto.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
@Tag(name = "Reservation API", description = "회의실 예약 관리 API")
public class ReservationController {
    
    private final ReservationService reservationService;
    private final RoomService roomService;
    
    public ReservationController(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }
    
    @PostMapping
    @Operation(summary = "회의실 예약 생성", description = "새로운 회의실 예약을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "예약 생성 성공", 
                     content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "예약 충돌")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestHeader("User-Id") Long userId,
            @Parameter(description = "예약 생성 정보", required = true)
            @Valid @RequestBody CreateReservationRequest request) {
        try {
            Reservation reservation = reservationService.createReservation(
                userId, 
                request.getRoomId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getPurpose()
            );
            
            Room room = roomService.getRoomById(request.getRoomId());
            ReservationResponse response = ReservationResponse.fromDomainEntity(reservation, room.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @DeleteMapping("/{reservationId}")
    @Operation(summary = "회의실 예약 취소", description = "기존 회의실 예약을 취소합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "예약 취소 성공"),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음"),
        @ApiResponse(responseCode = "403", description = "권한 없음 (본인 예약만 취소 가능)")
    })
    public ResponseEntity<Void> cancelReservation(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestHeader("User-Id") Long userId,
            @Parameter(description = "예약 ID", required = true, example = "1")
            @PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(userId, reservationId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 예약 목록 조회", description = "특정 사용자의 모든 예약 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                     content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<ReservationResponse>> getUserReservations(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Long userId) {
        try {
            List<Reservation> reservations = reservationService.getUserReservations(userId);
            List<ReservationResponse> responses = reservations.stream()
                .map(reservation -> {
                    Room room = roomService.getRoomById(reservation.getRoomId());
                    return ReservationResponse.fromDomainEntity(reservation, room.getName());
                })
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
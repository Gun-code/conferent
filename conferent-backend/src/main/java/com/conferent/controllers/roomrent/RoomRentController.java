package com.conferent.controllers.roomrent;

import com.conferent.dtos.roomrent.RoomRentRequest;
import com.conferent.dtos.roomrent.RoomRentResponse;
import com.conferent.entities.RoomRent;
import com.conferent.services.roomrent.RoomRentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/room-rents")
@RequiredArgsConstructor
@Tag(name = "회의실-예약 연결 관리", description = "회의실과 예약의 연결 관리 API")
public class RoomRentController {

    private final RoomRentService roomRentService;

    @GetMapping
    @Operation(summary = "회의실-예약 연결 목록 조회", description = "모든 회의실-예약 연결을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class)))
    })
    public ResponseEntity<List<RoomRentResponse>> getAllRoomRents() {
        List<RoomRentResponse> roomRents = roomRentService.getAllRoomRents().stream()
                .map(RoomRentResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(roomRents);
    }

    @GetMapping("/{id}")
    @Operation(summary = "회의실-예약 연결 상세 조회", description = "특정 회의실-예약 연결을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class))),
        @ApiResponse(responseCode = "404", description = "회의실-예약 연결을 찾을 수 없음")
    })
    public ResponseEntity<RoomRentResponse> getRoomRentById(
            @Parameter(description = "회의실-예약 연결 ID", example = "1")
            @PathVariable Long id) {
        RoomRentResponse roomRent = RoomRentResponse.from(roomRentService.getRoomRentById(id));
        return ResponseEntity.ok(roomRent);
    }

    @GetMapping("/rent/{rentId}")
    @Operation(summary = "예약별 회의실 조회", description = "특정 예약에 연결된 회의실들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class)))
    })
    public ResponseEntity<List<RoomRentResponse>> getRoomRentsByRentId(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long rentId) {
        List<RoomRentResponse> roomRents = roomRentService.getRoomRentsByRentId(rentId).stream()
                .map(RoomRentResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(roomRents);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "회의실별 예약 조회", description = "특정 회의실에 연결된 예약들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class)))
    })
    public ResponseEntity<List<RoomRentResponse>> getRoomRentsByRoomId(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long roomId) {
        List<RoomRentResponse> roomRents = roomRentService.getRoomRentsByRoomId(roomId).stream()
                .map(RoomRentResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(roomRents);
    }

    @PostMapping
    @Operation(summary = "회의실-예약 연결 생성", description = "회의실과 예약을 연결합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "회의실 또는 예약을 찾을 수 없음")
    })
    public ResponseEntity<RoomRentResponse> createRoomRent(
            @Parameter(description = "회의실-예약 연결 생성 정보")
            @Valid @RequestBody RoomRentRequest request) {
        RoomRentResponse roomRent = RoomRentResponse.from(roomRentService.createRoomRent(request.getRoomId(), request.getRentId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(roomRent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회의실-예약 연결 삭제", description = "회의실-예약 연결을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회의실-예약 연결을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteRoomRent(
            @Parameter(description = "회의실-예약 연결 ID", example = "1")
            @PathVariable Long id) {
        roomRentService.deleteRoomRent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rent/{rentId}")
    @Operation(summary = "예약별 연결 삭제", description = "특정 예약의 모든 회의실 연결을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteByRentId(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long rentId) {
        roomRentService.deleteByRentId(rentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/room/{roomId}")
    @Operation(summary = "회의실별 연결 삭제", description = "특정 회의실의 모든 예약 연결을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteByRoomId(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long roomId) {
        roomRentService.deleteByRoomId(roomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/conflicts")
    @Operation(summary = "시간 충돌 확인", description = "특정 회의실의 시간 충돌을 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "확인 완료",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class)))
    })
    public ResponseEntity<List<RoomRentResponse>> findConflictingReservations(
            @Parameter(description = "회의실 ID", example = "1")
            @RequestParam Long roomId,
            @Parameter(description = "시작 시간", example = "2024-01-15T14:00:00")
            @RequestParam LocalDateTime startTime,
            @Parameter(description = "종료 시간", example = "2024-01-15T16:00:00")
            @RequestParam LocalDateTime endTime) {
        List<RoomRentResponse> conflicts = roomRentService.findConflictingReservations(roomId, startTime, endTime).stream()
                .map(RoomRentResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(conflicts);
    }

    @GetMapping("/exists")
    @Operation(summary = "연결 존재 여부 확인", description = "특정 회의실과 예약의 연결이 존재하는지 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "확인 완료")
    })
    public ResponseEntity<Boolean> existsByRoomIdAndRentId(
            @Parameter(description = "회의실 ID", example = "1")
            @RequestParam Long roomId,
            @Parameter(description = "예약 ID", example = "1")
            @RequestParam Long rentId) {
        boolean exists = roomRentService.existsByRoomIdAndRentId(roomId, rentId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/find")
    @Operation(summary = "연결 조회", description = "특정 예약과 회의실의 연결을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class))),
        @ApiResponse(responseCode = "404", description = "연결을 찾을 수 없음")
    })
    public ResponseEntity<RoomRentResponse> findByRentIdAndRoomId(
            @Parameter(description = "예약 ID", example = "1")
            @RequestParam Long rentId,
            @Parameter(description = "회의실 ID", example = "1")
            @RequestParam Long roomId) {
        RoomRentResponse roomRent = RoomRentResponse.from(roomRentService.findByRentIdAndRoomId(rentId, roomId));
        return ResponseEntity.ok(roomRent);
    }

    
    @GetMapping("/available")
    @Operation(summary = "이용 가능한 회의실 조회", description = "현재 이용 가능한 회의실 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomRentResponse.class)))
    })
    public ResponseEntity<List<RoomRentResponse>> getAvailableRooms() {
        List<RoomRentResponse> rooms = roomRentService.getAvailableRooms().stream()
                .map(RoomRentResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(rooms);
    }
} 
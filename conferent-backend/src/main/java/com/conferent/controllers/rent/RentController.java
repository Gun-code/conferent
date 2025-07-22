package com.conferent.controllers.rent;

import com.conferent.dtos.rent.CreateRentRequest;
import com.conferent.dtos.rent.RentResponse;
import com.conferent.services.rent.RentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rents")
@RequiredArgsConstructor
@Tag(name = "예약 관리", description = "회의실 예약 CRUD API")
public class RentController {

    private final RentService rentService;

    @GetMapping
    @Operation(summary = "예약 목록 조회", description = "모든 예약 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class)))
    })
    public ResponseEntity<List<RentResponse>> getAllRents() {
        List<RentResponse> rents = rentService.getAllRents();
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/{id}")
    @Operation(summary = "예약 상세 조회", description = "특정 예약의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class))),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    public ResponseEntity<RentResponse> getRentById(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long id) {
        RentResponse rent = rentService.getRentById(id);
        return ResponseEntity.ok(rent);
    }

    @GetMapping("/creator/{creatorId}")
    @Operation(summary = "사용자별 예약 조회", description = "특정 사용자가 생성한 예약들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class)))
    })
    public ResponseEntity<List<RentResponse>> getRentsByCreator(
            @Parameter(description = "예약 생성자 ID", example = "1")
            @PathVariable Long creatorId) {
        List<RentResponse> rents = rentService.getRentsByCreatorId(creatorId);
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/date-range")
    @Operation(summary = "날짜 범위별 예약 조회", description = "특정 날짜 범위의 예약들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class)))
    })
    public ResponseEntity<List<RentResponse>> getRentsByDateRange(
            @Parameter(description = "시작 날짜", example = "2024-01-15T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 날짜", example = "2024-01-20T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<RentResponse> rents = rentService.getRentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/room/{roomId}")
    @Operation(summary = "회의실별 예약 조회", description = "특정 회의실의 예약들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class)))
    })
    public ResponseEntity<List<RentResponse>> getRentsByRoom(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long roomId) {
        List<RentResponse> rents = rentService.getRentsByRoomId(roomId);
        return ResponseEntity.ok(rents);
    }

    @PostMapping
    @Operation(summary = "예약 생성", description = "새로운 회의실 예약을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "시간 충돌")
    })
    public ResponseEntity<RentResponse> createRent(
            @Parameter(description = "예약 생성 정보")
            @Valid @RequestBody CreateRentRequest request) {
        RentResponse rent = rentService.createRent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "예약 수정", description = "기존 예약 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = RentResponse.class))),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "시간 충돌")
    })
    public ResponseEntity<RentResponse> updateRent(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "예약 수정 정보")
            @Valid @RequestBody CreateRentRequest request) {
        RentResponse rent = rentService.updateRent(id, request);
        return ResponseEntity.ok(rent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "예약 삭제", description = "예약을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteRent(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long id) {
        rentService.deleteRent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/conflicts")
    @Operation(summary = "시간 충돌 확인", description = "특정 시간대의 예약 충돌을 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "확인 완료",
            content = @Content(schema = @Schema(implementation = RentResponse.class)))
    })
    public ResponseEntity<Boolean> checkTimeConflict(
            @Parameter(description = "회의실 ID 목록")
            @RequestParam List<Long> roomIds,
            @Parameter(description = "시작 시간", example = "2024-01-15T14:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "종료 시간", example = "2024-01-15T16:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean hasConflict = rentService.hasTimeConflict(roomIds, startTime, endTime);
        return ResponseEntity.ok(hasConflict);
    }
} 
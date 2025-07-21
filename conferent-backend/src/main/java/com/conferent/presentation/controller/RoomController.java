package com.conferent.presentation.controller;

import com.conferent.business.entity.Room;
import com.conferent.business.service.RoomService;
import com.conferent.presentation.dto.CreateRoomRequest;
import com.conferent.presentation.dto.RoomResponse;
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
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
@Tag(name = "Room API", description = "회의실 관리 API")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    @GetMapping
    @Operation(summary = "회의실 목록 조회", description = "모든 회의실 또는 최소 수용인원으로 필터링된 회의실 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                     content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<List<RoomResponse>> getRooms(
            @Parameter(description = "최소 수용인원 (선택사항)", example = "10")
            @RequestParam(required = false) Integer minCapacity) {
        try {
            List<Room> rooms;
            if (minCapacity != null) {
                rooms = roomService.getRoomsByMinCapacity(minCapacity);
            } else {
                rooms = roomService.getAllRooms();
            }
            
            List<RoomResponse> responses = rooms.stream()
                .map(RoomResponse::fromDomainEntity)
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    @Operation(summary = "회의실 생성", description = "새로운 회의실을 생성합니다. (관리자 권한 필요)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공", 
                     content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    public ResponseEntity<RoomResponse> createRoom(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestHeader("User-Id") Long userId,
            @Parameter(description = "회의실 생성 정보", required = true)
            @Valid @RequestBody CreateRoomRequest request) {
        try {
            Room room = roomService.createRoom(
                userId,
                request.getName(),
                request.getLocation(),
                request.getCapacity(),
                request.getDescription()
            );
            
            RoomResponse response = RoomResponse.fromDomainEntity(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    @GetMapping("/{roomId}")
    @Operation(summary = "회의실 상세 조회", description = "특정 회의실의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
                     content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "404", description = "회의실을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<RoomResponse> getRoomById(
            @Parameter(description = "회의실 ID", required = true, example = "1")
            @PathVariable Long roomId) {
        try {
            Room room = roomService.getRoomById(roomId);
            RoomResponse response = RoomResponse.fromDomainEntity(room);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 
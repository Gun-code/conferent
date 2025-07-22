package com.conferent.controllers.room;

import com.conferent.dtos.room.RoomRequest;
import com.conferent.dtos.room.RoomResponse;
import com.conferent.services.room.RoomService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "회의실 관리", description = "회의실 CRUD API")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @Operation(summary = "회의실 목록 조회", description = "모든 회의실 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "회의실 상세 조회", description = "특정 회의실의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "404", description = "회의실을 찾을 수 없음")
    })
    public ResponseEntity<RoomResponse> getRoomById(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long id) {
        RoomResponse room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/search")
    @Operation(summary = "회의실 검색", description = "이름으로 회의실을 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공",
            content = @Content(schema = @Schema(implementation = RoomResponse.class)))
    })
    public ResponseEntity<List<RoomResponse>> searchRooms(
            @Parameter(description = "검색할 회의실 이름", example = "대회의실")
            @RequestParam String name) {
        List<RoomResponse> rooms = roomService.searchRoomsByName(name);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    @Operation(summary = "회의실 생성", description = "새로운 회의실을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<RoomResponse> createRoom(
            @Parameter(description = "회의실 생성 정보")
            @Valid @RequestBody RoomRequest request) {
        RoomResponse room = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @PutMapping("/{id}")
    @Operation(summary = "회의실 수정", description = "기존 회의실 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = RoomResponse.class))),
        @ApiResponse(responseCode = "404", description = "회의실을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<RoomResponse> updateRoom(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "회의실 수정 정보")
            @Valid @RequestBody RoomRequest request) {
        RoomResponse room = roomService.updateRoom(id, request);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회의실 삭제", description = "회의실을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회의실을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteRoom(
            @Parameter(description = "회의실 ID", example = "1")
            @PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
} 
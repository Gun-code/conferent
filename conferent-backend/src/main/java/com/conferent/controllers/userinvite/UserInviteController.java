package com.conferent.controllers.userinvite;

import com.conferent.dtos.userinvite.UserInviteRequest;
import com.conferent.dtos.userinvite.UserInviteResponse;
import com.conferent.dtos.userinvite.UpdateInviteStatusRequest;
import com.conferent.entities.UserInvite;
import com.conferent.enums.InviteStatus;
import com.conferent.services.userinvite.UserInviteService;
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

import java.util.List;

@RestController
@RequestMapping("/api/user-invites")
@RequiredArgsConstructor
@Tag(name = "사용자 초대 관리", description = "사용자 초대 관리 API")
public class UserInviteController {

    private final UserInviteService userInviteService;

    @GetMapping
    @Operation(summary = "사용자 초대 목록 조회", description = "모든 사용자 초대를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInviteResponse.class)))
    })
    public ResponseEntity<List<UserInviteResponse>> getAllUserInvites() {
        List<UserInviteResponse> userInvites = userInviteService.getAllUserInvites().stream()
                .map(UserInviteResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(userInvites);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 초대 상세 조회", description = "특정 사용자 초대를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInviteResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자 초대를 찾을 수 없음")
    })
    public ResponseEntity<UserInviteResponse> getUserInviteById(
            @Parameter(description = "사용자 초대 ID", example = "1")
            @PathVariable Long id) {
        UserInviteResponse userInvite = UserInviteResponse.from(userInviteService.getUserInviteById(id));
        return ResponseEntity.ok(userInvite);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 초대 조회", description = "특정 사용자의 초대 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInviteResponse.class)))
    })
    public ResponseEntity<List<UserInviteResponse>> getUserInvitesByUserId(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId) {
        List<UserInviteResponse> userInvites = userInviteService.getUserInvitesByUserId(userId).stream()
                .map(UserInviteResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(userInvites);
    }

    @GetMapping("/rent/{rentId}")
    @Operation(summary = "예약별 초대 조회", description = "특정 예약의 초대 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInviteResponse.class)))
    })
    public ResponseEntity<List<UserInviteResponse>> getUserInvitesByRentId(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long rentId) {
        List<UserInviteResponse> userInvites = userInviteService.getUserInvitesByRentId(rentId).stream()
                .map(UserInviteResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(userInvites);
    }

    @GetMapping("/user/{userId}/status/{status}")
    @Operation(summary = "사용자별 상태별 초대 조회", description = "특정 사용자의 특정 상태 초대를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInviteResponse.class)))
    })
    public ResponseEntity<List<UserInviteResponse>> getUserInvitesByUserIdAndStatus(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "초대 상태", example = "PENDING")
            @PathVariable InviteStatus status) {
        List<UserInviteResponse> userInvites = userInviteService.getUserInvitesByUserIdAndStatus(userId, status).stream()
                .map(UserInviteResponse::from)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(userInvites);
    }

    @PostMapping
    @Operation(summary = "사용자 초대 생성", description = "새로운 사용자 초대를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = UserInvite.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 RoomRent를 찾을 수 없음")
    })
    public ResponseEntity<UserInvite> createUserInvite(
            @Parameter(description = "사용자 ID", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "RoomRent ID", example = "1")
            @RequestParam Long roomRentId) {
        UserInvite userInvite = userInviteService.createUserInvite(userId, roomRentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userInvite);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "초대 상태 업데이트", description = "사용자 초대 상태를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업데이트 성공",
            content = @Content(schema = @Schema(implementation = UserInvite.class))),
        @ApiResponse(responseCode = "404", description = "사용자 초대를 찾을 수 없음")
    })
    public ResponseEntity<UserInvite> updateInviteStatus(
            @Parameter(description = "사용자 초대 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "새로운 상태", example = "ACCEPTED")
            @RequestParam InviteStatus status) {
        UserInvite userInvite = userInviteService.updateInviteStatus(id, status);
        return ResponseEntity.ok(userInvite);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 초대 삭제", description = "사용자 초대를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자 초대를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteUserInvite(
            @Parameter(description = "사용자 초대 ID", example = "1")
            @PathVariable Long id) {
        userInviteService.deleteUserInvite(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = "사용자별 초대 삭제", description = "특정 사용자의 모든 초대를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteByUserId(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId) {
        userInviteService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/room-rent/{roomRentId}")
    @Operation(summary = "RoomRent별 초대 삭제", description = "특정 RoomRent의 모든 초대를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteByRoomRentId(
            @Parameter(description = "RoomRent ID", example = "1")
            @PathVariable Long roomRentId) {
        userInviteService.deleteByRoomRentId(roomRentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    @Operation(summary = "초대 존재 여부 확인", description = "특정 사용자와 RoomRent의 초대가 존재하는지 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "확인 완료")
    })
    public ResponseEntity<Boolean> existsByUserIdAndRoomRentId(
            @Parameter(description = "사용자 ID", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "RoomRent ID", example = "1")
            @RequestParam Long roomRentId) {
        boolean exists = userInviteService.existsByUserIdAndRoomRentId(userId, roomRentId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/find")
    @Operation(summary = "초대 조회", description = "특정 사용자와 RoomRent의 초대를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserInvite.class))),
        @ApiResponse(responseCode = "404", description = "초대를 찾을 수 없음")
    })
    public ResponseEntity<UserInvite> findByUserIdAndRoomRentId(
            @Parameter(description = "사용자 ID", example = "1")
            @RequestParam Long userId,
            @Parameter(description = "RoomRent ID", example = "1")
            @RequestParam Long roomRentId) {
        UserInvite userInvite = userInviteService.findByUserIdAndRoomRentId(userId, roomRentId);
        return ResponseEntity.ok(userInvite);
    }

    @GetMapping("/user/{userId}/pending-count")
    @Operation(summary = "대기 중인 초대 개수 조회", description = "특정 사용자의 대기 중인 초대 개수를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Long> countPendingInvitesByUserId(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId) {
        long count = userInviteService.countPendingInvitesByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/rent/{rentId}/accepted-count")
    @Operation(summary = "수락된 초대 개수 조회", description = "특정 예약의 수락된 초대 개수를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<Long> countAcceptedInvitesForRent(
            @Parameter(description = "예약 ID", example = "1")
            @PathVariable Long rentId) {
        long count = userInviteService.countAcceptedInvitesForRent(rentId);
        return ResponseEntity.ok(count);
    }
} 
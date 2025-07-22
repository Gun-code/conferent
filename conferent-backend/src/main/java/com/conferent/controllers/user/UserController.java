package com.conferent.controllers.user;

import com.conferent.dtos.user.UserRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.enums.Role;
import com.conferent.services.user.UserService;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리", description = "사용자 CRUD API")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "사용자 목록 조회", description = "모든 사용자 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "이름으로 사용자를 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<List<UserResponse>> searchUsers(
            @Parameter(description = "검색할 사용자 이름", example = "홍길동")
            @RequestParam String name) {
        List<UserResponse> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "역할별 사용자 조회", description = "특정 역할의 사용자들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<List<UserResponse>> getUsersByRole(
            @Parameter(description = "사용자 역할", example = "USER")
            @PathVariable String role) {
        List<UserResponse> users = userService.findUsersByRole(Role.valueOf(role.toUpperCase()));
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "사용자 생성 정보")
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "기존 사용자 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "사용자 수정 정보")
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 
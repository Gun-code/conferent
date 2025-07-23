package com.conferent.dtos.auth;

import com.conferent.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 * 클라이언트에서 회원가입 시 전송하는 데이터 구조
 * 
 * 포함 정보:
 * - 사용자 이름
 * - 이메일 (중복 체크 필요)
 * - 비밀번호
 * - 사용자 역할
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청")
public class RegisterRequest {

    /**
     * 사용자 이름
     * - 필수 입력값
     * - 최대 100자
     */
    @NotBlank(message = "사용자 이름은 필수입니다")
    @Size(max = 100, message = "사용자 이름은 100자를 초과할 수 없습니다")
    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    private String name;

    /**
     * 사용자 이메일
     * - 필수 입력값
     * - 이메일 형식 검증
     * - 최대 100자
     * - 중복 체크 필요 (서비스 레이어에서 처리)
     */
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다")
    @Schema(description = "사용자 이메일", example = "hong@conferent.com", required = true)
    private String email;

    /**
     * 사용자 비밀번호
     * - 필수 입력값
     * - 최소 6자 이상
     * - 서비스 레이어에서 암호화 처리됨
     */
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    @Schema(description = "사용자 비밀번호", example = "user123", required = true)
    private String password;

    /**
     * 사용자 역할
     * - 필수 입력값
     * - USER 또는 ADMIN
     * - 일반적으로 회원가입 시에는 USER로 고정
     */
    @NotNull(message = "역할은 필수입니다")
    @Schema(description = "사용자 역할", example = "USER", required = true)
    private Role role;
} 
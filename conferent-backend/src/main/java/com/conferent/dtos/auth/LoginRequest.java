package com.conferent.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 * 클라이언트에서 로그인 시 전송하는 데이터 구조
 * 
 * 포함 정보:
 * - 이메일 (사용자 식별자)
 * - 비밀번호
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청")
public class LoginRequest {

    /**
     * 사용자 이메일
     * - 필수 입력값
     * - 이메일 형식 검증
     * - 최대 100자
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
     */
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    @Schema(description = "사용자 비밀번호", example = "user123", required = true)
    private String password;
} 
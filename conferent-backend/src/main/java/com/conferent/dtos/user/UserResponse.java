package com.conferent.dtos.user;

import com.conferent.entities.User;
import com.conferent.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 응답")
public class UserResponse {
    
    @Schema(description = "사용자 ID", example = "1")
    private Long id;
    
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;
    
    @Schema(description = "이메일", example = "hong@example.com")
    private String email;
    
    @Schema(description = "사용자 역할", example = "USER")
    private Role role;
    
    @Schema(description = "생성 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 시간", example = "2024-01-15T14:00:00")
    private LocalDateTime updatedAt;
    
    /**
     * Entity에서 DTO로 변환하는 팩토리 메서드 (비밀번호 제외)
     */
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    /**
     * DTO에서 Entity로 변환하는 메서드 (비밀번호는 별도 설정 필요)
     */
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setRole(this.role);
        user.setCreatedAt(this.createdAt);
        user.setUpdatedAt(this.updatedAt);
        return user;
    }
} 
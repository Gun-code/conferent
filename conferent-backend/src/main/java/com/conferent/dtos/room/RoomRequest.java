package com.conferent.dtos.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회의실 생성/수정 요청")
public class RoomRequest {
    
    @Schema(description = "회의실 이름", example = "대회의실")
    @NotBlank(message = "회의실 이름은 필수입니다")
    @Size(max = 100, message = "회의실 이름은 100자를 초과할 수 없습니다")
    private String name;
    
    @Schema(description = "회의실 위치", example = "본관 3층")
    @Size(max = 200, message = "위치는 200자를 초과할 수 없습니다")
    private String location;
    
    @Schema(description = "수용 인원", example = "20")
    @NotNull(message = "수용 인원은 필수입니다")
    @Min(value = 1, message = "수용 인원은 1명 이상이어야 합니다")
    private Integer capacity;
    
    @Schema(description = "회의실 설명", example = "프레젠테이션 가능한 대형 회의실")
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다")
    private String description;
} 
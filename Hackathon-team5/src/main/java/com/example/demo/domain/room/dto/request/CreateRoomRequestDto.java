package com.example.demo.domain.room.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방 생성 요청")
public class CreateRoomRequestDto {

    @Schema(description = "방 제목", example = "홍대 도둑을 찾아라")
    @NotBlank(message = "방 제목은 필수입니다.")
    private String title;

    @Schema(description = "장소 이름", example = "홍대입구역 9번 출구")
    @NotBlank(message = "장소 이름은 필수입니다.")
    private String placeName;

    @Schema(description = "위도", example = "37.5575")
    @NotNull(message = "위도는 필수입니다.")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다.")
    private BigDecimal lat;

    @Schema(description = "경도", example = "126.9244")
    @NotNull(message = "경도는 필수입니다.")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다.")
    private BigDecimal lng;

    @Schema(description = "만남 시간", example = "2026-01-10T18:00:00")
    @NotNull(message = "만남 시간은 필수입니다.")
    @Future(message = "만남 시간은 현재 시간 이후여야 합니다.")
    private LocalDateTime meetingTime;

    @Schema(description = "최대 참가자 수", example = "12")
    @NotNull(message = "최대 참가자 수는 필수입니다.")
    @Min(value = 2, message = "최소 2명 이상이어야 합니다.")
    @Max(value = 20, message = "최대 20명까지 가능합니다.")
    private Integer maxParticipants;
}

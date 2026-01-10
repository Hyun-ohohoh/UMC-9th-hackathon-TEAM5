package com.example.demo.domain.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주변 방 조회 응답")
public class NearbyRoomResponseDto {

    @Schema(description = "방 ID", example = "1")
    private Long roomId;

    @Schema(description = "방 제목", example = "홍대 도둑을 찾아라")
    private String title;

    @Schema(description = "장소 이름", example = "홍대입구역 9번 출구")
    private String placeName;

    @Schema(description = "위도", example = "37.5575")
    private BigDecimal lat;

    @Schema(description = "경도", example = "126.9244")
    private BigDecimal lng;

    @Schema(description = "만남 시간", example = "2026-01-10T18:00:00")
    private LocalDateTime meetingTime;

    @Schema(description = "현재 참가자 수", example = "5")
    private Integer currentParticipants;

    @Schema(description = "최대 참가자 수", example = "12")
    private Integer maxParticipants;

    @Schema(description = "사용자로부터의 거리(km)", example = "1.2")
    private Double distance;

    @Schema(description = "방 상태 (WAITING, STARTING, PLAYING, FINISHED)", example = "WAITING")
    private String status;
}

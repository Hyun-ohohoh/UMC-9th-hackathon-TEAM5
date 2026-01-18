package com.example.demo.domain.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방 세부정보 조회 응답")
public class RoomDetailResponseDto {

    @Schema(description = "방 ID", example = "1")
    private Long roomId;

    @Schema(description = "방 제목", example = "홍대 도둑을 찾아라")
    private String title;

    @Schema(description = "장소 이름", example = "홍대입구역 9번 출구")
    private String placeName;

    @Schema(description = "만남 시간", example = "2026-01-10T18:00:00")
    private LocalDateTime meetingTime;

    @Schema(description = "방 상태 (WAITING, PLAYING, FINISHED)", example = "WAITING")
    private String status;

    @Schema(description = "게임 카운트다운(초)", example = "1800")
    private Integer countdownSeconds;

    @Schema(description = "도망가는 시간(초)", example = "60")
    private Integer escapeTime;

    @Schema(description = "방 설명", example = "홍대에서 도둑잡기 게임합니다!")
    private String description;

    @Schema(description = "정원 정보")
    private CapacityInfo capacity;

    @Schema(description = "참가자 목록")
    private List<ParticipantInfo> participants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정원 정보")
    public static class CapacityInfo {
        @Schema(description = "현재 참가자 수", example = "5")
        private Integer current;

        @Schema(description = "최대 참가자 수", example = "10")
        private Integer total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "참가자 정보")
    public static class ParticipantInfo {
        @Schema(description = "사용자 ID", example = "2")
        private Long userId;

        @Schema(description = "닉네임", example = "참가자1")
        private String nickname;
    }
}

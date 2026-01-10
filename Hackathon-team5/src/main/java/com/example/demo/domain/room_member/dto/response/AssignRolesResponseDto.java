package com.example.demo.domain.room_member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀 배정 응답")
public class AssignRolesResponseDto {

    @Schema(description = "방 ID", example = "101")
    private Long roomId;

    @Schema(description = "팀 통계")
    private TeamStats stats;

    @Schema(description = "참가자 목록")
    private List<ParticipantInfo> participants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 통계")
    public static class TeamStats {

        @Schema(description = "경찰 총 인원", example = "1")
        private Integer totalPolice;

        @Schema(description = "도둑 총 인원", example = "3")
        private Integer totalThieves;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "참가자 정보")
    public static class ParticipantInfo {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "닉네임", example = "경찰사자")
        private String nickname;

        @Schema(description = "역할 (POLICE, THIEF)", example = "POLICE")
        private String role;

        @Schema(description = "도착 여부", example = "true")
        private Boolean isArrived;
    }
}

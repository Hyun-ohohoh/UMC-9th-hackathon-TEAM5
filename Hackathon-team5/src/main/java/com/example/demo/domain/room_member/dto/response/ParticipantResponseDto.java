package com.example.demo.domain.room_member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "참가자 정보 응답")
public class ParticipantResponseDto {

    @Schema(description = "방 ID", example = "101")
    private Long roomId;

    @Schema(description = "참가자 목록")
    private List<AssignRolesResponseDto.ParticipantInfo> participants;

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

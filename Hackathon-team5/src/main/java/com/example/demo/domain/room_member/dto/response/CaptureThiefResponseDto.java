package com.example.demo.domain.room_member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "도둑 검거 응답")
public class CaptureThiefResponseDto {

    @Schema(description = "검거당한 도둑 사용자 ID", example = "2")
    private Long thiefUserId;

    @Schema(description = "검거당한 도둑 닉네임", example = "도둑라이언")
    private String thiefNickname;

    @Schema(description = "검거한 경찰 사용자 ID", example = "1")
    private Long policeUserId;

    @Schema(description = "검거한 경찰 닉네임", example = "경찰라이언")
    private String policeNickname;

    @Schema(description = "남은 도둑 수", example = "5")
    private Integer remainingThieves;

    @Schema(description = "메시지", example = "도둑을 검거했습니다!")
    private String message;
}

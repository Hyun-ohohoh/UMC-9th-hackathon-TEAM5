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
@Schema(description = "탈옥 성공 응답")
public class ReleaseThiefResponseDto {

    @Schema(description = "탈옥한 도둑 사용자 ID", example = "2")
    private Long thiefUserId;

    @Schema(description = "탈옥한 도둑 닉네임", example = "도둑라이언")
    private String thiefNickname;

    @Schema(description = "남은 도둑 수", example = "6")
    private Integer remainingThieves;

    @Schema(description = "메시지", example = "탈옥에 성공했습니다!")
    private String message;
}

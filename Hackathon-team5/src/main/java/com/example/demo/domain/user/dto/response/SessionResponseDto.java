package com.example.demo.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 응답")
public class SessionResponseDto {

   @Schema(description = "사용자 고유 ID", example = "1")
   private Long userId;

   @Schema(description = "닉네임", example = "나호")
   private String nickname;

   @Schema(description = "JWT 액세스 토큰")
   private String accessToken;

   @Schema(description = "토큰 타입", example = "Bearer")
   private String tokenType;
}

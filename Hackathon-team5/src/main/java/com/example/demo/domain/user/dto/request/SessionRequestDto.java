package com.example.demo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청")
public class SessionRequestDto {
    @Schema(description = "닉네임")
    @NotBlank
    private String nickname;

    @Schema(description = "비밀번호")
    @NotBlank
    private String password;

    @Schema(description = "위도")
    @NotNull(message = "위도는 필수 입력값입니다.")
    private BigDecimal lat;

    @Schema(description = "경도")
    @NotNull(message = "경도는 필수 입력값입니다.")
    private BigDecimal lng;
}

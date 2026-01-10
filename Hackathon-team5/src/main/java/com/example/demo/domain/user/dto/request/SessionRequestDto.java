package com.example.demo.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank

    private BigDecimal lat;
    @Schema(description = "경도")
    @NotBlank
    private BigDecimal lng;
}

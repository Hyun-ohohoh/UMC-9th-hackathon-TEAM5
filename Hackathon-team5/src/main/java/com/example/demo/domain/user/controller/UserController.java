package com.example.demo.domain.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.user.dto.request.SessionRequestDto;
import com.example.demo.domain.user.dto.response.SessionResponseDto;
import com.example.demo.domain.user.service.AuthService;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final AuthService authService;

    @PostMapping("/session")
    @Operation(summary = "로그인 및 세션 생성", description = "닉네임과 4자리 비밀번호로 로그인(또는 자동 가입)")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.USER_NOT_FOUND,
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.INVALID_PASSWORD,
            ErrorCode.INTERNAL_SERVER_ERROR
    })
    public ApiResponse<SessionResponseDto> createSession(
            @Valid @RequestBody SessionRequestDto request) {

        SessionResponseDto response = authService.login(request);
        return ApiResponse.success("로그인 성공", response);
    }
}

package com.example.demo.domain.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.user.dto.request.SessionRequestDto;
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

    @PostMapping("/session")
    @Operation(summary = "로그인 및 세션 생성")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.USER_NOT_FOUND,
            ErrorCode.INVALID_INPUT_VALUE
    })
    public ApiResponse<String> createSession(
            @Valid @RequestBody SessionRequestDto request) {
        // TODO: 구현 필요
        return ApiResponse.success("더미");
    }
}

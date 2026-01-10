package com.example.demo.domain.room_game.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room_game.dto.response.GameStatusResponseDto;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms/{roomId}/game")
@RequiredArgsConstructor
@Tag(name = "Room Game", description = "게임 상태 관련 API")
public class RoomGameController {

    @GetMapping("/status")
    @Operation(summary = "게임 현황 및 시간 동기화", description = "G1,2")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND
    })
    public ApiResponse<GameStatusResponseDto> getGameStatus(
            @Parameter(description = "방 ID") @PathVariable Long roomId) {
        // TODO: 구현 필요
        return ApiResponse.success(new GameStatusResponseDto());
    }
}

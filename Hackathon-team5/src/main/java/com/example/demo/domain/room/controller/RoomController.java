package com.example.demo.domain.room.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room.dto.request.CreateRoomRequestDto;
import com.example.demo.domain.room.dto.response.CreateRoomResponseDto;
import com.example.demo.domain.room.dto.response.NearbyRoomsResponseDto;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Room", description = "방 관련 API")
public class RoomController {

    @GetMapping("/nearby")
    @Operation(summary = "주변 방 조회", description = "사용자 위치 기반으로 주변 방 목록 조회")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<NearbyRoomsResponseDto> getNearbyRooms(
            @Parameter(description = "사용자 ID") @RequestParam Long userId) {
        // TODO: userId로 users 테이블에서 위도/경도 조회 후 계산
        return ApiResponse.success(new NearbyRoomsResponseDto());
    }

    @PostMapping
    @Operation(summary = "방 생성")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<CreateRoomResponseDto> createRoom(
            @Valid @RequestBody CreateRoomRequestDto request) {
        // TODO: 구현 필요
        return ApiResponse.success(new CreateRoomResponseDto());
    }
}

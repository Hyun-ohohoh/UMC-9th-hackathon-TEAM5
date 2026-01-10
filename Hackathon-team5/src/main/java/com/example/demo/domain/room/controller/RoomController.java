package com.example.demo.domain.room.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room.dto.request.CreateRoomRequestDto;
import com.example.demo.domain.room.dto.response.CreateRoomResponseDto;
import com.example.demo.domain.room.dto.response.NearbyRoomsResponseDto;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.service.RoomService;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
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
    private final RoomService roomService;
    private final UserService userService;

    @GetMapping("/nearby")
    @Operation(summary = "주변 방 조회", description = "사용자 위치 기반으로 주변 방 목록 조회")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<NearbyRoomsResponseDto> getNearbyRooms() {

        NearbyRoomsResponseDto response = roomService.getRoomsForUser(1L);

        return ApiResponse.success(response);
    }

    @PostMapping
    @Operation(summary = "방 생성")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.INVALID_INPUT_VALUE,
            ErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<CreateRoomResponseDto> createRoom(
            @Valid @RequestBody CreateRoomRequestDto request) {
        CreateRoomResponseDto responseDto = roomService.createRoom(request, 1L);

        return ApiResponse.success(responseDto);
    }
}

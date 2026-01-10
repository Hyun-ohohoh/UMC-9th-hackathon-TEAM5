package com.example.demo.domain.room_member.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room_member.dto.request.AssignRolesRequestDto;
import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.dto.response.PhotoUploadResponseDto;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rooms/{roomId}")
@RequiredArgsConstructor
@Tag(name = "Room Member", description = "방 참가자 관련 API")
public class RoomMemberController {

    @PatchMapping("/roles")
    @Operation(summary = "팀 배정", description = "방장 전용 - 마지막에 한번에 보내는 플로우로 생각해봤습니다")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.FORBIDDEN
    })
    public ApiResponse<AssignRolesResponseDto> assignRoles(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Valid @RequestBody AssignRolesRequestDto request) {
        // TODO: 구현 필요
        return ApiResponse.success(new AssignRolesResponseDto());
    }

    @GetMapping("/participants")
    @Operation(summary = "도착 여부 조회")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND
    })
    public ApiResponse<List<ParticipantResponseDto>> getParticipants(
            @Parameter(description = "방 ID") @PathVariable Long roomId) {
        // TODO: 구현 필요
        return ApiResponse.success(List.of());
    }

    @PatchMapping("/participants/{userId}/arrival")
    @Operation(summary = "도착으로 변경", description = "방장 전용")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.FORBIDDEN
    })
    public ApiResponse<Void> updateArrival(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        // TODO: 구현 필요
        return ApiResponse.success(null);
    }

    @PostMapping("/photo")
    @Operation(summary = "사진 업로드")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.INVALID_INPUT_VALUE
    })
    public ApiResponse<PhotoUploadResponseDto> uploadPhoto(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "사진 파일") @RequestPart MultipartFile photo) {
        // TODO: 구현 필요
        return ApiResponse.success(new PhotoUploadResponseDto());
    }

    @PatchMapping("/participants/{userId}/capture")
    @Operation(summary = "도독 검거")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND
    })
    public ApiResponse<Void> captureThief(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "사용자 ID") @PathVariable Long userId) {
        // TODO: 구현 필요
        return ApiResponse.success(null);
    }

    @PatchMapping("/participants/{userId}/release")
    @Operation(summary = "탈옥", description = "도독이 본인것만")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.FORBIDDEN
    })
    public ApiResponse<Void> releaseThief(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "사용자 ID (본인)") @PathVariable Long userId) {
        // TODO: 구현 필요
        return ApiResponse.success(null);
    }
}

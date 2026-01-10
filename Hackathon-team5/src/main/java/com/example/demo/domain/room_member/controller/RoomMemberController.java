package com.example.demo.domain.room_member.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room_member.dto.request.AssignRolesRequestDto;
import com.example.demo.domain.room_member.dto.request.JoinRoomRequestDto;
import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.JoinRoomResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.dto.response.PhotoUploadResponseDto;
import com.example.demo.domain.room_member.service.RoomMemberService;
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
    private final RoomMemberService roomMemberService;

    @PatchMapping("/roles")
    @Operation(summary = "팀 배정 및 게임 시작", description = "방장 전용")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ONLY_HOST_ALLOWED,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS,
            ErrorCode.INVALID_INPUT_VALUE
    })
    public ApiResponse<AssignRolesResponseDto> assignRolesAndStartGame(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Valid @RequestBody AssignRolesRequestDto request) {
        // TODO: 실제 인증 시스템 연동 후 hostUserId를 실제 인증된 사용자 ID로 변경
        Long hostUserId = 1L; // 임시로 1L 사용 (인증 구현 후 수정 필요)

        AssignRolesResponseDto response = roomMemberService.assignRolesAndStartGame(roomId, request, hostUserId);
        return ApiResponse.success(response);
    }

    @PostMapping("/join")
    @Operation(summary = "방 참가", description = "방에 참가합니다. 역할군(경찰/도둑/랜덤)을 선택할 수 있습니다.")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS,
            ErrorCode.ALREADY_JOINED_ROOM,
            ErrorCode.ROOM_FULL,
            ErrorCode.ROLE_CAPACITY_FULL,
            ErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<JoinRoomResponseDto> joinRoom(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Valid @RequestBody JoinRoomRequestDto request) {
        // TODO: 실제 인증 시스템 연동 후 userId를 실제 인증된 사용자 ID로 변경
        Long userId = 2L; // 임시로 2L 사용 (인증 구현 후 수정 필요)

        JoinRoomResponseDto response = roomMemberService.joinRoom(roomId, userId, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/participants")
    @Operation(summary = "도착 여부 조회")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND
    })
    public ApiResponse<ParticipantResponseDto> getParticipants(
            @Parameter(description = "방 ID") @PathVariable Long roomId) {
        // TODO: 구현 필요
        ParticipantResponseDto response = roomMemberService.getParticipantsStatus(roomId);

        return ApiResponse.success(response);
    }

    @PatchMapping("/participants/{userId}/arrival")
    @Operation(summary = "도착으로 변경", description = "방장 전용 - 대기방에 있는 참여자의 도착 여부를 변경합니다.")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_MEMBER_NOT_FOUND,
            ErrorCode.ONLY_HOST_ALLOWED,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS
    })
    public ApiResponse<Void> updateArrival(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "도착으로 변경할 참가자의 사용자 ID") @PathVariable Long userId) {
        // TODO: 실제 인증 시스템 연동 후 hostUserId를 실제 인증된 사용자 ID로 변경
        Long hostUserId = 1L; // 임시로 1L 사용 (인증 구현 후 수정 필요)
        
        roomMemberService.markAsArrived(roomId, userId, hostUserId);
        
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
    @Operation(summary = "도둑 검거")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_MEMBER_NOT_FOUND,
            ErrorCode.FORBIDDEN,
            ErrorCode.INVALID_INPUT_VALUE    })
    public ApiResponse<?> captureThief(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "검거한 도둑의 ID") @PathVariable Long userId) {
        // TODO: 구현 필요
        Long currentPoliceId = 1L;

        // 1. 검거 로직 실행
        roomMemberService.captureThief(roomId, userId, currentPoliceId);


        return ApiResponse.success("도둑을 검거했습니다!");
    }

    @PatchMapping("/participants/{userId}/release")
    @Operation(summary = "탈옥", description = "도둑이 본인것만")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.FORBIDDEN,
            ErrorCode.GAME_NOT_STARTED,
            ErrorCode.NOT_A_THIEF,
            ErrorCode.NOT_IN_JAIL
    })
    public ApiResponse<Void> releaseThief(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "사용자 ID (본인)") @PathVariable Long userId) {

        roomMemberService.releaseThief(roomId, userId);
        return ApiResponse.success(null);
    }
}

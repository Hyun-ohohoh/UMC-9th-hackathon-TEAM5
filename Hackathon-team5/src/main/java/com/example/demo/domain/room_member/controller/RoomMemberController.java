package com.example.demo.domain.room_member.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_member.dto.request.AssignRolesRequestDto;
import com.example.demo.domain.room_member.dto.request.JoinRoomRequestDto;
import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.CaptureThiefResponseDto;
import com.example.demo.domain.room_member.dto.response.JoinRoomResponseDto;
import com.example.demo.domain.room_member.dto.response.LeaveRoomResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.dto.response.PhotoUploadResponseDto;
import com.example.demo.domain.room_member.dto.response.ReleaseThiefResponseDto;
import com.example.demo.domain.room_member.service.RoomMemberService;
import com.example.demo.global.config.SwaggerConfig;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.infra.S3Service;
import com.example.demo.global.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/rooms/{roomId}")
@RequiredArgsConstructor
@Tag(name = "Room Member", description = "방 참가자 관련 API")
public class RoomMemberController {
    private final RoomMemberService roomMemberService;
    private final S3Service s3Service;
    private final RoomRepository roomRepository;

    @PatchMapping("/roles")
    @Operation(summary = "팀 배정 및 게임 시작", description = "방장 전용")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ONLY_HOST_ALLOWED,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS,
            ErrorCode.INVALID_INPUT_VALUE
    })
    public ApiResponse<AssignRolesResponseDto> assignRolesAndStartGame(
            @Parameter(hidden = true)@AuthUser Long hostUserId,
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Valid @RequestBody AssignRolesRequestDto request) {

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
            @Parameter(hidden = true)@AuthUser Long userId,
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Valid @RequestBody JoinRoomRequestDto request) {

        JoinRoomResponseDto response = roomMemberService.joinRoom(roomId, userId, request);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/leave")
    @Operation(summary = "방 나가기", description = "방에서 나갑니다. 방장이 나가면 다른 참가자에게 방장이 위임됩니다. 참가자가 없으면 방이 삭제됩니다.")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS,
            ErrorCode.NOT_JOINED_ROOM
    })
    public ApiResponse<LeaveRoomResponseDto> leaveRoom(
            @Parameter(hidden = true) @AuthUser Long userId,
            @Parameter(description = "방 ID") @PathVariable Long roomId) {

        LeaveRoomResponseDto response = roomMemberService.leaveRoom(roomId, userId);
        return ApiResponse.success(response);
    }

    @GetMapping("/participants")
    @Operation(summary = "도착 여부 조회")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND
    })
    public ApiResponse<ParticipantResponseDto> getParticipants(
            @Parameter(description = "방 ID") @PathVariable Long roomId) {
        ParticipantResponseDto response = roomMemberService.getParticipantsStatus(roomId);

        return ApiResponse.success(response);
    }

    @PatchMapping("/participants/{targetUserId}/arrival")
    @Operation(summary = "도착으로 변경", description = "방장 전용 - 대기방에 있는 참여자의 도착 여부를 변경합니다.")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_MEMBER_NOT_FOUND,
            ErrorCode.ONLY_HOST_ALLOWED,
            ErrorCode.ROOM_NOT_IN_WAITING_STATUS
    })
    public ApiResponse<Void> updateArrival(
            @Parameter(hidden = true)@AuthUser Long userId,
            @Parameter(description = "바꾸려는 user ID") @PathVariable  Long targetUserId,
            @Parameter(description = "방 ID") @PathVariable Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        Long hostUserId = room.getHost().getId();

        if(!userId.equals(hostUserId)){
            throw new BusinessException(ErrorCode.ONLY_HOST_ALLOWED);
        }

        roomMemberService.markAsArrived(roomId, targetUserId, hostUserId);

        return ApiResponse.success(null);
    }


    @PatchMapping("/participants/{thiefId}/capture")
    @Operation(summary = "도둑 검거")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.ROOM_NOT_FOUND,
            ErrorCode.ROOM_MEMBER_NOT_FOUND,
            ErrorCode.FORBIDDEN,
            ErrorCode.INVALID_INPUT_VALUE    })
    public ApiResponse<CaptureThiefResponseDto> captureThief(
            @Parameter(hidden = true)@AuthUser Long currentPoliceId,
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(description = "검거한 도둑의 ID") @PathVariable Long thiefId) {

        CaptureThiefResponseDto response = roomMemberService.captureThief(roomId, thiefId, currentPoliceId);

        return ApiResponse.success(response);
    }

    @PatchMapping("/participants/release")
    @Operation(summary = "탈옥", description = "도둑이 본인것만")
    @SwaggerConfig.ApiErrorExamples({
            ErrorCode.RESOURCE_NOT_FOUND,
            ErrorCode.FORBIDDEN,
            ErrorCode.GAME_NOT_STARTED,
            ErrorCode.NOT_A_THIEF,
            ErrorCode.NOT_IN_JAIL
    })
    public ApiResponse<ReleaseThiefResponseDto> releaseThief(
            @Parameter(description = "방 ID") @PathVariable Long roomId,
            @Parameter(hidden = true)@AuthUser Long userId) {

        ReleaseThiefResponseDto response = roomMemberService.releaseThief(roomId, userId);
        return ApiResponse.success(response);
    }
}

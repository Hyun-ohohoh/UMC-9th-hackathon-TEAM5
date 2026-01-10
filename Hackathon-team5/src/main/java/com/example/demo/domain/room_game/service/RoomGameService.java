package com.example.demo.domain.room_game.service;

import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_game.dto.response.GameStatusResponseDto;
import com.example.demo.domain.room_game.entity.RoomGameState;
import com.example.demo.domain.room_game.entity.enums.FinishReason;
import com.example.demo.domain.room_game.entity.enums.WinningTeam;
import com.example.demo.domain.room_game.repository.RoomGameStateRepository;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.entity.enums.Role;
import com.example.demo.domain.room_member.repository.RoomMemberRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.websocket.service.WebSocketMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomGameService {

    private final RoomGameStateRepository roomGameStateRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final WebSocketMessageService webSocketMessageService;

    @Transactional
    public GameStatusResponseDto finishGame(Long roomId, FinishReason finishReason, WinningTeam winningTeam) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 2. 방 상태 확인: PLAYING 상태일 때만 종료 가능
        if (room.getStatus() != RoomStatus.PLAYING) {
            throw new BusinessException(ErrorCode.ROOM_NOT_IN_PLAYING_STATUS);
        }

        // 3. RoomGameState 조회
        RoomGameState gameState = roomGameStateRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GAME_STATE_NOT_FOUND));

        // 4. 게임 종료 처리
        gameState.finishGame(finishReason, winningTeam);

        // 5. 방 상태를 FINISHED로 변경
        room.updateStatus(RoomStatus.FINISHED);

        // 6. 참가자 목록 조회
        List<RoomMember> members = roomMemberRepository.findAllByRoomIdWithUser(roomId);

        // 8. 참가자 정보 변환
        List<GameStatusResponseDto.GameParticipant> participants = members.stream()
                .map(member -> GameStatusResponseDto.GameParticipant.builder()
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .role(member.getRole() != null ? member.getRole().name() : null)
                        .isAlive(member.getThiefState())
                        .isArrived(member.getIsArrived())
                        .caughtCount(member.getCaughtCount())
                        .build())
                .collect(Collectors.toList());

        // 9. 응답 DTO 생성
        GameStatusResponseDto response = GameStatusResponseDto.builder()
                .startTime(gameState.getPlayingAt())
                .endTime(gameState.getFinishedAt())
                .participants(participants)
                .build();

        // 10. WebSocket으로 게임 종료 이벤트 전송
        webSocketMessageService.sendEventToRoom(roomId, "GAME_FINISHED", response);

        return response;
    }
    @Transactional
    public void saveState(RoomGameState roomGameState) {
        roomGameStateRepository.save(roomGameState);
    }
}

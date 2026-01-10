package com.example.demo.domain.room_member.service;

import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.converter.RoomMemberConverter;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.repository.RoomMemberRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomMemberConverter roomMemberConverter;
    private final RoomRepository roomRepository;

    @Transactional
    public void markAsArrived(Long roomId, Long targetUserId, Long hostUserId) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 2. 방 상태 확인: WAITING 상태일 때만 도착 확인 가능
        if (room.getStatus() != RoomStatus.WAITING) {
            throw new BusinessException(ErrorCode.ROOM_NOT_IN_WAITING_STATUS);
        }

        // 3. 권한 확인: 요청자가 방장인지 확인
        if (!room.getHost().getId().equals(hostUserId)) {
            throw new BusinessException(ErrorCode.ONLY_HOST_ALLOWED);
        }

        // 4. 대상 참가자 존재 확인
        RoomMember targetMember = roomMemberRepository.findByRoom_IdAndUser_Id(roomId, targetUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_MEMBER_NOT_FOUND));

        // 5. 도착 상태 업데이트
        targetMember.updateToArrived();
    }

    @Transactional(readOnly = true)
    public ParticipantResponseDto getParticipantsStatus(Long roomId) {
        // 1. 해당 방의 모든 참여자 DB에서 최신 상태로 가져옴
        List<RoomMember> members = roomMemberRepository.findAllByRoom_Id(roomId);

        // 2. 만약 참여자가 한 명도 없다면 방이 존재하지 않거나 비정상적인 접근
        if (members.isEmpty()) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }

        // 3. 컨버터를 통해 Entity 리스트 DTO로 변환해서 반환
        return roomMemberConverter.toParticipantResponseDto(roomId, members);
    }
}
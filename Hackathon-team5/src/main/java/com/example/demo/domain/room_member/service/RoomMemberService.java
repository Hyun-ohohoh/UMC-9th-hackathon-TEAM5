package com.example.demo.domain.room_member.service;

import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.repository.RoomMemberRepository;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
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
}
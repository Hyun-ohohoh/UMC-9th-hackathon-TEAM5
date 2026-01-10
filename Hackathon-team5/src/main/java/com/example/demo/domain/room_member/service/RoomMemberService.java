package com.example.demo.domain.room_member.service;

import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_member.converter.RoomMemberConverter;
import com.example.demo.domain.room_member.dto.request.AssignRolesRequestDto;
import com.example.demo.domain.room_member.dto.request.JoinRoomRequestDto;
import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.JoinRoomResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.entity.enums.JoinStatus;
import com.example.demo.domain.room_member.entity.enums.Role;
import com.example.demo.domain.room_member.entity.enums.RolePreference;
import com.example.demo.domain.room_member.repository.RoomMemberRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberConverter roomMemberConverter;
    private final UserService userService;

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

    @Transactional
    public JoinRoomResponseDto joinRoom(Long roomId, Long userId, JoinRoomRequestDto request) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 2. 방 상태 확인: WAITING 상태일 때만 참가 가능
        if (room.getStatus() != RoomStatus.WAITING) {
            throw new BusinessException(ErrorCode.ROOM_NOT_IN_WAITING_STATUS);
        }

        // 3. 이미 참가한 멤버인지 확인
        boolean alreadyJoined = roomMemberRepository.findByRoom_IdAndUser_Id(roomId, userId).isPresent();
        if (alreadyJoined) {
            throw new BusinessException(ErrorCode.ALREADY_JOINED_ROOM);
        }

        // 4. 유저 정보 조회
        User user = userService.getUserById(userId);

        // 5. 현재 참가자 목록 조회
        List<RoomMember> currentMembers = roomMemberRepository.findAllByRoomIdWithUser(roomId);

        // 6. 전체 정원 확인
        if (currentMembers.size() >= room.getCapacityTotal()) {
            throw new BusinessException(ErrorCode.ROOM_FULL);
        }

        // 7. 역할군별 정원 확인 및 역할 선호도 결정
        RolePreference finalRolePreference = determineRolePreference(
                request.getRolePreference(),
                currentMembers,
                room.getCapacityPolice(),
                room.getCapacityThief()
        );

        // 8. RoomMember 생성 및 저장
        RoomMember newMember = RoomMember.builder()
                .room(room)
                .user(user)
                .joinStatus(JoinStatus.JOINED)
                .rolePreference(finalRolePreference)
                .isArrived(false)
                .build();
        roomMemberRepository.save(newMember);

        System.out.println("방 참가 완료 - roomId: " + roomId + ", userId: " + userId + ", rolePreference: " + finalRolePreference);

        return JoinRoomResponseDto.builder()
                .roomId(roomId)
                .userId(userId)
                .rolePreference(finalRolePreference.name())
                .message("방 참가에 성공했습니다.")
                .build();
    }

    /**
     * 역할 선호도를 결정하는 메서드
     * RANDOM인 경우 여유 있는 역할군으로 자동 배정
     */
    private RolePreference determineRolePreference(
            JoinRoomRequestDto.RolePreference requestedPreference,
            List<RoomMember> currentMembers,
            Integer policeCapacity,
            Integer thiefCapacity
    ) {
        // 현재 역할군별 인원 계산
        long currentPoliceCount = currentMembers.stream()
                .filter(m -> m.getRolePreference() == RolePreference.POLICE)
                .count();
        long currentThiefCount = currentMembers.stream()
                .filter(m -> m.getRolePreference() == RolePreference.THIEF)
                .count();

        // RANDOM이 아닌 경우: 요청한 역할군의 정원 확인
        if (requestedPreference == JoinRoomRequestDto.RolePreference.POLICE) {
            if (currentPoliceCount >= policeCapacity) {
                throw new BusinessException(ErrorCode.ROLE_CAPACITY_FULL);
            }
            return RolePreference.POLICE;
        } else if (requestedPreference == JoinRoomRequestDto.RolePreference.THIEF) {
            if (currentThiefCount >= thiefCapacity) {
                throw new BusinessException(ErrorCode.ROLE_CAPACITY_FULL);
            }
            return RolePreference.THIEF;
        }

        // RANDOM인 경우: 여유 있는 역할군으로 배정
        boolean policeAvailable = currentPoliceCount < policeCapacity;
        boolean thiefAvailable = currentThiefCount < thiefCapacity;

        if (policeAvailable && thiefAvailable) {
            // 둘 다 여유 있으면 랜덤 배정
            return Math.random() < 0.5 ? RolePreference.POLICE : RolePreference.THIEF;
        } else if (policeAvailable) {
            return RolePreference.POLICE;
        } else if (thiefAvailable) {
            return RolePreference.THIEF;
        } else {
            // 둘 다 꽉 찬 경우 (이론적으로는 전체 정원 체크에서 걸려야 함)
            throw new BusinessException(ErrorCode.ROOM_FULL);
        }
    }

    @Transactional
    public AssignRolesResponseDto assignRolesAndStartGame(Long roomId, AssignRolesRequestDto request, Long hostUserId) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 2. 권한 확인: 방장만 가능
        if (!room.getHost().getId().equals(hostUserId)) {
            throw new BusinessException(ErrorCode.ONLY_HOST_ALLOWED);
        }

        // 3. 방 상태 확인: WAITING 상태일 때만 팀 배정 가능
        if (room.getStatus() != RoomStatus.WAITING) {
            throw new BusinessException(ErrorCode.ROOM_NOT_IN_WAITING_STATUS);
        }

        // 4. 모든 멤버 조회
        List<RoomMember> allMembers = roomMemberRepository.findAllByRoomIdWithUser(roomId);
        System.out.println("=== 조회된 멤버 수: " + allMembers.size());
        allMembers.forEach(m -> System.out.println("멤버: userId=" + m.getUser().getId() + ", nickname=" + m.getUser().getNickname()));

        // 5. 역할 배정 요청을 Map으로 변환 (userId -> role)
        Map<Long, String> roleMap = request.getRoles().stream()
                .collect(Collectors.toMap(
                        AssignRolesRequestDto.RoleAssignment::getUserId,
                        AssignRolesRequestDto.RoleAssignment::getRole
                ));

        // 6. 각 멤버에게 역할 배정
        for (RoomMember member : allMembers) {
            String roleString = roleMap.get(member.getUser().getId());
            if (roleString == null) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }

            Role role = Role.valueOf(roleString.toUpperCase());
            member.assignRole(role);
            System.out.println("역할 배정: userId=" + member.getUser().getId() + ", role=" + role);
        }

        // 7. 방 상태를 PLAYING으로 변경
        room.updateStatus(RoomStatus.PLAYING);
        System.out.println("방 상태 변경: roomId=" + roomId + ", status=PLAYING");

        // 8. 응답 DTO 생성 및 반환
        System.out.println("=== 응답 생성 전 멤버 수: " + allMembers.size());
        allMembers.forEach(m -> System.out.println("최종 상태: userId=" + m.getUser().getId() + ", role=" + m.getRole()));
        return roomMemberConverter.convertToAssignRolesResponseDto(roomId, allMembers);
    }

}
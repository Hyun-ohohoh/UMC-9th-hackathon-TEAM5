package com.example.demo.domain.room_member.converter;

import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.entity.enums.Role;
import com.example.demo.domain.room_member.entity.enums.JoinStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMemberConverter {

    public AssignRolesResponseDto convertToAssignRolesResponseDto(
            Long roomId,
            List<RoomMember> members) {

        // 팀 통계 계산
        long policeCount = members.stream()
                .filter(m -> m.getRole() == Role.POLICE)
                .count();
        long thiefCount = members.stream()
                .filter(m -> m.getRole() == Role.THIEF)
                .count();

        // 참가자 목록 변환
        List<AssignRolesResponseDto.ParticipantInfo> participants = members.stream()
                .map(this::convertToParticipantInfo)
                .collect(Collectors.toList());

        return AssignRolesResponseDto.builder()
                .roomId(roomId)
                .stats(AssignRolesResponseDto.TeamStats.builder()
                        .totalPolice((int) policeCount)
                        .totalThieves((int) thiefCount)
                        .build())
                .participants(participants)
                .build();
    }

    private AssignRolesResponseDto.ParticipantInfo convertToParticipantInfo(RoomMember member) {
        return AssignRolesResponseDto.ParticipantInfo.builder()
                .userId(member.getUser().getId())
                .nickname(member.getUser().getNickname())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .isArrived(member.getIsArrived())
                .build();
    }
    public ParticipantResponseDto toParticipantResponseDto(Long roomId, List<RoomMember> roomMembers) {
        List<AssignRolesResponseDto.ParticipantInfo> infoList = roomMembers.stream()
                .map(member -> AssignRolesResponseDto.ParticipantInfo.builder()
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .role(member.getRole() != null ? member.getRole().name() : "NONE")
                        .isArrived(member.getJoinStatus() == JoinStatus.VERIFIED)
                        .build())
                .collect(Collectors.toList());

        return new ParticipantResponseDto(roomId, infoList);
    }
}

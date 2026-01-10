package com.example.demo.domain.room_member.converter;

import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
import com.example.demo.domain.room_member.dto.response.ParticipantResponseDto;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.entity.enums.JoinStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMemberConverter {


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
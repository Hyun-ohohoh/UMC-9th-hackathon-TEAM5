package com.example.demo.domain.room.converter;

import com.example.demo.domain.room.dto.response.NearbyRoomDataResponseDto;
import com.example.demo.domain.room.dto.response.RoomDetailResponseDto;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room_member.entity.RoomMember;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomConverter {

    public NearbyRoomDataResponseDto convertToDataDto(Room room, BigDecimal userLat, BigDecimal userLon) {

        return NearbyRoomDataResponseDto.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .placeName(room.getPlaceText())
                .lat(room.getLatitude())
                .lng(room.getLongitude())
                .meetingTime(room.getMeetingTime())
                .currentParticipants(0) // 추후 로직 추가
                .maxParticipants(room.getCapacityTotal())
                .status(room.getStatus().name())
                .build();
    }

    public RoomDetailResponseDto convertToRoomDetailDto(Room room, List<RoomMember> members) {
        // 참가자 정보 변환
        List<RoomDetailResponseDto.ParticipantInfo> participants = members.stream()
                .map(member -> RoomDetailResponseDto.ParticipantInfo.builder()
                        .userId(member.getUser().getId())
                        .nickname(member.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());

        // 정원 정보 생성
        RoomDetailResponseDto.CapacityInfo capacity = RoomDetailResponseDto.CapacityInfo.builder()
                .current(members.size())
                .total(room.getCapacityTotal())
                .build();

        // 응답 DTO 생성
        return RoomDetailResponseDto.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .placeName(room.getPlaceText())
                .meetingTime(room.getMeetingTime())
                .status(room.getStatus().name())
                .countdownSeconds(room.getCountdownSeconds())
                .capacity(capacity)
                .participants(participants)
                .build();
    }
}

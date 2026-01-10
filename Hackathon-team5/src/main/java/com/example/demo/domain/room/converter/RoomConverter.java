package com.example.demo.domain.room.converter;

import com.example.demo.domain.room.dto.response.NearbyRoomDataResponseDto;
import com.example.demo.domain.room.entity.Room;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
}

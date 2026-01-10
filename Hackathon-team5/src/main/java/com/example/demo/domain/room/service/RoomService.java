package com.example.demo.domain.room.service;

import com.example.demo.domain.room.converter.RoomConverter;
import com.example.demo.domain.room.dto.request.CreateRoomRequestDto;
import com.example.demo.domain.room.dto.response.CreateRoomResponseDto;
import com.example.demo.domain.room.dto.response.NearbyRoomDataResponseDto;
import com.example.demo.domain.room.dto.response.NearbyRoomsResponseDto;
import com.example.demo.domain.room.dto.response.RoomDetailResponseDto;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.room.repository.RoomRepository;
import com.example.demo.domain.room_member.entity.RoomMember;
import com.example.demo.domain.room_member.entity.enums.JoinStatus;
import com.example.demo.domain.room_member.repository.RoomMemberRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserService userService;
    private final RoomConverter roomConverter;

    @Transactional
    public CreateRoomResponseDto createRoom(CreateRoomRequestDto dto, Long userId) {

        if (dto.getPolice_capacity() + dto.getThief_capacity() > dto.getMaxParticipants()){
            throw new BusinessException(ErrorCode.VALIDATION_ERROR);
        }
        User user = userService.getUserById(userId);
        Room room = Room.builder()
                .host(user)
                .title(dto.getTitle())
                .placeText(dto.getPlaceName())
                .latitude(dto.getLat())
                .longitude(dto.getLng())
                .meetingTime(dto.getMeetingTime())
                .capacityTotal(dto.getMaxParticipants())
                .capacityPolice(dto.getPolice_capacity())
                .capacityThief(dto.getThief_capacity())
                .status(RoomStatus.WAITING)
                .escapeTime(dto.getEscapeTime())
                .countdownSeconds(dto.getCountdownSeconds())
                .build();

        Room savedRoom = roomRepository.save(room);

        // 방장을 room_members에 자동 추가
        RoomMember hostMember = RoomMember.builder()
                .room(savedRoom)
                .user(user)
                .joinStatus(JoinStatus.JOINED)
                .isArrived(false)
                .build();
        roomMemberRepository.save(hostMember);

        System.out.println("방 생성 완료 - roomId: " + savedRoom.getId() + ", 방장 userId: " + user.getId());

        return CreateRoomResponseDto.builder()
                .roomId(savedRoom.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public NearbyRoomsResponseDto getRoomsForUser(Long userId) {
        // 1. 사용자 정보 조회 (사용자의 현재 위도, 경도 활용)
        User user = userService.getUserById(userId);

        // 2. 반경 2km(2000m) 고정 조회
        double FIXED_RADIUS = 3000.0;
        List<Room> rooms = roomRepository.findRoomsWithinRadius(
                user.getLongitude().doubleValue(),
                user.getLatitude().doubleValue(),
                FIXED_RADIUS
        );

        // 3. DTO 변환 및 거리 계산
        List<NearbyRoomDataResponseDto> roomDataList = rooms.stream()
                .map(room -> roomConverter.convertToDataDto(room, user.getLatitude(), user.getLongitude()))
                .collect(Collectors.toList());

        return NearbyRoomsResponseDto.builder()
                .rooms(roomDataList)
                .totalCount(roomDataList.size())
                .build();
    }

    @Transactional(readOnly = true)
    public RoomDetailResponseDto getRoomDetail(Long roomId) {
        // 1. 방 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        // 2. 참가자 목록 조회
        List<RoomMember> members = roomMemberRepository.findAllByRoomIdWithUser(roomId);

        // 3. 컨버터를 통해 DTO 변환
        return roomConverter.convertToRoomDetailDto(room, members);
    }

}

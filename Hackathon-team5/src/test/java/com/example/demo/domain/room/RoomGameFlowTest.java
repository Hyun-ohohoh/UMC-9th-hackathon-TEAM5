//package com.example.demo.domain.room;
//
//import com.example.demo.domain.room.dto.request.CreateRoomRequestDto;
//import com.example.demo.domain.room.dto.response.CreateRoomResponseDto;
//import com.example.demo.domain.room.entity.Room;
//import com.example.demo.domain.room.repository.RoomRepository;
//import com.example.demo.domain.room.service.RoomService;
//import com.example.demo.domain.room_member.dto.request.AssignRolesRequestDto;
//import com.example.demo.domain.room_member.dto.request.JoinRoomRequestDto;
//import com.example.demo.domain.room_member.dto.response.AssignRolesResponseDto;
//import com.example.demo.domain.room_member.dto.response.JoinRoomResponseDto;
//import com.example.demo.domain.room_member.entity.RoomMember;
//import com.example.demo.domain.room_member.repository.RoomMemberRepository;
//import com.example.demo.domain.room_member.service.RoomMemberService;
//import com.example.demo.domain.user.entity.User;
//import com.example.demo.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//@DisplayName("방 생성 -> 참가 -> 시작 통합 테스트")
//class RoomGameFlowTest {
//
//    @Autowired
//    private RoomService roomService;
//
//    @Autowired
//    private RoomMemberService roomMemberService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private RoomMemberRepository roomMemberRepository;
//
//    private User host;
//    private User user2;
//    private User user3;
//    private User user4;
//
//    @BeforeEach
//    void setUp() {
//        // 테스트용 유저 4명 생성
//        host = User.builder()
//                .nickname("방장")
//                .passwordHash("hashed_password")
//                .latitude(new BigDecimal("37.4979"))
//                .longitude(new BigDecimal("127.0276"))
//                .build();
//        userRepository.save(host);
//
//        user2 = User.builder()
//                .nickname("참가자1")
//                .passwordHash("hashed_password")
//                .latitude(new BigDecimal("37.4979"))
//                .longitude(new BigDecimal("127.0276"))
//                .build();
//        userRepository.save(user2);
//
//        user3 = User.builder()
//                .nickname("참가자2")
//                .passwordHash("hashed_password")
//                .latitude(new BigDecimal("37.4979"))
//                .longitude(new BigDecimal("127.0276"))
//                .build();
//        userRepository.save(user3);
//
//        user4 = User.builder()
//                .nickname("참가자3")
//                .passwordHash("hashed_password")
//                .latitude(new BigDecimal("37.4979"))
//                .longitude(new BigDecimal("127.0276"))
//                .build();
//        userRepository.save(user4);
//    }
//
//    @Test
//    @DisplayName("방 생성 -> 3명 참가 -> 역할 배정 및 게임 시작 성공")
//    void testCompleteGameFlow() {
//        // 1. 방 생성 (방장 자동 추가)
//        CreateRoomRequestDto createRoomRequest = new CreateRoomRequestDto(
//                "테스트 방",
//                "강남역 1번 출구",
//                new BigDecimal("37.5006"),
//                new BigDecimal("127.0365"),
//                LocalDateTime.now().plusHours(1),
//                10,
//                2,
//                8,
//                1800,
//                300
//        );
//
//        CreateRoomResponseDto createRoomResponse = roomService.createRoom(createRoomRequest, host.getId());
//        Long roomId = createRoomResponse.getRoomId();
//
//        System.out.println("\n=== 1. 방 생성 완료 ===");
//        System.out.println("방 ID: " + roomId);
//
//        // 방이 생성되었는지 확인
//        Room room = roomRepository.findById(roomId).orElseThrow();
//        assertThat(room).isNotNull();
//        assertThat(room.getTitle()).isEqualTo("테스트 방");
//
//        // 방장이 자동으로 추가되었는지 확인
//        List<RoomMember> membersAfterCreate = roomMemberRepository.findAllByRoomIdWithUser(roomId);
//        assertThat(membersAfterCreate).hasSize(1);
//        assertThat(membersAfterCreate.get(0).getUser().getId()).isEqualTo(host.getId());
//        System.out.println("방장 자동 추가 확인: " + membersAfterCreate.get(0).getUser().getNickname());
//
//        // 2. 참가자 3명 참가
//        System.out.println("\n=== 2. 참가자 참가 시작 ===");
//
//        // 참가자1: 경찰 선호
//        JoinRoomRequestDto joinRequest1 = JoinRoomRequestDto.builder()
//                .rolePreference(JoinRoomRequestDto.RolePreference.POLICE)
//                .build();
//        JoinRoomResponseDto joinResponse1 = roomMemberService.joinRoom(roomId, user2.getId(), joinRequest1);
//        assertThat(joinResponse1.getRolePreference()).isEqualTo("POLICE");
//        System.out.println("참가자1 참가 완료: " + user2.getNickname() + " - " + joinResponse1.getRolePreference());
//
//        // 참가자2: 도둑 선호
//        JoinRoomRequestDto joinRequest2 = JoinRoomRequestDto.builder()
//                .rolePreference(JoinRoomRequestDto.RolePreference.THIEF)
//                .build();
//        JoinRoomResponseDto joinResponse2 = roomMemberService.joinRoom(roomId, user3.getId(), joinRequest2);
//        assertThat(joinResponse2.getRolePreference()).isEqualTo("THIEF");
//        System.out.println("참가자2 참가 완료: " + user3.getNickname() + " - " + joinResponse2.getRolePreference());
//
//        // 참가자3: 랜덤
//        JoinRoomRequestDto joinRequest3 = JoinRoomRequestDto.builder()
//                .rolePreference(JoinRoomRequestDto.RolePreference.RANDOM)
//                .build();
//        JoinRoomResponseDto joinResponse3 = roomMemberService.joinRoom(roomId, user4.getId(), joinRequest3);
//        assertThat(joinResponse3.getRolePreference()).isIn("POLICE", "THIEF");
//        System.out.println("참가자3 참가 완료: " + user4.getNickname() + " - " + joinResponse3.getRolePreference());
//
//        // 전체 참가자 확인 (방장 포함 4명)
//        List<RoomMember> allMembers = roomMemberRepository.findAllByRoomIdWithUser(roomId);
//        assertThat(allMembers).hasSize(4);
//        System.out.println("총 참가자 수: " + allMembers.size() + "명");
//
//        // 3. 역할 배정 및 게임 시작
//        System.out.println("\n=== 3. 역할 배정 및 게임 시작 ===");
//
//        // 역할 배정 요청 생성
//        List<AssignRolesRequestDto.RoleAssignment> roleAssignments = new ArrayList<>();
//        roleAssignments.add(new AssignRolesRequestDto.RoleAssignment(host.getId(), "POLICE"));
//        roleAssignments.add(new AssignRolesRequestDto.RoleAssignment(user2.getId(), "POLICE"));
//        roleAssignments.add(new AssignRolesRequestDto.RoleAssignment(user3.getId(), "THIEF"));
//        roleAssignments.add(new AssignRolesRequestDto.RoleAssignment(user4.getId(), "THIEF"));
//
//        AssignRolesRequestDto assignRolesRequest = new AssignRolesRequestDto(roleAssignments);
//
//        // 역할 배정 및 게임 시작
//        AssignRolesResponseDto assignRolesResponse = roomMemberService.assignRolesAndStartGame(
//                roomId,
//                assignRolesRequest,
//                host.getId()
//        );
//
//        // 응답 검증
//        assertThat(assignRolesResponse).isNotNull();
//        assertThat(assignRolesResponse.getRoomId()).isEqualTo(roomId);
//        assertThat(assignRolesResponse.getStats().getTotalPolice()).isEqualTo(2);
//        assertThat(assignRolesResponse.getStats().getTotalThieves()).isEqualTo(2);
//        assertThat(assignRolesResponse.getParticipants()).hasSize(4);
//
//        System.out.println("\n=== 최종 결과 ===");
//        System.out.println("방 ID: " + assignRolesResponse.getRoomId());
//        System.out.println("경찰: " + assignRolesResponse.getStats().getTotalPolice() + "명");
//        System.out.println("도둑: " + assignRolesResponse.getStats().getTotalThieves() + "명");
//        System.out.println("\n참가자 목록:");
//        assignRolesResponse.getParticipants().forEach(p ->
//                System.out.println("- " + p.getNickname() + " (역할: " + p.getRole() + ")")
//        );
//
//        // 방 상태가 PLAYING으로 변경되었는지 확인
//        Room updatedRoom = roomRepository.findById(roomId).orElseThrow();
//        assertThat(updatedRoom.getStatus().name()).isEqualTo("PLAYING");
//        System.out.println("\n방 상태: " + updatedRoom.getStatus());
//
//        System.out.println("\n✅ 테스트 성공!");
//    }
//}

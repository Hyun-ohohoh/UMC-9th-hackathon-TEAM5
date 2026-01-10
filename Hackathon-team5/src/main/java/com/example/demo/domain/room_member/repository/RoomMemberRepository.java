package com.example.demo.domain.room_member.repository;

import com.example.demo.domain.room_member.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    // 특정 방의 특정 유저 정보를 조회
    Optional<RoomMember> findByRoom_IdAndUser_Id(Long roomId, Long userId);
}
package com.example.demo.domain.room_member.repository;

import com.example.demo.domain.room_member.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    // 특정 방의 모든 참여자 목록을 조회
    List<RoomMember> findAllByRoom_Id(Long roomId);

    // 특정 방의 특정 유저 정보를 조회
    Optional<RoomMember> findByRoom_IdAndUser_Id(Long roomId, Long userId);

    // 특정 방의 모든 멤버 조회 (User 정보도 함께 fetch join)
    @Query("SELECT rm FROM RoomMember rm JOIN FETCH rm.user WHERE rm.room.id = :roomId")
    List<RoomMember> findAllByRoomIdWithUser(@Param("roomId") Long roomId);
}
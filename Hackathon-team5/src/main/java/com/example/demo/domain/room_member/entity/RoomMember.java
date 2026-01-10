package com.example.demo.domain.room_member.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.room_member.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JoinStatus joinStatus = JoinStatus.JOINED;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ThiefState thiefState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caught_by_user_id")
    private User caughtByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escaped_by_user_id")
    private User escapedByUser;

    @Column(name = "caught_at")
    private LocalDateTime caughtAt;

    @Column(name = "is_arrived", nullable = false)
    @Builder.Default
    private Boolean isArrived = false;

    public void updateToArrived() {
        // JoinStatus를 VERIFIED(도착 완료)로 변경
        this.joinStatus = com.example.demo.domain.room_member.entity.enums.JoinStatus.VERIFIED;
    }
}
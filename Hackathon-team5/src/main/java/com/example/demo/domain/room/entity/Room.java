package com.example.demo.domain.room.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.domain.room.entity.enums.RoomStatus;
import com.example.demo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Column(nullable = false)
    private String title;

    @Column(name = "meeting_time", nullable = false)
    private LocalDateTime meetingTime;

    @Column(name = "countdown_seconds", nullable = false)
    private Integer countdownSeconds;

    @Column(name = "escape_time", nullable = false)
    private Integer escapeTime;

    @Column(columnDefinition = "TEXT")
    private String discription;

    @Column(name = "capacity_police", nullable = false)
    private Integer capacityPolice;

    @Column(name = "capacity_thief", nullable = false)
    private Integer capacityThief;

    @Column(name = "place_text")
    private String placeText;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomStatus status = RoomStatus.WAITING;

    public void updateStatus(RoomStatus status) {
        this.status = status;
    }

    public void updateHost(User newHost) {
        this.host = newHost;
    }

    public Integer getCapacityTotal() {
        return this.capacityPolice + this.capacityThief;
    }
}
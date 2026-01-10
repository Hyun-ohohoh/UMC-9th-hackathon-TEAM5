package com.example.demo.domain.room_game.entity;

import com.example.demo.domain.room.entity.Room;
import com.example.demo.domain.room_game.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_game_state")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomGameState {

    @Id
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "game_image_url", columnDefinition = "TEXT")
    private String gameImageUrl;

    @Column(name = "playing_at")
    private LocalDateTime playingAt;

    @Column(name = "playtime_seconds", nullable = false)
    @Builder.Default
    private Integer playtimeSeconds = 0;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private FinishReason finishReason;

    @Enumerated(EnumType.STRING)
    private WinningTeam winningTeam;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void finishGame(FinishReason finishReason, WinningTeam winningTeam) {
        this.finishedAt = LocalDateTime.now();
        // LocalDateTime 차이를 초 단위로 계산
        if (this.playingAt != null) {
            this.playtimeSeconds = (int) java.time.Duration.between(this.playingAt, this.finishedAt).getSeconds();
        }
        this.finishReason = finishReason;
        this.winningTeam = winningTeam;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePlayingTime(LocalDateTime playingAt) {
        this.playingAt = playingAt;
        this.updatedAt = LocalDateTime.now();
    }
}
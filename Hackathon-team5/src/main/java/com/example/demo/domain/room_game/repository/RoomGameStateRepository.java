package com.example.demo.domain.room_game.repository;

import com.example.demo.domain.room_game.entity.RoomGameState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomGameStateRepository extends JpaRepository<RoomGameState, Long> {
}

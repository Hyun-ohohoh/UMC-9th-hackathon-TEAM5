package com.example.demo.domain.room_member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomResponseDto {

    private Long roomId;
    private Long userId;
    private String rolePreference;
    private String message;
}

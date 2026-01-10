package com.example.demo.domain.room_member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomRequestDto {

    @NotNull(message = "역할 선택은 필수입니다")
    private RolePreference rolePreference;

    public enum RolePreference {
        POLICE,  // 경찰 선호
        THIEF,   // 도둑 선호
        RANDOM   // 랜덤
    }
}

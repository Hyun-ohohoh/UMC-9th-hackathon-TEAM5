package com.example.demo.domain.room_member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "팀 배정 요청")
public class AssignRolesRequestDto {

    @Schema(description = "역할 배정 목록")
    @NotEmpty(message = "역할 배정 목록은 비어있을 수 없습니다.")
    @Valid
    private List<RoleAssignment> roles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사용자별 역할 배정")
    public static class RoleAssignment {

        @Schema(description = "사용자 ID", example = "1")
        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @Schema(description = "역할 (POLICE, THIEF)", example = "POLICE")
        @NotNull(message = "역할은 필수입니다.")
        private String role;
    }
}

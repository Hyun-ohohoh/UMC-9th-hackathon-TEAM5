package com.example.demo.domain.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주변 방 목록 응답")
public class NearbyRoomsResponseDto {

    @Schema(description = "방 목록")
    private List<NearbyRoomResponseDto> rooms;

    @Schema(description = "전체 방 개수", example = "10")
    private Integer totalCount;
}

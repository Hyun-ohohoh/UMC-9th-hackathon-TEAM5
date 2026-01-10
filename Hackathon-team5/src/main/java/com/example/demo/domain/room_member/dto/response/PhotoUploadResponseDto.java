package com.example.demo.domain.room_member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사진 업로드 응답")
public class PhotoUploadResponseDto {

    @Schema(description = "기본적으로 이미지에 접근 불가능하게 하고 presigned url 제공")
    private String imageUrl;
}

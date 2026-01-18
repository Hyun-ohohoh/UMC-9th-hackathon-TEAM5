package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 실제 사용되는 공통 에러들
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "서버 내부 오류가 발생했습니다."),

    // 실제 사용되는 검증 에러들
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_001", "입력값 검증에 실패했습니다."),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "VALIDATION_002", "필수 필드가 누락되었습니다."),

    // 인증/권한 에러들
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_002", "접근이 거절되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_003", "유저를 찾을 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_004", "권한이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH_005", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "AUTH_006", "이미 사용 중인 닉네임입니다."),

    // JWT 관련 에러들
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_007", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_008", "만료된 토큰입니다."),


    // 리소스 에러들
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_001", "요청한 리소스를 찾을 수 없습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_002", "방을 찾을 수 없습니다."),
    ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_003", "방 참가자를 찾을 수 없습니다."),

    GAME_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_004", "game state를 찾을 수 없습니다."),

    // 충돌 오류
    CONCURRENCY_CONFLICT(HttpStatus.CONFLICT, "CONFLICT_001", "요청이 다른 사용자와 충돌했습니다. 페이지를 새로고침 후 다시 시도해주세요."),
    
    // 비즈니스 로직 에러들
    ONLY_HOST_ALLOWED(HttpStatus.FORBIDDEN, "BUSINESS_001", "방장만 접근할 수 있습니다."),
    ROOM_NOT_IN_WAITING_STATUS(HttpStatus.BAD_REQUEST, "BUSINESS_002", "대기 중인 방에서만 가능한 작업입니다."),
    ALREADY_JOINED_ROOM(HttpStatus.BAD_REQUEST, "BUSINESS_003", "이미 참가한 방입니다."),
    ROOM_FULL(HttpStatus.BAD_REQUEST, "BUSINESS_004", "방 정원이 가득 찼습니다."),
    ROLE_CAPACITY_FULL(HttpStatus.BAD_REQUEST, "BUSINESS_005", "선택한 역할군의 정원이 가득 찼습니다."),
    ROOM_NOT_IN_PLAYING_STATUS(HttpStatus.BAD_REQUEST, "BUSINESS_006", "게임 중인 방에서만 가능한 작업입니다."),
    GAME_NOT_STARTED(HttpStatus.BAD_REQUEST, "BUSINESS_006", "게임이 진행 중일 때만 가능합니다."),
    NOT_A_THIEF(HttpStatus.BAD_REQUEST, "BUSINESS_007", "도둑만 탈옥할 수 있습니다."),
    NOT_IN_JAIL(HttpStatus.BAD_REQUEST, "BUSINESS_008", "현재 감옥에 갇힌 상태가 아닙니다."),
    HOST_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "BUSINESS_009", "방장은 방을 나갈 수 없습니다."),
    NOT_JOINED_ROOM(HttpStatus.BAD_REQUEST, "BUSINESS_010", "참가하지 않은 방입니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_004", "파일 업로드 중 오류가 발생했습니다."),;


    // TODO: 비즈니스 로직 개발하면서 필요한 에러코드들 추가
    private final HttpStatus status;
    private final String code;
    private final String message;
}
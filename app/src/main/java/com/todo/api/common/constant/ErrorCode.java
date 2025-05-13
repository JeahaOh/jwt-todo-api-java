package com.todo.api.common.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값 검증 실패"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 인자가 전달되었습니다."),
    INVALID_STATE(HttpStatus.BAD_REQUEST, "잘못된 상태입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // 409 Conflict
    RESOURCE_CONFLICT(HttpStatus.CONFLICT, "리소스 충돌이 발생했습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 호출 중 오류가 발생했습니다."),

    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    MEMBER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    MEMBER_UPDATE_NO_DATA(HttpStatus.BAD_REQUEST, "변경할 데이터가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}

package com.todo.api.common.util;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import com.todo.api.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseUtilWithCustomExceptionTest {
    @Test
    @DisplayName("CustomException - 기본 메시지 사용")
    void testCustomExceptionWithDefaultMessage() {
        // given
        CustomException exception = new CustomException(ErrorCode.USER_NOT_FOUND);

        // when
        CustomResponse<?> response = ResponseUtil.fail(exception);

        // then
        assertAll(
                () -> assertFalse(response.isSuccess()),
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("사용자를 찾을 수 없습니다.", response.getMessage()),
                () -> assertNull(response.getData()));
    }

    @Test
    @DisplayName("CustomException - 커스텀 메시지 사용")
    void testCustomExceptionWithCustomMessage() {
        // given
        String customMessage = "사용자 ID 12345를 찾을 수 없습니다.";
        CustomException exception = new CustomException(ErrorCode.USER_NOT_FOUND, customMessage);

        // when
        CustomResponse<?> response = ResponseUtil.fail(exception);

        // then
        assertAll(
                () -> assertFalse(response.isSuccess()),
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals(customMessage, response.getMessage()),
                () -> assertNull(response.getData()));
    }

    @Test
    @DisplayName("다양한 ErrorCode 테스트")
    void testVariousErrorCodes() {
        // 400 Bad Request 계열
        CustomResponse<?> invalidRequest = ResponseUtil.fail(
                new CustomException(ErrorCode.INVALID_REQUEST));
        assertEquals(400, invalidRequest.getStatus());
        assertEquals("잘못된 요청입니다.", invalidRequest.getMessage());

        // 401 Unauthorized 계열
        CustomResponse<?> unauthorized = ResponseUtil.fail(
                new CustomException(ErrorCode.UNAUTHORIZED));
        assertEquals(401, unauthorized.getStatus());
        assertEquals("인증이 필요합니다.", unauthorized.getMessage());

        // 403 Forbidden 계열
        CustomResponse<?> forbidden = ResponseUtil.fail(
                new CustomException(ErrorCode.FORBIDDEN));
        assertEquals(403, forbidden.getStatus());
        assertEquals("접근 권한이 없습니다.", forbidden.getMessage());

        // 409 Conflict 계열
        CustomResponse<?> conflict = ResponseUtil.fail(
                new CustomException(ErrorCode.DUPLICATE_RESOURCE));
        assertEquals(409, conflict.getStatus());
        assertEquals("이미 존재하는 리소스입니다.", conflict.getMessage());

        // 500 Internal Server Error 계열
        CustomResponse<?> serverError = ResponseUtil.fail(
                new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        assertEquals(500, serverError.getStatus());
        assertEquals("서버 내부 오류입니다.", serverError.getMessage());
    }

    @Test
    @DisplayName("회원 관련 ErrorCode 테스트")
    void testMemberRelatedErrorCodes() {
        // MEMBER_NOT_FOUND
        CustomResponse<?> memberNotFound = ResponseUtil.fail(
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        assertEquals(404, memberNotFound.getStatus());
        assertEquals("존재하지 않는 회원입니다.", memberNotFound.getMessage());

        // MEMBER_EMAIL_DUPLICATE
        CustomResponse<?> emailDuplicate = ResponseUtil.fail(
                new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATE));
        assertEquals(409, emailDuplicate.getStatus());
        assertEquals("이미 존재하는 이메일입니다.", emailDuplicate.getMessage());

        // MEMBER_PASSWORD_MISMATCH
        CustomResponse<?> passwordMismatch = ResponseUtil.fail(
                new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH));
        assertEquals(400, passwordMismatch.getStatus());
        assertEquals("비밀번호가 일치하지 않습니다.", passwordMismatch.getMessage());
    }
}
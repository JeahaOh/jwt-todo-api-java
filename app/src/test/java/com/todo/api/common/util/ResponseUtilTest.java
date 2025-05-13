package com.todo.api.common.util;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import com.todo.api.common.exception.CustomException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilTest {

    @Nested
    @DisplayName("✅ CustomResponse 테스트")
    class CustomResponseTest {

        @Test
        @DisplayName("성공 응답 생성 테스트")
        void successResponse_shouldContainCorrectFields() {
            String data = "test-data";
            CustomResponse<String> response = ResponseUtil.success(data);

            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getMessage()).isEqualTo("Success");
            assertThat(response.getData()).isEqualTo("test-data");
        }

        @Test
        @DisplayName("성공 응답 + 커스텀 메시지")
        void successResponseWithCustomMessage() {
            String data = "custom-data";
            String message = "데이터가 성공적으로 처리되었습니다.";
            CustomResponse<String> response = ResponseUtil.success(message, data);

            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getMessage()).isEqualTo(message);
            assertThat(response.getData()).isEqualTo(data);
        }
    }

    @Nested
    @DisplayName("❌ 실패 응답 테스트")
    class FailResponseTest {

        @Test
        @DisplayName("HTTP 상태 및 메시지로 실패 응답 생성")
        void failResponseWithStatusAndMessage() {
            String message = "요청 실패";
            CustomResponse<?> response = ResponseUtil.fail(HttpStatus.BAD_REQUEST, message);

            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getMessage()).isEqualTo(message);
            assertThat(response.getData()).isNull();
        }

        @Test
        @DisplayName("CustomException 기반 실패 응답 생성")
        void failResponseWithCustomException() {
            CustomException ce = new CustomException(ErrorCode.MEMBER_NOT_FOUND);
            CustomResponse<?> response = ResponseUtil.fail(ce);

            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            assertThat(response.getMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getMessage());
            assertThat(response.getData()).isNull();
        }

        @Test
        @DisplayName("일반 Exception 기반 실패 응답 생성")
        void failResponseWithNormalException() {
            Exception e = new RuntimeException("서버 내부 오류");
            CustomResponse<?> response = ResponseUtil.fail(e);

            assertThat(response.isSuccess()).isFalse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(response.getMessage()).isEqualTo("서버 내부 오류");
            assertThat(response.getData()).isNull();
        }

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
    }

    @Nested
    @DisplayName("⚠️ CustomException 테스트")
    class CustomExceptionTest {

        @Test
        @DisplayName("에러코드만으로 생성")
        void createCustomExceptionWithErrorCode() {
            CustomException exception = new CustomException(ErrorCode.INVALID_TOKEN);

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_TOKEN);
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_TOKEN.getMessage());
        }

        @Test
        @DisplayName("에러코드 + 상세 메시지 생성")
        void createCustomExceptionWithDetailMessage() {
            String detail = "토큰이 변조되었습니다.";
            CustomException exception = new CustomException(ErrorCode.INVALID_TOKEN, detail);

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_TOKEN);
            assertThat(exception.getMessage()).isEqualTo(detail);
        }
    }

    @Nested
    @DisplayName("🔢 ErrorCode Enum 테스트")
    class ErrorCodeTest {

        @Test
        @DisplayName("에러코드 enum 값이 정확한지 확인")
        void errorCodeEnumValidation() {
            ErrorCode code = ErrorCode.DUPLICATE_USER;

            assertThat(code.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(code.getMessage()).isEqualTo("이미 존재하는 사용자입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 사용자 에러코드 확인")
        void memberNotFoundErrorCode() {
            ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

            assertThat(code.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(code.getMessage()).isEqualTo("존재하지 않는 회원입니다.");
        }
    }

    @Nested
    @DisplayName("ErrorCode 자체 테스트")
    class ErrorCodeTests {

        @Test
        @DisplayName("ErrorCode의 HTTP 상태 코드 매핑 검증")
        void testErrorCodeStatusMapping() {
            // 400 계열
            assertEquals(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST.getHttpStatus());
            assertEquals(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED.getHttpStatus());

            // 401 계열
            assertEquals(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getHttpStatus());
            assertEquals(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_TOKEN.getHttpStatus());

            // 403 계열
            assertEquals(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN.getHttpStatus());
            assertEquals(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED.getHttpStatus());

            // 404 계열
            assertEquals(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND.getHttpStatus());
            assertEquals(HttpStatus.NOT_FOUND, ErrorCode.TODO_NOT_FOUND.getHttpStatus());

            // 409 계열
            assertEquals(HttpStatus.CONFLICT, ErrorCode.RESOURCE_CONFLICT.getHttpStatus());
            assertEquals(HttpStatus.CONFLICT, ErrorCode.DUPLICATE_RESOURCE.getHttpStatus());

            // 500 계열
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.DATABASE_ERROR.getHttpStatus());
        }

        @Test
        @DisplayName("ErrorCode 메시지 확인")
        void testErrorCodeMessages() {
            assertAll(
                    () -> assertEquals("잘못된 요청입니다.", ErrorCode.INVALID_REQUEST.getMessage()),
                    () -> assertEquals("인증이 필요합니다.", ErrorCode.UNAUTHORIZED.getMessage()),
                    () -> assertEquals("접근 권한이 없습니다.", ErrorCode.FORBIDDEN.getMessage()),
                    () -> assertEquals("사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND.getMessage()),
                    () -> assertEquals("리소스 충돌이 발생했습니다.", ErrorCode.RESOURCE_CONFLICT.getMessage()),
                    () -> assertEquals("서버 내부 오류입니다.", ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
        }

        @Test
        @DisplayName("ErrorCode 카테고리별 그룹화 테스트")
        void testErrorCodeCategorization() {
            // 400 계열 ErrorCode들
            List<ErrorCode> badRequestCodes = Arrays.asList(
                    ErrorCode.INVALID_REQUEST,
                    ErrorCode.DUPLICATE_USER,
                    ErrorCode.VALIDATION_FAILED,
                    ErrorCode.INVALID_ARGUMENT,
                    ErrorCode.INVALID_STATE,
                    ErrorCode.MEMBER_PASSWORD_MISMATCH,
                    ErrorCode.MEMBER_UPDATE_NO_DATA);

            badRequestCodes.forEach(code -> assertEquals(HttpStatus.BAD_REQUEST, code.getHttpStatus(),
                    code.name() + " should be BAD_REQUEST"));

            // 404 계열 ErrorCode들
            List<ErrorCode> notFoundCodes = Arrays.asList(
                    ErrorCode.USER_NOT_FOUND,
                    ErrorCode.TODO_NOT_FOUND,
                    ErrorCode.RESOURCE_NOT_FOUND,
                    ErrorCode.MEMBER_NOT_FOUND);

            notFoundCodes.forEach(code -> assertEquals(HttpStatus.NOT_FOUND, code.getHttpStatus(),
                    code.name() + " should be NOT_FOUND"));
        }
    }
}

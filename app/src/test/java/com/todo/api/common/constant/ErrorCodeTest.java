package com.todo.api.common.constant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unit")
public class ErrorCodeTest {
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

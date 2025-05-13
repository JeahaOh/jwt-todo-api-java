package com.todo.api.common.exception;

import com.todo.api.common.constant.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

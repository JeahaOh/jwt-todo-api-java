package com.todo.api.common.util;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ResponseUtil {
    public static <T> CustomResponse<T> success(T data) {
        return new CustomResponse<>(true, HttpStatus.OK.value(), "Success", data);
    }

    public static <T> CustomResponse<T> success(String message, T data) {
        return new CustomResponse<>(true, HttpStatus.OK.value(), message, data);
    }

    public static CustomResponse<?> fail(HttpStatus status, String message) {
        return new CustomResponse<>(false, status.value(), message, null);
    }

    public static CustomResponse<?> fail(Exception e) {
        if (e instanceof CustomException ce) {
            return new CustomResponse<>(
                    false,
                    ce.getErrorCode().getHttpStatus().value(),
                    ce.getMessage(),
                    null
            );
        }
        return new CustomResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
    }
}

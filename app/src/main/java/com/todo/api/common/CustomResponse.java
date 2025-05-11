package com.todo.api.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;

    public CustomResponse(boolean success, int status, String message, T data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
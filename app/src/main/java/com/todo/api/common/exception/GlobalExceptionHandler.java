package com.todo.api.common.exception;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import com.todo.api.common.util.ResponseUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = { Exception.class })
  public ResponseEntity<CustomResponse<?>> handleGenericException(Exception ex) {
    log.error("CUZ : {}, MSG : {}", ex.getMessage(), ex.getMessage());
    ex.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseUtil.fail(ex));
  }

  @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
  public ResponseEntity<CustomResponse<?>> handleBadRequest(RuntimeException ex) {
    log.error("CUZ : {}, MSG : {}", ex.getMessage(), ex.getMessage());
    ex.printStackTrace();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ResponseUtil.fail(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomResponse<?>> handleCustomException(CustomException e) {
    ErrorCode code = e.getErrorCode();
    return ResponseEntity
        .status(code.getHttpStatus())
        .body(ResponseUtil.fail(code.getHttpStatus(), code.getMessage()));
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<CustomResponse<?>> handleEntityExistsException(EntityExistsException e) {
    log.error("EntityExistsException: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ResponseUtil.fail(HttpStatus.CONFLICT, e.getMessage()));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<CustomResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
    log.error("EntityNotFoundException: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ResponseUtil.fail(HttpStatus.NOT_FOUND, e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .findFirst()
        .orElse("유효하지 않은 요청입니다.");

    log.error("MethodArgumentNotValidException: {}", message);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ResponseUtil.fail(HttpStatus.BAD_REQUEST, message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<CustomResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
    String message = e.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .findFirst()
        .orElse("유효하지 않은 요청입니다.");

    log.error("ConstraintViolationException: {}", message);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ResponseUtil.fail(HttpStatus.BAD_REQUEST, message));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<CustomResponse<?>> handleBindException(BindException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .findFirst()
        .orElse("유효하지 않은 요청입니다.");

    log.error("BindException: {}", message);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ResponseUtil.fail(HttpStatus.BAD_REQUEST, message));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<CustomResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
    log.error("AuthorizationDeniedException: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ResponseUtil.fail(HttpStatus.FORBIDDEN, e.getMessage()));
  }

  @ExceptionHandler(value = { RuntimeException.class })
  public ResponseEntity<CustomResponse<?>> handleRuntimeException(RuntimeException ex) {
    log.error("CUZ : {}, MSG : {}", ex.getMessage(), ex.getMessage());

    HttpStatus status;
    if (ex.getMessage().contains("찾을 수 없습니다")) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex.getMessage().contains("권한이 없습니다") || ex.getMessage().contains("접근")) {
      status = HttpStatus.FORBIDDEN;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return ResponseEntity
        .status(status)
        .body(ResponseUtil.fail(status, ex.getMessage()));
  }
}
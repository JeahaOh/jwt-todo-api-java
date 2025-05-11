package com.todo.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    CustomResponse<?> customResponse = new CustomResponse<>(
        false,
        ErrorCode.FORBIDDEN.getHttpStatus().value(),
        ErrorCode.FORBIDDEN.getMessage(),
        null);

    response.getWriter().write(objectMapper.writeValueAsString(customResponse));
  }
}
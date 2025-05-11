package com.todo.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    CustomResponse<?> customResponse = new CustomResponse<>(
        false,
        ErrorCode.UNAUTHORIZED.getHttpStatus().value(),
        ErrorCode.UNAUTHORIZED.getMessage(),
        null);

    response.getWriter().write(objectMapper.writeValueAsString(customResponse));
  }
}
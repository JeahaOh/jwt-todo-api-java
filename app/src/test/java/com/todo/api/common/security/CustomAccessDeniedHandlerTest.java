package com.todo.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.common.CustomResponse;
import com.todo.api.common.constant.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("unit")
public class CustomAccessDeniedHandlerTest {

  private CustomAccessDeniedHandler accessDeniedHandler;
  private ObjectMapper objectMapper;

  private HttpServletRequest request;
  private HttpServletResponse response;
  private StringWriter responseWriter;

  @BeforeEach
  void setUp() throws IOException {
    objectMapper = new ObjectMapper();
    accessDeniedHandler = new CustomAccessDeniedHandler(objectMapper);

    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    responseWriter = new StringWriter();

    PrintWriter printWriter = new PrintWriter(responseWriter);
    when(response.getWriter()).thenReturn(printWriter);
  }

  @Test
  void 일반_AccessDeniedException_처리_테스트() throws IOException, ServletException {
    // given
    AccessDeniedException exception = new AccessDeniedException("Access denied");

    // when
    accessDeniedHandler.handle(request, response, exception);

    // then
    verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    verify(response).setContentType("application/json");
    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.name());

    CustomResponse<?> result = objectMapper.readValue(responseWriter.toString(), CustomResponse.class);
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getStatus()).isEqualTo(ErrorCode.FORBIDDEN.getHttpStatus().value());
    assertThat(result.getMessage()).isEqualTo(ErrorCode.FORBIDDEN.getMessage());
  }

  @Test
  void AuthorizationDeniedException_처리_테스트() throws IOException, ServletException {
    // given
    String customMessage = "접근 권한이 없습니다.";
    AccessDeniedException exception = new AccessDeniedException(customMessage);

    // when
    accessDeniedHandler.handle(request, response, exception);

    // then
    verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    verify(response).setContentType("application/json");
    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.name());

    CustomResponse<?> result = objectMapper.readValue(responseWriter.toString(), CustomResponse.class);
    assertThat(result.isSuccess()).isFalse();
    assertThat(result.getStatus()).isEqualTo(ErrorCode.FORBIDDEN.getHttpStatus().value());
    assertThat(result.getMessage()).isEqualTo(customMessage);
  }
}
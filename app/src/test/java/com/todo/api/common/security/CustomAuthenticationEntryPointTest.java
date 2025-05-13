package com.todo.api.common.security;

import com.todo.api.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomAuthenticationEntryPointTest extends SecurityIntegrationTest {

  @Autowired
  private CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Test
  @DisplayName("인증되지 않은 요청에 대해 401 Unauthorized 응답을 반환한다")
  void commence_ShouldReturn401Unauthorized() throws Exception {
    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.status").value(ErrorCode.UNAUTHORIZED.getHttpStatus().value()))
        .andExpect(jsonPath("$.message").value(ErrorCode.UNAUTHORIZED.getMessage()));
  }
}
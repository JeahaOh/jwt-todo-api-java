package com.todo.api.integration.mmbr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.mmbr.dto.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integration")
public class MemberControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("회원가입 -> 로그인 -> JWT 인증 흐름 테스트")
  void testSignupLoginAndJwtAuthentication() throws Exception {
    // 1. 회원가입
    MemberDto.SignUpRequest signUpRequest = new MemberDto.SignUpRequest();
    signUpRequest.setEmail("test@example.com");
    signUpRequest.setName("테스트");
    signUpRequest.setPassword("password123");

    mockMvc.perform(post("/users/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.email").value("test@example.com"));

    // 2. 로그인
    MemberDto.LoginRequest loginRequest = new MemberDto.LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password123");

    MvcResult loginResult = mockMvc.perform(post("/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.accessToken").exists())
        .andReturn();

    String responseBody = loginResult.getResponse().getContentAsString();
    String accessToken = objectMapper.readTree(responseBody)
        .path("data")
        .path("accessToken")
        .asText();

    // 3. JWT 토큰으로 인증이 필요한 API 호출
    mockMvc.perform(get("/users/me")
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.email").value("test@example.com"));

    // 4. 토큰 없이 API 호출 시 401 에러 확인
    mockMvc.perform(get("/users/me"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("JWT 인증 실패 테스트")
  void testJwtAuthenticationFailure() throws Exception {
    // 1. 유효하지 않은 토큰으로 API 호출
    mockMvc.perform(get("/users/me")
        .header("Authorization", "Bearer invalid.token.here"))
        .andExpect(status().isUnauthorized());

    // 2. 잘못된 형식의 토큰으로 API 호출
    mockMvc.perform(get("/users/me")
        .header("Authorization", "invalid-format"))
        .andExpect(status().isUnauthorized());

    // 3. 토큰 없이 API 호출
    mockMvc.perform(get("/users/me"))
        .andExpect(status().isUnauthorized());
  }
}

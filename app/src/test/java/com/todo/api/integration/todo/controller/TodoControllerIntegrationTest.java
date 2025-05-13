package com.todo.api.integration.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.mmbr.dto.MemberDto;
import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
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
public class TodoControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private String accessToken;

  @BeforeEach
  void setUp() throws Exception {
    // 1. 회원가입
    MemberDto.SignUpRequest signUpRequest = new MemberDto.SignUpRequest();
    signUpRequest.setEmail("test@example.com");
    signUpRequest.setName("테스트");
    signUpRequest.setPassword("password123");

    mockMvc.perform(post("/users/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest)));

    // 2. 로그인하여 토큰 발급
    MemberDto.LoginRequest loginRequest = new MemberDto.LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password123");

    MvcResult loginResult = mockMvc.perform(post("/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andReturn();

    String responseBody = loginResult.getResponse().getContentAsString();
    accessToken = objectMapper.readTree(responseBody)
        .path("data")
        .path("accessToken")
        .asText();
  }

  @Test
  @DisplayName("TODO 생성 -> 목록조회 -> 수정 -> 삭제 테스트")
  void testTodoCrudFlow() throws Exception {
    // 1. TODO 생성
    TodoCreateRequest createRequest = new TodoCreateRequest();
    createRequest.setTitle("테스트 할일");
    createRequest.setDescription("테스트 할일 설명입니다.");

    MvcResult createResult = mockMvc.perform(post("/todos")
        .header("Authorization", "Bearer " + accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.title").value("테스트 할일"))
        .andReturn();

    String responseBody = createResult.getResponse().getContentAsString();
    Integer todoNo = objectMapper.readTree(responseBody)
        .path("data")
        .path("no")
        .asInt();

    // 2. TODO 목록 조회
    mockMvc.perform(get("/todos")
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.content[0].title").value("테스트 할일"));

    // 3. TODO 수정
    TodoUpdateRequest updateRequest = new TodoUpdateRequest();
    updateRequest.setTitle("수정된 할일");
    updateRequest.setDescription("수정된 할일 설명입니다.");
    updateRequest.setCompleted(true);

    mockMvc.perform(put("/todos/" + todoNo)
        .header("Authorization", "Bearer " + accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.title").value("수정된 할일"))
        .andExpect(jsonPath("$.data.completed").value(true));

    // 4. TODO 삭제
    mockMvc.perform(delete("/todos/" + todoNo)
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));

    // 5. 삭제된 TODO 조회 시 404 확인
    mockMvc.perform(get("/todos/" + todoNo)
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("JWT 인증 실패 테스트")
  void testJwtAuthenticationFailure() throws Exception {
    // 1. 유효하지 않은 토큰으로 API 호출
    mockMvc.perform(get("/todos")
        .header("Authorization", "Bearer invalid.token.here"))
        .andExpect(status().isUnauthorized());

    // 2. 잘못된 형식의 토큰으로 API 호출
    mockMvc.perform(get("/todos")
        .header("Authorization", "invalid-format"))
        .andExpect(status().isUnauthorized());

    // 3. 토큰 없이 API 호출
    mockMvc.perform(get("/todos"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("유효하지 않은 ID 접근 테스트")
  void testInvalidIdAccess() throws Exception {
    // 1. 존재하지 않는 TODO ID로 조회
    mockMvc.perform(get("/todos/999999")
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound());

    // 2. 존재하지 않는 TODO ID로 수정
    TodoUpdateRequest updateRequest = new TodoUpdateRequest();
    updateRequest.setTitle("수정된 할일");
    updateRequest.setDescription("수정된 할일 설명입니다.");
    updateRequest.setCompleted(true);

    mockMvc.perform(put("/todos/999999")
        .header("Authorization", "Bearer " + accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());

    // 3. 존재하지 않는 TODO ID로 삭제
    mockMvc.perform(delete("/todos/999999")
        .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound());
  }
}

package com.todo.api.common.security;

import com.todo.api.common.util.JwtUtil;
import com.todo.api.mmbr.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterTest extends SecurityIntegrationTest {

  @Autowired
  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("유효한 JWT 토큰이 있는 요청은 인증이 성공한다")
  void doFilterInternal_WithValidToken_ShouldAuthenticate() throws Exception {
    // given
    String email = "test@example.com";
    String password = "password123";
    String name = "test name";
    Member member = createTestMember(email, password, name);
    String token = jwtUtil.generateToken(email, member.getNo());

    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("모든 할일 목록"));
  }

  @Test
  @DisplayName("유효하지 않은 JWT 토큰이 있는 요청은 인증이 실패한다")
  void doFilterInternal_WithInvalidToken_ShouldNotAuthenticate() throws Exception {
    // given
    String invalidToken = "invalid.token.here";

    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
        .header("Authorization", "Bearer " + invalidToken)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @DisplayName("JWT 토큰이 없는 요청은 인증이 실패한다")
  void doFilterInternal_WithoutToken_ShouldNotAuthenticate() throws Exception {
    // when & then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }
}
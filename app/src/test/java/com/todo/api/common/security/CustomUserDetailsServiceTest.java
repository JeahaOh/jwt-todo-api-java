package com.todo.api.common.security;

import com.todo.api.mmbr.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomUserDetailsServiceTest extends SecurityIntegrationTest {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Test
  @DisplayName("존재하는 사용자의 이메일로 UserDetails를 조회할 수 있다")
  void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {
    // given
    String email = "test@example.com";
    String password = "password123";
    String name = "test name";
    createTestMember(email, password, name);

    // when
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    // then
    assertThat(userDetails.getUsername()).isEqualTo(email);
    assertThat(userDetails.getAuthorities()).hasSize(1);
    assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
  }

  @Test
  @DisplayName("존재하지 않는 사용자의 이메일로 조회 시 UsernameNotFoundException이 발생한다")
  void loadUserByUsername_WithNonExistingUser_ShouldThrowException() {
    // given
    String nonExistingEmail = "nonexisting@example.com";

    // when & then
    assertThatThrownBy(() -> userDetailsService.loadUserByUsername(nonExistingEmail))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("존재하지 않는 이메일입니다.");
  }
}
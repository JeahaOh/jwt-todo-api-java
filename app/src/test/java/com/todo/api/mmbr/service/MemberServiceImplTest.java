package com.todo.api.mmbr.service;

import com.todo.api.common.constant.ErrorCode;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.util.JwtUtil;
import com.todo.api.mmbr.domain.Member;
import com.todo.api.mmbr.dto.MemberDto;
import com.todo.api.mmbr.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

  @InjectMocks
  private MemberServiceImpl memberService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private Authentication authentication;

  private Member testMember;
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TEST_PASSWORD = "password123";
  private static final String TEST_NAME = "테스트";
  private static final String TEST_JWT = "test.jwt.token";

  @BeforeEach
  void setUp() {
    testMember = new Member();
    testMember.setNo(1);
    testMember.setEmail(TEST_EMAIL);
    testMember.setName(TEST_NAME);
    testMember.setPassword(TEST_PASSWORD);
    testMember.setCreatedAt(LocalDateTime.now());
    testMember.setUpdatedAt(LocalDateTime.now());

    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  @DisplayName("회원가입에 성공하면 회원 정보가 반환된다")
  void signUp_Success() {
    // Given
    MemberDto.SignUpRequest request = new MemberDto.SignUpRequest();
    request.setEmail(TEST_EMAIL);
    request.setPassword(TEST_PASSWORD);
    request.setName(TEST_NAME);

    when(memberRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
    when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encodedPassword");
    when(memberRepository.save(any(Member.class))).thenReturn(testMember);

    // When
    MemberDto.SignUpResponse response = memberService.signUp(request);

    // Then
    assertThat(response.getEmail()).isEqualTo(TEST_EMAIL);
    assertThat(response.getName()).isEqualTo(TEST_NAME);
    verify(memberRepository).save(any(Member.class));
  }

  @Test
  @DisplayName("이미 등록된 이메일로 회원가입 시 예외가 발생한다")
  void signUp_DuplicateEmail() {
    // Given
    MemberDto.SignUpRequest request = new MemberDto.SignUpRequest();
    request.setEmail(TEST_EMAIL);
    request.setPassword(TEST_PASSWORD);
    request.setName(TEST_NAME);

    when(memberRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> memberService.signUp(request))
        .isInstanceOf(CustomException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_EMAIL_DUPLICATE);
  }

  @Test
  @DisplayName("로그인에 성공하면 액세스 토큰과 회원 정보가 반환된다")
  void login_Success() {
    // Given
    MemberDto.LoginRequest request = new MemberDto.LoginRequest();
    request.setEmail(TEST_EMAIL);
    request.setPassword(TEST_PASSWORD);

    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
    when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
    when(jwtUtil.generateToken(TEST_EMAIL, testMember.getNo())).thenReturn(TEST_JWT);

    // When
    MemberDto.LoginResponse response = memberService.login(request);

    // Then
    assertThat(response.getAccessToken()).isEqualTo(TEST_JWT);
    assertThat(response.getUser().getEmail()).isEqualTo(TEST_EMAIL);
    assertThat(response.getUser().getName()).isEqualTo(TEST_NAME);
  }

  @Test
  @DisplayName("존재하지 않는 이메일로 로그인 시 예외가 발생한다")
  void login_EmailNotFound() {
    // Given
    MemberDto.LoginRequest request = new MemberDto.LoginRequest();
    request.setEmail(TEST_EMAIL);
    request.setPassword(TEST_PASSWORD);

    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> memberService.login(request))
        .isInstanceOf(CustomException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
  }

  @Test
  @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다")
  void login_PasswordMismatch() {
    // Given
    MemberDto.LoginRequest request = new MemberDto.LoginRequest();
    request.setEmail(TEST_EMAIL);
    request.setPassword("wrongPassword");

    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
    when(passwordEncoder.matches("wrongPassword", TEST_PASSWORD)).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> memberService.login(request))
        .isInstanceOf(CustomException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_PASSWORD_MISMATCH);
  }

  @Test
  @DisplayName("현재 로그인된 회원 정보를 정상적으로 조회할 수 있다")
  void getCurrentMember_Success() {
    // Given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(TEST_JWT);
    when(jwtUtil.getEmailFromToken(TEST_JWT)).thenReturn(TEST_EMAIL);
    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));

    // When
    MemberDto.InfoResponse response = memberService.getCurrentMember();

    // Then
    assertThat(response.getEmail()).isEqualTo(TEST_EMAIL);
    assertThat(response.getName()).isEqualTo(TEST_NAME);
  }

  @Test
  @DisplayName("회원 정보 수정에 성공하면 수정된 정보를 반환한다")
  void updateCurrentMember_Success() {
    // Given
    MemberDto.UpdateRequest request = new MemberDto.UpdateRequest();
    request.setName("새이름");
    request.setPassword("newPassword");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(TEST_JWT);
    when(jwtUtil.getEmailFromToken(TEST_JWT)).thenReturn(TEST_EMAIL);
    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));
    when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
    when(memberRepository.save(any(Member.class))).thenReturn(testMember);

    // When
    MemberDto.InfoResponse response = memberService.updateCurrentMember(request);

    // Then
    assertThat(response.getEmail()).isEqualTo(TEST_EMAIL);
    verify(memberRepository).save(any(Member.class));
  }

  @Test
  @DisplayName("이름과 비밀번호가 모두 없으면 수정할 수 없다")
  void updateCurrentMember_NoData() {
    // Given
    MemberDto.UpdateRequest request = new MemberDto.UpdateRequest();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(TEST_JWT);
    when(jwtUtil.getEmailFromToken(TEST_JWT)).thenReturn(TEST_EMAIL);
    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));

    // When & Then
    assertThatThrownBy(() -> memberService.updateCurrentMember(request))
        .isInstanceOf(CustomException.class)
        .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_UPDATE_NO_DATA);
  }

  @Test
  @DisplayName("현재 로그인된 회원을 성공적으로 탈퇴 처리한다")
  void deleteCurrentMember_Success() {
    // Given
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(TEST_JWT);
    when(jwtUtil.getEmailFromToken(TEST_JWT)).thenReturn(TEST_EMAIL);
    when(memberRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testMember));

    // When
    memberService.deleteCurrentMember();

    // Then
    verify(memberRepository).delete(testMember);
  }
}
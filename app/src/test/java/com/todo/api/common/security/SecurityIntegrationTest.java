package com.todo.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.api.mmbr.domain.Member;
import com.todo.api.mmbr.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class SecurityIntegrationTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MemberRepository memberRepository;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    memberRepository.deleteAll();
  }

  protected Member createTestMember(String email, String password, String name) {
    Member member = new Member();
    member.setEmail(email);
    member.setPassword(passwordEncoder.encode(password));
    member.setName(name);

    return memberRepository.save(member);
  }
}
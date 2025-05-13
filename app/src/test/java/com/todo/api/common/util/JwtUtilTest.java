package com.todo.api.common.util;

import com.todo.api.common.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        // jwtConfig mocking
        jwtConfig = Mockito.mock(JwtConfig.class);
        Mockito.when(jwtConfig.getAccessTokenValidityInMinutes()).thenReturn(10L); // 10분 유효시간

        jwtUtil = new JwtUtil(jwtConfig);
    }

    @Test
    @DisplayName("토큰 생성 및 분석 테스트")
    void testGenerateAndParseToken() {
        String email = "test@example.com";
        Integer memberNo = 123;

        String token = jwtUtil.generateToken(email, memberNo);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(email, jwtUtil.getEmailFromToken(token));
        assertEquals(memberNo, jwtUtil.getMemberNoFromToken(token));
    }

    @Test
    @DisplayName("유효하지 않은 토큰 테스트")
    void testInvalidToken() {
        String invalidToken = "invalid.token.string";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰 테스트")
    void testExpiredToken() throws InterruptedException {
        // 1초짜리 만료 시간 설정
        Mockito.when(jwtConfig.getAccessTokenValidityInMinutes()).thenReturn(0L); // 0분 -> 바로 만료
        jwtUtil = new JwtUtil(jwtConfig);

        String token = jwtUtil.generateToken("test@example.com", 123);

        // 유효 시간 지나도록 잠시 대기
        Thread.sleep(1500);

        assertFalse(jwtUtil.validateToken(token));
    }
}
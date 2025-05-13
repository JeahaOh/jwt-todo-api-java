package com.todo.api;

import com.todo.api.common.security.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Tag("unit")
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class AppApplicationTests {

	@Test
	@DisplayName("간단한 계산 테스트")
	void simpleCalculation() {
		int result = 1 + 1;
		assertTrue(result == 2);
	}
}

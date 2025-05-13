package com.todo.api.health.controller;

import com.todo.api.common.CustomResponse;
import com.todo.api.health.dto.HealthCheckResponse;
import com.todo.api.health.service.HealthCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class HealthCheckControllerTest {

  @Mock
  private HealthCheckService healthCheckService;

  @InjectMocks
  private HealthCheckController healthCheckController;

  @Test
  @DisplayName("헬스 체크 API가 정상적으로 동작하는지 확인")
  void healthCheck() {
    // given
    HealthCheckResponse mockResponse = HealthCheckResponse.builder()
        .status("UP")
        .tableCount(3)
        .responseTime(100L)
        .timestamp("2024-03-20T10:00:00")
        .build();
    when(healthCheckService.checkHealth()).thenReturn(mockResponse);

    // when
    ResponseEntity<CustomResponse<HealthCheckResponse>> response = healthCheckController.healthCheck();

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getData()).isEqualTo(mockResponse);
    verify(healthCheckService, times(1)).checkHealth();
  }

  @Test
  @DisplayName("에러 테스트 API가 예외를 발생시키는지 확인")
  void testError() {
    // given
    doThrow(new RuntimeException("테스트 에러")).when(healthCheckService).testError();

    // when & then
    assertThatThrownBy(() -> healthCheckController.testError())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("테스트 에러");
    verify(healthCheckService, times(1)).testError();
  }
}
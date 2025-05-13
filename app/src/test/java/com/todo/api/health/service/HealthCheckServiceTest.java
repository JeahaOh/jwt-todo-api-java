package com.todo.api.health.service;

import com.todo.api.health.dto.HealthCheckResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class HealthCheckServiceTest {

  @Mock
  private EntityManager entityManager;

  @Mock
  private Query query;

  @InjectMocks
  private HealthCheckService healthCheckService;

  @Test
  @DisplayName("헬스 체크가 정상적으로 동작하는지 확인")
  void checkHealth() {
    // given
    when(entityManager.createNativeQuery("SELECT name FROM sqlite_master WHERE type='table'"))
        .thenReturn(query);
    List<String> mockTables = Arrays.asList("table1", "table2", "table3");
    when(query.getResultList()).thenReturn(mockTables);

    // when
    HealthCheckResponse response = healthCheckService.checkHealth();

    // then
    assertThat(response.getStatus()).isEqualTo("UP");
    assertThat(response.getTableCount()).isEqualTo(3);
    assertThat(response.getResponseTime()).isGreaterThanOrEqualTo(0);
    assertThat(response.getTimestamp()).isNotNull();
  }

  @Test
  @DisplayName("testError 메소드가 예외를 발생시키는지 확인")
  void testError() {
    // when & then
    assertThatThrownBy(() -> healthCheckService.testError())
        .isInstanceOfAny(IllegalArgumentException.class, RuntimeException.class, IllegalStateException.class);
  }
}
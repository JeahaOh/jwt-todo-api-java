package com.todo.api.service;

import com.todo.api.dto.HealthCheckResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class HealthCheckService {

  @PersistenceContext
  private EntityManager entityManager;

  public HealthCheckResponse checkHealth() {
    long startTime = System.currentTimeMillis();

    // SQLite 테이블 수 조회
    List<?> tables = entityManager.createNativeQuery(
        "SELECT name FROM sqlite_master WHERE type='table'").getResultList();

    long endTime = System.currentTimeMillis();
    long responseTime = endTime - startTime;

    return HealthCheckResponse.builder()
        .status("UP")
        .tableCount(tables.size())
        .responseTime(responseTime)
        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
        .build();
  }

  public void testError() {
    int random = (int) (Math.random() * 3);
    log.info("random : {}", random);

    switch (random) {
      case 0:
        throw new IllegalArgumentException("잘못된 인자가 전달되었습니다.");
      case 1:
        throw new RuntimeException("서버 내부 오류가 발생했습니다.");
      default:
        throw new IllegalStateException("잘못된 상태입니다.");
    }
  }
}
package com.todo.api.controller;

import com.todo.api.dto.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Health Check API")
public class HealthCheckController {

  @PersistenceContext
  private EntityManager entityManager;

  @GetMapping
  @Operation(summary = "Health Check", description = "서버 상태, 테이블 수, 응답 시간을 확인합니다.")
  public HealthCheckResponse healthCheck() {
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
}
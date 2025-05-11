package com.todo.api.controller;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.util.ResponseUtil;
import com.todo.api.dto.HealthCheckResponse;
import com.todo.api.service.HealthCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Health Check API")
@RequiredArgsConstructor
public class HealthCheckController {

  private final HealthCheckService healthCheckService;

  @GetMapping
  @Operation(summary = "Health Check", description = "서버 상태, 테이블 수, 응답 시간을 확인합니다.")
  public ResponseEntity<CustomResponse<HealthCheckResponse>> healthCheck() {
    HealthCheckResponse response = healthCheckService.checkHealth();
    return ResponseEntity.ok(ResponseUtil.success("Health check completed successfully", response));
  }

  @GetMapping("/error")
  @Operation(summary = "Error Test", description = "예외 발생을 테스트합니다.")
  public ResponseEntity<CustomResponse<Void>> testError() {
    healthCheckService.testError();
    return ResponseEntity.ok().build();
  }
}
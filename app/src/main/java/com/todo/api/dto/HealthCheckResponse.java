package com.todo.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthCheckResponse {
  private String status;
  private int tableCount;
  private long responseTime;
  private String timestamp;
}
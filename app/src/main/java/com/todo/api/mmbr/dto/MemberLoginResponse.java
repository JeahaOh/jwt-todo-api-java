package com.todo.api.mmbr.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberLoginResponse {
  private String accessToken;
  private UserInfo user;

  @Getter
  @Builder
  public static class UserInfo {
    private Integer no;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }
}